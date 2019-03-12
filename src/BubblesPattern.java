import java.util.ArrayList;
import java.util.List;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.LXParameter;

/* Bubbles.
 * This pattern Copyright(c)2019 Justin Belcher, use with permission only.
 */
@LXCategory("Colossal Collective")
public class BubblesPattern extends MantisPattern {

    public final CompoundParameter density = 
            new CompoundParameter("Density", .2, 0.05, .45)
            .setDescription("Density of bubbles");
    
    public final CompoundParameter minBubbleSpeed = 
            new CompoundParameter("MinSpeed", 5, 3, 50)
            .setDescription("Minimum pixel moves per second");
    
    public final CompoundParameter maxBubbleSpeed = 
            new CompoundParameter("MaxSpeed", 60, 5, 75)
            .setDescription("Maximum pixel moves per second");

    private List<LimbBubbleCollection> edges;

    public BubblesPattern(LX lx) {
        super(lx);
        
        addParameter(density);
        addParameter(minBubbleSpeed);
        addParameter(maxBubbleSpeed);
    }

    @Override
    public void onActive() {
        super.onActive();
        this.safetyCheckParameters();
        initialize();
    }
    
    @Override
    public void setRandomParameters() {
        super.setRandomParameters();
        randomizeParameter(this.density);
        randomizeParameter(this.minBubbleSpeed);
        this.minBubbleSpeed.setValue(this.minBubbleSpeed.getValue() / 2);
        randomizeParameter(this.maxBubbleSpeed, this.minBubbleSpeed.getValue(), this.maxBubbleSpeed.range.max);
    }
    
    void safetyCheckParameters() {
        if (this.maxBubbleSpeed.getValuef() <= this.minBubbleSpeed.getValuef() + 1) {
            this.maxBubbleSpeed.setValue(this.minBubbleSpeed.getValue() + 5);
        }
    }

    private void initialize() {
        this.edges = new ArrayList<LimbBubbleCollection>();

        float density = this.density.getValuef();
        
        for (PuppetPixelGroup edge : this.model.allLimbs) {
            LimbBubbleCollection c = new LimbBubbleCollection();
            c.edge = edge;
            c.maxPos = edge.size() - 1;
            int numBubbles = (int) (((float) edge.size()) * density);
            for (int b = 0; b < numBubbles; b++) {
                Bubble newBubble = createBubble();
                c.bubbles.add(newBubble);
            }

            this.edges.add(c);
        }
    }

    private Bubble createBubble() {
        Bubble b = new Bubble();
        
        if (this.isRainbow.getValueb()) {
            b.color = getRandomColor(); 
        } else {
            b.color = LXColor.hsb(this.hue1.getValuef(), this.saturation1.getValuef(), this.brightness1.getValuef());
        }
        
        b.pos = 0;

        this.safetyCheckParameters();
        float minSpeed = this.minBubbleSpeed.getValuef();
        float maxSpeed = this.maxBubbleSpeed.getValuef();
        float speedRange = Math.max(maxSpeed - minSpeed, 1);
        float pixelsPerSec = (float) ((Math.random() * speedRange) + minSpeed);

        b.timePerMove = 1000f / pixelsPerSec;
        b.nextMoveTime = this.runMs + b.timePerMove;

        return b;
    }

    Boolean beatDetected = false;

    public void onBeatPressed(LXParameter p) {
        this.beatDetected = true;
    }

    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        int solidColor = LXColor.hsb(this.hue1.getValuef(), this.saturation1.getValuef(), this.brightness1.getValuef());
        boolean isRainbow = this.isRainbow.getValueb();

        // Foreach bubble: shift position if it's time. Create new bubble if it's beyond max position.
        for (LimbBubbleCollection ebc : this.edges) {

            List<Bubble> expiredBubbles = new ArrayList<Bubble>();

            for (Bubble bubble : ebc.bubbles) {
                // Is it time to increment?
                if (this.runMs > bubble.nextMoveTime) {
                    bubble.pos++;
                    bubble.nextMoveTime = this.runMs + bubble.timePerMove;
                    if (bubble.pos > ebc.maxPos) {
                        expiredBubbles.add(bubble);
                        //newBubbles.add(createBubble());
                    }
                }
            }

            // Remove expired bubbles
            for (Bubble expiredBubble : expiredBubbles) {
                ebc.bubbles.remove(expiredBubble);
            }
            
            // Create new bubbles
            // *Could change this to calculate density across whole structure, and add to random group.
            int targetNumBubbles = (int)(this.density.getValuef() * (float)ebc.edge.size());
            int numNewBubbles = targetNumBubbles - ebc.bubbles.size();
            for (int n = 0; n < numNewBubbles; n++) {
                ebc.bubbles.add(createBubble());
            }
            
            // Render every bubble
            for (Bubble bubble : ebc.bubbles) {
                if (isRainbow) {
                    colors[ebc.edge.getPoint(bubble.pos).index] = bubble.color;                    
                } else {
                    colors[ebc.edge.getPoint(bubble.pos).index] = solidColor;                    
                }
            }
        }
    }

    public class Bubble {
        public int pos;
        public double timePerMove;
        public int color;
        public double nextMoveTime;
    }

    public class LimbBubbleCollection {
        public PuppetPixelGroup edge;
        //PixelDirection edgeDirection;
        public int maxPos;
        public List<Bubble> bubbles = new ArrayList<Bubble>();
    }
}

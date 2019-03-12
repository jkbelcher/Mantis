import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.CompoundParameter;

@LXCategory("Colossal Collective")
public class RainbowShiftPattern extends MantisPatternNormalized {

    public final CompoundParameter hueRange = 
            new CompoundParameter("hueRange", 128, 5, 400)
            .setDescription("hueRange");
    
    float huePos = 0;

    public RainbowShiftPattern(LX lx) {
        super(lx);

        addParameter(hueRange);
        this.setSpeedRange(.001, 1.1);
    }
    
    public void setRandomParameters() {
        randomizeParameter(this.hueRange);
        randomizeParameter(this.speed);
    }

    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        float hueRange = this.hueRange.getValuef();
        
        // Calc current position
        float speed = this.getSpeedf();
        float degreesMoved = hueRange*speed*(((float) deltaMs)/1000f);
        
        huePos -= degreesMoved;
        huePos %= 360;
        
        for (INormalizedScope scope : this.scopes) {
            for (NormalizedPoint np : scope.getPointsNormalized()) {
                float n = np.rn;
                float hue = ((n * hueRange) + huePos) %360;
                colors[np.p.index] = LXColor.hsb(hue, 100, 100);
            }
        }
    }

}

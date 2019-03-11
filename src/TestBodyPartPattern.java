import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.parameter.BooleanParameter.Mode;
import processing.core.PApplet;

@LXCategory("Test")
public class TestBodyPartPattern extends MantisPattern {

    public final DiscreteParameter bodyPart = 
            new DiscreteParameter("BodyPart", 1, 1, 3+1)
            .setDescription("BodyPart");
          
        public final DiscreteParameter rightLeft = 
                new DiscreteParameter("RL", 1, 1, 2+1)
                .setDescription("RightLeft");

        public final BooleanParameter identify = 
                new BooleanParameter("Identify")
                .setDescription("Identify the current values")
                .setMode(Mode.MOMENTARY);    
        
        
    public TestBodyPartPattern(LX lx) {
        super(lx);
        
        addParameter(bodyPart);
        addParameter(rightLeft);
        addParameter(identify);  
    }
        
    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        int bodyPart = this.bodyPart.getValuei();
        int rightLeft = this.rightLeft.getValuei();
        
        //Hacky solution to help us identify configuration issues
        if (this.identify.getValueb()) {
            PApplet.println("BodyPart: " + bodyPart + "  RightLeft: " + rightLeft);
        }
        
        for (PuppetPixel pp : this.model.puppetPixels) {
            if (pp.params.bodyPart == bodyPart && pp.params.rightLeft == rightLeft) {
                colors[pp.p.index] = LXColor.RED;
            }
        }

    }

}

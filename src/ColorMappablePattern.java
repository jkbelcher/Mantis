import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.DiscreteParameter;

@LXCategory("Colossal Collective RETIRED")
public class ColorMappablePattern extends MantisPattern {

    public final DiscreteParameter red = 
            new DiscreteParameter("Red", 0, 0, 255)
            .setDescription("Red");
    
    public final DiscreteParameter green = 
            new DiscreteParameter("Green", 0, 0, 255)
            .setDescription("Green");
    
    public final DiscreteParameter blue = 
            new DiscreteParameter("Blue", 0, 0, 255)
            .setDescription("Blue");
    
    public ColorMappablePattern(LX lx) {
        super(lx);
        
        addParameter(red);
        addParameter(green);
        addParameter(blue);      
    }

    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        int red = this.red.getValuei();
        int green = this.green.getValuei();
        int blue = this.blue.getValuei();
        float brightness = this.brightness1.getValuef();
        float saturation = this.saturation1.getValuef();
        
        int c = LXColor.hsb(LXColor.h(LXColor.rgb(red, green, blue)), saturation, brightness);

        for (LXPoint p : this.model.getPoints()) {
            colors[p.index] = c;
        }
    }

}

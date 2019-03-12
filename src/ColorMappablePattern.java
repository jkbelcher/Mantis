import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.DiscreteParameter;

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
    
    public final CompoundParameter brightness = 
            new CompoundParameter("Brightness", 0, 0, 1)
            .setDescription("Brightness");
    
    public ColorMappablePattern(LX lx) {
        super(lx);
        
        addParameter(red);
        addParameter(green);
        addParameter(blue);
        //addParameter(brightness);        
    }

    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        int red = this.red.getValuei();
        int green = this.green.getValuei();
        int blue = this.blue.getValuei();
        float brightness = this.brightness.getValuef();
        
        int c = LXColor.hsb(LXColor.h(LXColor.rgb(red, green, blue)), 100, brightness);

        for (LXPoint p : this.model.getPoints()) {
            colors[p.index] = c;
        }
    }

}

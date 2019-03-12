import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.LXUtils;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.BoundedParameter;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.parameter.BoundedParameter.Range;

// To write a pattern that only uses the three dimensional coordinates of points, a pattern class can directly extend LXPattern.
// To write a pattern that uses collections specific to the Puppet model, extend the MantisPattern class.
// Then from within your pattern you can access the this.model property.
// This prevents each pattern from needing to cast LXModel to JouleModel.

public abstract class MantisPattern extends RandomizableLXPattern {

    public final CompoundParameter hue1 = 
            new CompoundParameter("Hue1", LXColor.h(LXColor.RED), 0, 360)
            .setDescription("Hue1");
    
    public final CompoundParameter saturation1 = 
            new CompoundParameter("Sat1", 100, 0, 100)
            .setDescription("Saturation1");
    
    public final CompoundParameter brightness1 = 
            new CompoundParameter("Bright1", 100, 0, 100)
            .setDescription("Brightness1");
    
    public final CompoundParameter hue2 = 
            new CompoundParameter("Hue2", LXColor.h(LXColor.RED), 0, 360)
            .setDescription("Hue1");
    
    public final CompoundParameter saturation2 = 
            new CompoundParameter("Sat2", 100, 0, 100)
            .setDescription("Saturation1");
    
    public final CompoundParameter brightness2 = 
            new CompoundParameter("Bright2", 0, 0, 100)
            .setDescription("Brightness1");
    
    public final BooleanParameter isRainbow = 
            new BooleanParameter("Rainbow")
            .setDescription("Rainbow colors, if available")
            .setMode(BooleanParameter.Mode.TOGGLE);
    
    public final CompoundParameter size = 
            new CompoundParameter("Size", .1, 0, 1)
            .setDescription("Size");    

    public final CompoundParameter speed = 
            new CompoundParameter("Speed", .2, 0, 1)
            .setDescription("Speed");
    
    protected final MantisModel model;
    
    private Range adjSize = new Range(0, 1);
    private Range adjSpeed = new Range(0, 1);

    public MantisPattern(LX lx) {
        super(lx);
        this.model = (MantisModel)lx.getModel();
        
        addParameter(hue1);
        addParameter(saturation1);
        addParameter(brightness1);        
        addParameter(size);
        addParameter(hue2);
        addParameter(saturation2);
        addParameter(brightness2);        
        addParameter(speed);
        addParameter(isRainbow);
    }
    
    protected void setSizeRange(int min, int max) {
        this.adjSize = new Range(min, max);        
    }

    protected void setSizeRange(double min, double max) {
        this.adjSize = new Range(min, max);        
    }

    protected void setSpeedRange(int min, int max) {
        this.adjSpeed = new Range(min, max);        
    }
    
    protected void setSpeedRange(double min, double max) {
        this.adjSpeed = new Range(min, max);        
    }

    protected double getSize() {
        return this.adjSize.normalizedToValue(this.size.getValue());
    }
    
    protected float getSizef() {
        return (float)this.getSize();
    }

    protected double getSpeed() {
        return this.adjSpeed.normalizedToValue(this.speed.getValue());
    }
    
    protected float getSpeedf() {
        return (float)this.getSpeed();
    }

    public void setRandomParameters() {
        randomizeParameter(this.hue1);
        randomizeParameter(this.hue2);
        //randomizeParameter(this.saturation1);
        randomizeParameter(this.brightness1);
        randomizeParameter(this.brightness2);
        randomizeParameter(this.isRainbow);
        randomizeParameter(this.size);
        randomizeParameter(this.speed);
    }    
    
    protected int getColor1() {
        return LXColor.hsb(this.hue1.getValuef(), this.saturation1.getValuef(), this.brightness1.getValuef());
    }

    protected int getColor2() {
        return LXColor.hsb(this.hue2.getValuef(), this.saturation2.getValuef(), this.brightness2.getValuef());
    }
    
    public double getRandomHue() {
        return Math.random() * 360;
    }

}

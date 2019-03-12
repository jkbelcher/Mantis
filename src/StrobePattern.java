import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.LXParameterListener;
import heronarts.lx.parameter.BooleanParameter.Mode;

@LXCategory("Colossal Collective")
public class StrobePattern extends MantisPattern {

    public final BooleanParameter strobe = 
            new BooleanParameter("Strobe")
            .setDescription("Strobe!")
            .setMode(Mode.MOMENTARY);
    
    public final DiscreteParameter numFlashes = 
            new DiscreteParameter("Flashes", 5, 1, 20)
            .setDescription("Number of flashes");

    public final CompoundParameter timeOn = 
            new CompoundParameter("TimeOn", .05, .005, 0.2)
            .setDescription("Length in seconds for each flash.");

    public final CompoundParameter timeOff = 
            new CompoundParameter("TimeOff", .1, .005, 0.3)
            .setDescription("Length in seconds between flashes");
    
    public StrobePattern(LX lx) {
        super(lx);

        addParameter(strobe);
        addParameter(numFlashes);
        addParameter(timeOn);
        addParameter(timeOff);

        this.strobe.addListener(new LXParameterListener() {
            public void onParameterChanged(LXParameter p) {
                if (((BooleanParameter)p).getValueb()) {
                    onStrobe();
                }
            }
            });
    }
    
    protected void onStrobe() {
        this.remainingFlashes = this.numFlashes.getValuei();
        calcNextFlashTimes(this.runMs);
    }
    
    protected void calcNextFlashTimes(double beginTime) {
        this.start = beginTime;
        this.endOn = this.start + (this.timeOn.getValue() * 1000);
        this.endOff = this.endOn + (this.timeOff.getValue() * 1000);
    }

    int remainingFlashes = 0;
    double start;
    double endOn;
    double endOff;
    
    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        if (remainingFlashes > 0) {
            //Decrement number of flashes if expired
            if (this.runMs > this.endOff) {
                
                this.remainingFlashes--;
                if (this.remainingFlashes == 0)
                    return;
                
                calcNextFlashTimes(this.endOff);                
            }
            
            float outputLevel = this.runMs > this.endOn ? 0f : 1f;
            
            int color1 = LXColor.hsb(this.hue1.getValuef(), this.saturation1.getValuef(), this.brightness1.getValuef() * outputLevel);
            
            for (PuppetPixelGroup ppg : this.model.allLimbs) {
                for (int i = 0; i < ppg.size(); i++) {
                    PuppetPixel pp = ppg.puppetPixels.get(i);
                    colors[pp.getIndexColor()] = color1;
                }                
            }
        }    
    }

}

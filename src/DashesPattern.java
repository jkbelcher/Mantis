import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.DiscreteParameter;

@LXCategory("Colossal Collective")
public class DashesPattern extends MantisPattern {

    public final DiscreteParameter lengthOff = 
            new DiscreteParameter("LenOff", 1, 0, 30)
            .setDescription("Number of pixels between each dash");    
    public final CompoundParameter fade = 
            new CompoundParameter("Soft", .1, .05, .4)
            .setDescription("Softness, or Percentage of dash that fades out to black.");    
    public final CompoundParameter speed = 
            new CompoundParameter("Speed", 5, 0, 60)
            .setDescription("Pixel moves per second");

    public DashesPattern(LX lx) {
        super(lx);

        this.setSizeRange(2, 30);
        this.setSpeedRange(0, 60);        
        
        addParameter(lengthOff);
        addParameter(fade);

        this.setSpeedRange(0, 60);
    }
    
    public void setRandomParameters() {
        super.setRandomParameters();
        randomizeParameter(this.lengthOff);
        randomizeParameter(this.fade);
    }
    
    float pos = 0;

    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        float hue = this.hue1.getValuef();
        float saturation = this.saturation1.getValuef();
        float brightness = this.brightness1.getValuef();
        
        float speed = this.getSpeedf();
        float lengthf = this.getSizef();
        int length = (int)lengthf;        
        int lengthOff = this.lengthOff.getValuei();
        float fade = this.fade.getValuef();
        float fadeLen = fade * lengthf;
        
        int totalLen = length + lengthOff;
        float totalLenf = (float)totalLen;
        
        //Decrement the position, which visually advances the pattern
        this.pos -= speed * (float)deltaMs / 1000f;
        if (pos < 0) {
            pos += totalLenf;
        }
        
        //Calculate array of brightnesses for this frame
        float bright[] = new float[totalLen];
        float offset = pos;
        for (int iBright = 0; iBright < bright.length ; iBright++) {
            if (offset < fadeLen) {
                bright[iBright] = (offset / fadeLen) * brightness; 
            } else if (offset < lengthf - fadeLen) {
                bright[iBright] = brightness;
            } else if (offset < lengthf) {
                bright[iBright] = ((lengthf-offset) / fadeLen) * brightness;                
            } else {
                bright[iBright] = 0f;
            }
            
            offset += 1f;
            if (offset >= totalLenf) {
                offset -= totalLenf;
            }            
        }

        //Draw it
        for (PuppetPixelGroup ppg : this.model.allLimbs) {
            int b=0;
            for (int i = 0; i < ppg.size(); i++) {
                PuppetPixel pp = ppg.puppetPixels.get(i);
                colors[pp.getIndexColor()] = LXColor.hsb(hue, saturation, bright[b]);
                
                //Cycle through the brightness array
                b++;
                b %= bright.length;
            }
        }

    }

}

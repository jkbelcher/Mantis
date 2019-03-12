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
        
        float hue1 = this.hue1.getValuef();
        float saturation1 = this.saturation1.getValuef();
        float brightness1 = this.brightness1.getValuef();

        float hue2 = this.hue2.getValuef();
        float saturation2 = this.saturation2.getValuef();
        float brightness2 = this.brightness2.getValuef();
        
        float speed = this.getSpeedf();
        float lengthf = this.getSizef();
        int length = (int)lengthf;        
        int lengthOff = this.lengthOff.getValuei();
        float fade = this.fade.getValuef();
        float fadeLen = fade * lengthf;
        
        int halfLen = length + lengthOff;
        float halfLenf = (float)halfLen;
        int totalLen = halfLen * 2;
        float totalLenf = (float)totalLen;
        
        //Decrement the position, which visually advances the pattern
        this.pos -= speed * (float)deltaMs / 1000f;
        if (pos < 0) {
            pos += totalLenf;
        }
        
        //Calculate array of brightnesses for this frame
        int dashColors[] = new int[totalLen];
        float offset = pos;
        if (brightness2 == 0) {
            hue2 = hue1;
            saturation2 = saturation1;
            brightness2 = brightness1;
        }
        // *Need to fix fade out on first color.  This was rushed.
        for (int iBright = 0; iBright < totalLen ; iBright++) {
            float brightPixel;
            if (offset < fadeLen) {
                brightPixel = (offset / fadeLen); 
            } else if (offset < lengthf - fadeLen) {
                brightPixel = 1f;
            } else if (offset < lengthf) {
                brightPixel = ((lengthf-offset) / fadeLen);                
            } else if (offset < halfLenf){
                brightPixel = 0f;
            } else if (offset < halfLenf + fadeLen) {
                brightPixel = ((offset-halfLenf) / fadeLen);
            } else if (offset < halfLenf + lengthf - fadeLen) {
                brightPixel = 1f;                
            } else if (offset < (halfLenf + lengthf-offset) / fadeLen) {
                brightPixel = ((lengthf-(offset-halfLenf)) / fadeLen);
            } else {
                brightPixel = 0f;
            }                
            
            if (offset > halfLen)
                dashColors[iBright] = LXColor.hsb(hue1, saturation1, brightPixel * brightness1);
            else
                dashColors[iBright] = LXColor.hsb(hue2, saturation2, brightPixel * brightness2);                
            
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
                colors[pp.getIndexColor()] = dashColors[b];
                
                //Cycle through the brightness array
                b++;
                b %= dashColors.length;
            }
        }

    }

}

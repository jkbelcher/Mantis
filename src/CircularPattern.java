import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;

@LXCategory("Colossal Collective")
public class CircularPattern extends MantisPatternNormalized {

    public CircularPattern(LX lx) {
        super(lx);

        this.autoRandom.setValue(false);
        this.speed.setValue(0);

        // Speed is normal ranges shift per second
        this.setSpeedRange(0, 2);
        this.setSizeRange(.25, 2);
    }
    
    float offset = 0;

    @Override
    protected void run(double deltaMs) {
        this.clearColors();
        
        int color1 = this.getColor1();
        int color2 = this.getColor2();
        float size = this.getSizef();

        double speed = this.getSpeed();
        double amtMoved = speed / 1000.0 * deltaMs;
        this.offset -= amtMoved;
        while (this.offset < 0)
            this.offset += 1;
        
        for (INormalizedScope scope : this.scopes) {
            for (NormalizedPoint np : scope.getPointsNormalized()) {
                float n = np.rn;
                n *= size;
                n += this.offset;
                while (n > 1.0)
                    n -= 1.0;
                if (n < 0.5)
                    colors[np.p.index] = LXColor.lerp(color1, color2, n*2);
                else
                    colors[np.p.index] = LXColor.lerp(color1, color2, (1-n)*2);
            }
        }        
    }
}

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;

@LXCategory("Colossal Collective")
public class SolidColorMantisPattern extends MantisPattern {

    public SolidColorMantisPattern(LX lx) {
        super(lx);
    }
    
    @Override
    protected void run(double deltaMs) {
        this.clearColors();

        //int color1 = ;
        this.setColors(this.getColor1());

        /*
        for (LXPoint p : this.model.getPoints()) {
            colors[p.index] = color;
        }
        */
    }

}

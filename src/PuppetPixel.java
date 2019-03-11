import heronarts.lx.model.LXPoint;
import processing.core.PApplet;

public class PuppetPixel extends LXAbstractFixtureMapped implements Comparable<PuppetPixel> {

    public final PuppetPixelParameters params;
    public final LXPoint p;
    public final int feather;
    public final int panel;

    public PuppetPixel(PuppetPixelParameters params) {
        this.params = params;
        //PApplet.println("PuppetPixel: ",this.params.x, this.params.y, this.params.z);

        //Each LXPoint should be created only once.  We'll do it here, in the PuppetPixel constructor.
        LXPoint lxPoint = new LXPoint(params.x, params.y, params.z);
        this.p = lxPoint;
        
        this.feather = this.params.feather;
        this.panel = this.params.panel;
    }

    @Override
    public int compareTo(PuppetPixel o) {
        int comparePosition = o.params.position;
        return comparePosition - this.params.position;
    }
    
    public Boolean isFeatherPixel() {
        return this.feather > 0;        
    }
    
    public Boolean isPanelPixel() {
        return this.panel > 0;
    }
    
    public Boolean isBodyPixel() {
        return this.panel==0 && this.feather==0 && this.params.controllerChannel == 13;
    }

    public Boolean isNeckPixel() {
        return this.panel==0 && this.feather==0 && this.params.controllerChannel == 14;
    }

    public Boolean isEyePixel() {
        return this.panel==0 && this.feather==0 && this.params.controllerChannel == 30;
    }

}

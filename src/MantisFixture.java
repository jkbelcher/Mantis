import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import heronarts.lx.model.LXPoint;
import processing.core.PApplet;

// For the Mantis each channel is a fixture.
// Each limb is a fixture/channel.
public class MantisFixture extends LXAbstractFixtureMapped implements Comparable<MantisFixture> {

    public final int channel;
    public final MantisController controller;
    public final List<PuppetPixel> puppetPixels = new ArrayList<PuppetPixel>();
    
    public MantisFixture(int channel, MantisController controller) {
        this.channel = channel;
        this.controller = controller;
        //PApplet.println("Fixture "+this.channel);
    }
    
    @Override
    public int compareTo(MantisFixture o) {
        int compareChannel = o.channel;
        return compareChannel - this.channel;
    }

    public void addPuppetPixel(PuppetPixel puppetPixel)
    {
        this.puppetPixels.add(puppetPixel);
        this.addPoint(puppetPixel.p);
    }
    
    //Called once after model is loaded
    protected void setLoaded() {
        Collections.sort(puppetPixels);
    }
        
    //Return, in physical order, the indices of the LXPoints in this channel.
    //The indices are derived from the order in which the points were loaded into the model,
    //and therefore don't necessarily match anything physical.
    public int[] getPointIndicesForOutput() {
        int[] indices = new int[this.puppetPixels.size()];
        int i = 0;
        for (PuppetPixel tp : this.puppetPixels) {
            indices[i++] = tp.p.index;
          }
        return indices;        
    }
    
}

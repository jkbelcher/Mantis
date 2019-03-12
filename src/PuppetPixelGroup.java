import java.util.ArrayList;
import java.util.List;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXPoint;

// *This class could use some cleanup, along with MantisFixture and MantisModel
public class PuppetPixelGroup extends LXAbstractFixture implements INormalizedScope {

	public final List<PuppetPixel> puppetPixels;
	public int id = 0;
	
	public PuppetPixelGroup() {
	    this(0);
	}

	public PuppetPixelGroup(int id) {
	    this.id=id;
	    puppetPixels = new ArrayList<PuppetPixel>();
    }
	
	public PuppetPixelGroup(MantisFixture fixture) {
	    this.id = 0;
        puppetPixels = new ArrayList<PuppetPixel>();
        for (PuppetPixel pp : fixture.puppetPixels) {
            this.addPuppetPixel(pp);
        }	    
	}

	public PuppetPixelGroup addPuppetPixel(PuppetPixel newItem) {
		this.puppetPixels.add(newItem);
		this.addPoint(newItem.p);
		return this;
	}
	
	public PuppetPixelGroup addPuppetPixels(PuppetPixelGroup copy) {
	    for (PuppetPixel pp : copy.puppetPixels) {
	        this.addPuppetPixel(pp);
	    }
	    return this;
	}
	
    public List<LXPoint> getPoints() {
        return this.points;
    }

    public LXPoint getPoint(int index) {
        return this.points.get(index);
    }
/*	
	public PuppetPixelGroup copyIndicesToChildren() {
	    for (int i = 0; i<this.puppetPixels.size(); i++) {
	        this.puppetPixels.get(i).setIndexGroup(i);
	    }
	    return this;
	}	
	

*/
	public int size() {
	    return this.puppetPixels.size();
	}
	
    // INormalizedScope
    
    NormalScope normalScope = null;
    
    protected final List<NormalizedPoint> normalizedPoints = new ArrayList<NormalizedPoint>();
    
    protected void computeNormalized() {
        this.normalScope = new NormalScope(this);        
        for (LXPoint p : this.getPoints()) {
            this.normalizedPoints.add(new NormalizedPoint(p, this.normalScope));
        }
    }
    
    public NormalScope getNormalScope() {
        return this.normalScope;
    }
    
    public List<NormalizedPoint> getPointsNormalized() {
        return this.normalizedPoints;
    }
    
    public int countChildScopes() {
        return 0;
    }
    
    public List<INormalizedScope> getChildScope(int index) {
        switch (index) {
        default:
            throw new IllegalArgumentException("An invalid child scope was requested: " + this.getClass() + " " + index);                
        }
    }
    
    // end INormalizedScope   
}

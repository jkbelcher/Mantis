import java.util.ArrayList;
import java.util.List;

import heronarts.lx.model.LXAbstractFixture;

public class PuppetPixelGroup extends LXAbstractFixture {

	public final List<PuppetPixelPos> puppetPixels;
	public int id = 0;
	
	public PuppetPixelGroup() {
	    this(0);
	}

	public PuppetPixelGroup(int id) {
	    this.id=id;
	    puppetPixels = new ArrayList<PuppetPixelPos>();
    }

	public PuppetPixelGroup addPuppetPixelPosition(PuppetPixelPos newItem) {
		this.puppetPixels.add(newItem);
		this.addPoint(newItem.getPoint());
		return this;
	}
	
	public PuppetPixelGroup copyIndicesToChildren() {
	    for (int i = 0; i<this.puppetPixels.size(); i++) {
	        this.puppetPixels.get(i).setIndexGroup(i);
	    }
	    return this;
	}	
	
	public PuppetPixelGroup calculateNormalsByIndex()	{
		//Once all children have been added to the group,
		//calculate the normalized positions of children based on index
		
		//Justin's thoughts: The range of normals will be 0..1.  Let's have 0 include no points
		//and 1 include all points.  To do this, the first normalized position will be 1/[number of pixels].
		//The last normalized position will be [number of pixels]/[number of pixels] = 1.
		//No pixel within the group will have a normalized position of zero.
		//Note this is true for our PuppetPixelGroups but not necessarily for other normalized
		//positions in the LX framework.
		
		float numPixels = (float)this.puppetPixels.size();
		
		for (PuppetPixelPos item : this.puppetPixels) {
			item.setNormal(((float)(item.getIndexGroup()+1))/numPixels);			
		}		
		
		return this;
	}

	public int size() {
	    return this.puppetPixels.size();
	}
}

import heronarts.lx.model.LXPoint;

public class PuppetPixelPos {

	private final PuppetPixel pixel;
	private int indexGroup;
	private float n;    //Normalized position (0..1) within the parent PuppetPixelGroup
	
	public PuppetPixelPos(PuppetPixel pixel) {		
		this(pixel, 0);
	}
	
	public PuppetPixelPos(PuppetPixel pixel, int indexGroup) {
		this.pixel = pixel;
		this.indexGroup = indexGroup;
		this.n = 0f;
	}

	public PuppetPixelPos setIndexGroup(int index) {
		this.indexGroup = index;
		return this;
	}
	
	public int getIndexGroup() {
	    return this.indexGroup;
	}
	
	public PuppetPixelPos setNormal(float n) {
		this.n = n;
		return this;
	}
	
	public float getN() {
	    return this.n;
	}
	
	public LXPoint getPoint() {
	    return this.pixel.p;
	}
	
	//This is the index of the LXPoint within the model, which is the index of this point in the color buffer. 
	public int getIndexColor() {
        return this.pixel.p.index;
    }
	
	public int getPanel() {
	    return this.pixel.params.panel;
	}
	
	public int getFeather() {
	    return this.pixel.params.feather;
	}
	
	public int getRung() {
	    return this.pixel.params.rung;
	}
	
	public int getSpiral() {
	    return this.pixel.params.spiral;
	}
	
	public int getPosition() {
	    return this.pixel.params.position;
	}
    
    public Boolean isFeatherPixel() {
        return this.pixel.isFeatherPixel();        
    }
    
    public Boolean isPanelPixel() {
        return this.pixel.isPanelPixel();
    }
}

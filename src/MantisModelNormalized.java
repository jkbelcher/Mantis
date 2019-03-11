import java.util.ArrayList;
import java.util.List;

import heronarts.lx.model.LXFixture;

/**
 * This is a virtual wrapper around the PeacockModel
 * which allows interchanging of a target PuppetPixelGroup
 * without affecting the calling class.
 */
public class MantisModelNormalized {

    public final MantisModel model;
    
    private final List<PuppetPixelGroup> puppetPixelGroups;    
    private int index;    
    private PuppetPixelGroup group;
    
    public MantisModelNormalized(MantisModel model)
    {
        this(model, new PuppetPixelGroup[] { model.feathersLR, model.panelsLR, model.spiralsCW_IO, model.spiralsCCW_IO });
    }

    public MantisModelNormalized(MantisModel model, PuppetPixelGroup[] groups) {
        this.model = model;
        
        puppetPixelGroups = new ArrayList<PuppetPixelGroup>();
        for (PuppetPixelGroup group : groups) {
            if (group.size() > 0)
                puppetPixelGroups.add(group);
        }
        
        setIndex(0);
    }

    public int getIndex () {
        return this.index;
    }
    
    public MantisModelNormalized setIndex(int index) {
        if (index < this.puppetPixelGroups.size()) {
            this.index = index;
            this.group = this.puppetPixelGroups.get(index);
        }
        return this;
    }
    
    public PuppetPixelGroup getPuppetPixelGroup() {
        return this.group;
    }
    
    public Object[] getPuppetPixelGroupArray() {
        //_fixtures.toArray(new LXFixture[_fixtures.size()])
        return this.puppetPixelGroups.toArray();
    }
    
    public MantisModelNormalized setPuppetPixelGroup(PuppetPixelGroup newGroup) {
        int newIndex = puppetPixelGroups.indexOf(newGroup);
        
        if (newIndex > -1) {
            this.index = newIndex;
            this.group = newGroup;
        }
        
        return this;
    }
    
    public int numberPuppetPixelGroups() {
        return this.puppetPixelGroups.size();
    }

    public MantisModelNormalized goNext() {
        this.setIndex((index+1)%this.puppetPixelGroups.size());
        return this;
    }
    
    public MantisModelNormalized goRandom() {
        this.setIndex((int)(Math.random()*this.puppetPixelGroups.size()));
        return this;
    }
    
}

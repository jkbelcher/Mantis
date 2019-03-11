import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import heronarts.lx.model.LXAbstractFixture;
import processing.core.PApplet;

public class MantisController extends LXAbstractFixture {

    public final ControllerParameters params;
    public final List<MantisFixture> fixtures;
    public final TreeMap<Integer,MantisFixture> fixturesDict;

    public MantisController(ControllerParameters params) {
        this.params = params;
        this.fixtures = new ArrayList<MantisFixture>();
        this.fixturesDict = new TreeMap<Integer,MantisFixture>();
        
        PApplet.println("Controller "+this.params.id+": "+this.params.ipAddress+":"+this.params.port);
    }
    
    public MantisController addFixture(MantisFixture fixture) {
        this.fixtures.add(fixture);
        this.fixturesDict.put(fixture.channel, fixture);
        
        return this;
    }
    
    public MantisFixture getFixture(int key) {
        return this.fixturesDict.get(key);
    }
    
    public Boolean containsFixture(int key) {
        return this.fixturesDict.containsKey(key);
    }

}

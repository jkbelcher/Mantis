import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

/**
 * This model represents the entire Mantis puppet.
 * It contains lists of the logical lighted components on the puppet.
 * *Note this is a bit messy after the port from Peacock.  It could use some cleanup.
 */
public class MantisModel extends LXModel  implements INormalizedScope {

    public final List<MantisFixture> allMantisFixtures;   //One fixture for each controller channel.  Each body part is a fixture.
    public final List<MantisController> controllers;
    public final List<PuppetPixel> puppetPixels;
    
    public final PuppetPixelGroup allPuppetPixels;  //Needed?
    public final List<PuppetPixelGroup> allLimbs;
    public final List<PuppetPixelGroup> allSections;
       
    public MantisModel(LXModel[] allFixtures, List<MantisFixture> allMantisFixtures, List<MantisController> controllers, List<PuppetPixel> puppetPixels) {
        super(allFixtures);

        this.allMantisFixtures = allMantisFixtures;
        this.controllers = controllers;
        this.puppetPixels = puppetPixels;
        
        // Sort PuppetPixels within each collection
        Collections.sort(this.allMantisFixtures);
        for (MantisFixture fixture : this.allMantisFixtures) {
            fixture.setLoaded();
        }
        
        //Logical groups
        this.allPuppetPixels = new PuppetPixelGroup();
        this.allLimbs = new ArrayList<PuppetPixelGroup>();
        this.allSections = new ArrayList<PuppetPixelGroup>();
        
        this.initializeSubCollections();
    }
    
    private void initializeSubCollections() {
        PuppetPixelGroup claws = new PuppetPixelGroup();
        PuppetPixelGroup legs = new PuppetPixelGroup();
        PuppetPixelGroup wings = new PuppetPixelGroup();
        
        // Limbs
        for (MantisFixture f : this.allMantisFixtures) {
            PuppetPixelGroup newLimb = new PuppetPixelGroup(f);
            allLimbs.add(newLimb);
            
            PuppetPixel pp = newLimb.puppetPixels.get(0); //Check first pixel
            switch (pp.params.bodyPart) {
            case 1:
                claws.addPuppetPixels(newLimb);
                break;
            case 2: 
                legs.addPuppetPixels(newLimb);
                break;
            case 3:
                wings.addPuppetPixels(newLimb);
                break;
            }
        }
        
        // Sections
        this.allSections.add(claws);
        this.allSections.add(legs);
        this.allSections.add(wings);
        
        // All pixels
        for (PuppetPixel pp : this.puppetPixels) {
            this.allPuppetPixels.addPuppetPixel(pp);
        }        
    }
    
    protected MantisModel computeNormalsMantis() {
        this.allPuppetPixels.computeNormalized();
        for (PuppetPixelGroup ppg : this.allLimbs) {
            ppg.computeNormalized();
        }
        for (PuppetPixelGroup ppg : this.allSections) {
            ppg.computeNormalized();
        }        
        this.computeNormalized();

    	return this;
    }

    ///////////////////////////////////////////////////////
    //Static members for loading configuration from files:

    //For CSV files:
    static public final String subSeparator = ";";

    public static MantisModel LoadConfigurationFromFile(String controllerFile, String pixelFile) throws Exception
    {
        final List<MantisFixture> allMantisFixtures = new ArrayList<MantisFixture>();
        final List<MantisController> controllers = new ArrayList<MantisController>();
        final List<PuppetPixel> puppetPixels = new ArrayList<PuppetPixel>();

        //We use dictionaries to pair objects during the loading process
        final TreeMap<Integer,MantisController> controllersDict = new TreeMap<Integer,MantisController>();

        // Controllers
        List<ControllerParameters> cP = readControllersFromFile(controllerFile);
        for (ControllerParameters p : cP) {
            MantisController newController = new MantisController(p);
            controllers.add(newController);
            controllersDict.put(p.id, newController);
        }

        // Puppet Pixels
        List<PuppetPixelParameters> tpP = readPuppetPixelsFromFile(pixelFile);
        for (PuppetPixelParameters p : tpP) {
            PuppetPixel newPuppetPixel = new PuppetPixel(p);
            puppetPixels.add(newPuppetPixel);

            MantisController controller = controllersDict.get(p.controllerID);

            //Create the containing fixture if it does not exist.
            MantisFixture fixture;
            if (!controller.containsFixture(p.channel)) {
                fixture = new MantisFixture(p.channel, controller);
                controller.addFixture(fixture);
                allMantisFixtures.add(fixture);
            } else {
                fixture = controller.getFixture(p.channel);
            }

            //Add pixel to containing fixture.
            //This subsequently calls the important model loading method LXAbstractFixture.addPoint(LXPoint)
            fixture.addPuppetPixel(newPuppetPixel);
        }

        // LX wants a list of child models that as a whole contain one instance of each LXPoint.
        LXModel[] children = new LXModel[allMantisFixtures.size()];
        int i = 0;
        for (LXFixture fixture : allMantisFixtures) {
            children[i++] = new LXModel(fixture.getPoints());
        }

        return new MantisModel(children, allMantisFixtures, controllers, puppetPixels);
    }

    private static CellProcessor[] getControllerCsvProcessors() {
        return new CellProcessor[] {
            new UniqueHashCode(), // id (must be unique)
            new NotNull(), // ipAddress
            new ParseInt(), // port
        };
    }

    protected static List<ControllerParameters> readControllersFromFile(String filename) throws Exception {

        final ArrayList<ControllerParameters> results = new ArrayList<ControllerParameters>();

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getControllerCsvProcessors();

            Map<String, Object> c;
            while((c = mapReader.read(header, processors)) != null) {
                ControllerParameters p = new ControllerParameters();
                p.id = Integer.parseInt(c.get("id").toString());
                p.ipAddress = c.get("ipAddress").toString();
                p.port = Integer.parseInt(c.get("port").toString());

                results.add(p);
            }
        }
        finally {
            if(mapReader != null) {
                mapReader.close();
            }
        }

        return results;
    }

    private static CellProcessor[] getPuppetPixelCsvProcessors() {
        return new CellProcessor[] {
            new ParseInt(), // int controllerID;
            new ParseInt(), // int controllerChannel;
            new ParseInt(), // int position;
            new ParseInt(), // int bodyPart;
            new ParseInt(), // int rightLeft;
            new ParseDouble(), // float x;
            new ParseDouble(), // float y;
            new ParseDouble(), // float z;
        };
    }

    protected static List<PuppetPixelParameters> readPuppetPixelsFromFile(String filename) throws Exception {

        final ArrayList<PuppetPixelParameters> results = new ArrayList<PuppetPixelParameters>();

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new FileReader(filename), CsvPreference.STANDARD_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getPuppetPixelCsvProcessors();

            Map<String, Object> c;
            while((c = mapReader.read(header, processors)) != null) {
                PuppetPixelParameters p = new PuppetPixelParameters();

                p.controllerID = Integer.parseInt(c.get("controllerID").toString());
                p.channel = Integer.parseInt(c.get("channel").toString());
                p.position = Integer.parseInt(c.get("address").toString());
                p.bodyPart = Integer.parseInt(c.get("bodyPart").toString());
                p.rightLeft = Integer.parseInt(c.get("rightLeft").toString());
                p.x = Double.parseDouble(c.get("x").toString());
                p.y = Double.parseDouble(c.get("y").toString());
                p.z = Double.parseDouble(c.get("z").toString());

                results.add(p);
            }
        }
        finally {
            if(mapReader != null) {
                mapReader.close();
            }
        }

        return results;
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
        return 2;
    }
    
    public List<INormalizedScope> getChildScope(int index) {
        /* Sections
         * Limbs
         */
        switch (index) {
        case 0:
            return new ArrayList<INormalizedScope>(this.allLimbs);
        case 1:
            return new ArrayList<INormalizedScope>(this.allSections);
        default:
            throw new IllegalArgumentException("An invalid child scope was requested: " + this.getClass() + " " + index);                
        }
    }
    
    // end INormalizedScope
}

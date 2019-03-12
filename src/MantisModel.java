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

/**
 * This model represents the entire Mantis puppet.
 * It contains lists of the logical lighted components on the puppet.
 */
public class MantisModel extends LXModel {

    public final List<MantisFixture> allMantisFixtures;   //One fixture for each controller channel.  Each body part is a fixture.
    public final List<MantisController> controllers;
    public final List<PuppetPixel> puppetPixels;
    
    public final PuppetPixelGroup allPuppetPixels;
    public final List<PuppetPixelGroup> allLimbs;
    /*
    //Logical groupings of pixels
    //Use these maps to find specific components by ID
    public final List<PuppetPixelGroup> feathers;
    public final List<PuppetPixelGroup> panels;
    public final PuppetPixelGroup body;
    public final PuppetPixelGroup neck;
    public final PuppetPixelGroup eyes;
    
    //Normalized mappings
    //There are a bunch of different ways to group/order the spirals.
    //Use these objects to conveniently address pixels in a particular order, using a normalized 0..1 range.
    //Each group contains a list of pairs of [PuppetPixel] + [normalized position within the group]
    public final PuppetPixelGroup feathersLR;
    public final PuppetPixelGroup panelsLR;
    public final PuppetPixelGroup spiralsCW_IO;
    public final PuppetPixelGroup spiralsCCW_IO;  
    */
       
    public MantisModel(LXModel[] allFixtures, List<MantisFixture> allMantisFixtures, List<MantisController> controllers, List<PuppetPixel> puppetPixels) {
        super(allFixtures);

        this.allMantisFixtures = allMantisFixtures;
        this.controllers = controllers;
        this.puppetPixels = puppetPixels;
        
        //Sort PuppetPixels within each collection
        Collections.sort(this.allMantisFixtures);
        for (MantisFixture fixture : this.allMantisFixtures) {
            fixture.setLoaded();
        }
        
        /*
        //Logical groups
        this.feathers = new ArrayList<PuppetPixelGroup>();
        this.panels = new ArrayList<PuppetPixelGroup>();
        this.body = new PuppetPixelGroup();
        this.neck = new PuppetPixelGroup();
        this.eyes = new PuppetPixelGroup();
        
        //Normalized mappings
        this.feathersLR = new PuppetPixelGroup();
        this.panelsLR = new PuppetPixelGroup();
        this.spiralsCW_IO = new PuppetPixelGroup();
        this.spiralsCCW_IO = new PuppetPixelGroup();
        */
        this.allPuppetPixels = new PuppetPixelGroup();
        this.allLimbs = new ArrayList<PuppetPixelGroup>();
        
        this.initializeSubCollections();
    }
    
    private void initializeSubCollections() {
        /*
        //Feathers
        for (int i = 1; i <= 13; i++) {
            this.feathers.add(new PuppetPixelGroup(i));            
        }
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isFeatherPixel()) {
                this.feathers.get(p.feather-1).addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //Panels
        for (int i = 1; i <= 12; i++) {
            this.panels.add(new PuppetPixelGroup(i));            
        }
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isPanelPixel()) {
                this.panels.get(p.panel-1).addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //Body
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isBodyPixel()) {
                this.body.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //Neck
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isNeckPixel()) {
                this.neck.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //Eyes
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isEyePixel()) {
                this.eyes.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
                
    	//FeathersLR
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isFeatherPixel()) {                
                this.feathersLR.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //PanelsLR
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isPanelPixel()) {                
                this.panelsLR.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //SpiralsCW_IO = Spirals, Clockwise, Inside->Outside
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isPanelPixel() && p.params.spiral % 2 == 0) {                
                this.spiralsCW_IO.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }
        
        //SpiralsCCW_IO = Spirals, Counter-Clockwise, Inside->Outside
        for (PuppetPixel p : this.puppetPixels) {
            if (p.isPanelPixel() && p.params.spiral % 2 != 0) {                
                this.spiralsCCW_IO.addPuppetPixelPosition(new PuppetPixelPos(p));
            }
        }       
        */

    }
    
    protected MantisModel computeNormalsMantis() {
        //Positions are computed here, after the model is built and calculateNormals() has been called on it.
        //This is in case a collection wants to sort itself using a normalized value.
/*
        //Feathers
        for (PuppetPixelGroup g : this.feathers) {
            g.puppetPixels.sort((p1,p2) -> Float.compare(p1.getPoint().r, p2.getPoint().r));
            g.copyIndicesToChildren().calculateNormalsByIndex();
        }
        
        //Panels
        for (PuppetPixelGroup g : this.panels) {
            g.puppetPixels.sort((p1,p2) -> Float.compare(p1.getPoint().r, p2.getPoint().r));
            g.copyIndicesToChildren().calculateNormalsByIndex();
        }

        //Body
        this.body.puppetPixels.sort((p1,p2) -> p2.getPosition() - p1.getPosition());
        this.body.copyIndicesToChildren().calculateNormalsByIndex();
                
        //Neck
        this.neck.puppetPixels.sort((p1,p2) -> p2.getPosition() - p1.getPosition());
        this.neck.copyIndicesToChildren().calculateNormalsByIndex();
        
        //Eyes
        this.eyes.puppetPixels.sort((p1,p2) -> p2.getPosition() - p1.getPosition());
        this.eyes.copyIndicesToChildren().calculateNormalsByIndex();
        
        this.feathersLR.puppetPixels.sort((p1,p2) -> p1.getFeather() == p2.getFeather() ? p1.getPosition() - p2.getPosition() : p1.getFeather() - p2.getFeather());
        this.feathersLR.copyIndicesToChildren().calculateNormalsByIndex();

        this.panelsLR.puppetPixels.sort((p1,p2) -> p1.getPanel() == p2.getPanel() ? Float.compare(p1.getPoint().r, p2.getPoint().r) : p1.getPanel() - p2.getPanel());
        this.panelsLR.copyIndicesToChildren().calculateNormalsByIndex();
        
        this.spiralsCW_IO.puppetPixels.sort((p1,p2) -> p1.getSpiral() == p2.getSpiral() ? p2.getPosition() - p1.getPosition() : p2.getSpiral() - p1.getSpiral());
        this.spiralsCW_IO.copyIndicesToChildren().calculateNormalsByIndex();    //*Could do normals by position and not by spiral.

        this.spiralsCCW_IO.puppetPixels.sort((p1,p2) -> p1.getSpiral() == p2.getSpiral() ? p2.getPosition() - p1.getPosition() : p2.getSpiral() - p1.getSpiral());
        this.spiralsCCW_IO.copyIndicesToChildren().calculateNormalsByIndex();
*/
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

}

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.parameter.BooleanParameter.Mode;
import processing.core.PApplet;

public class DemoChannelPattern extends MantisPattern {

    public final DiscreteParameter channel = 
        new DiscreteParameter("Channel", 1, 1, 32)
        .setDescription("Channel to light");
      
    public final DiscreteParameter index = 
            new DiscreteParameter("Index", 0, 0, 74)
            .setDescription("Index");

    public final BooleanParameter identify = 
            new BooleanParameter("Identify")
            .setDescription("Identify the current values")
            .setMode(Mode.MOMENTARY);    

    public DemoChannelPattern(LX lx) {
        super(lx);
        
        addParameter(channel);
        addParameter(index);
        addParameter(identify);        
    }
    
    @Override
    protected void run(double arg0) {
        this.clearColors();

        //Get the current value of the parameter
        int channel = this.channel.getValuei();
        int index = this.index.getValuei();
        
        //Hacky solution to help us identify configuration issues
        if (this.identify.getValueb()) {
            PApplet.println("Channel: " + channel + "  Index: " + index);
        }

        for (MantisFixture fixture : model.allMantisFixtures) {
            if (fixture.channel == channel)
            {
                for (PuppetPixel tp : fixture.puppetPixels) {
                        colors[tp.p.index] = LXColor.RED;
                }
                
                if (fixture.puppetPixels.size() > index) {
                    colors[fixture.puppetPixels.get(index).p.index] = LXColor.BLUE;
                }
            }
        }
    }
    
}

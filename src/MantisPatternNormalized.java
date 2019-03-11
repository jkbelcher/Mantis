import heronarts.lx.LX;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.LXParameterListener;
import heronarts.lx.parameter.BooleanParameter.Mode;

public abstract class MantisPatternNormalized extends MantisPattern {

    public final MantisModelNormalized modelN;
    
    public final BooleanParameter randGroup = 
            new BooleanParameter("RandGroup", true)
            .setDescription("When ENABLED, the target group will be included in the randomization.")
            .setMode(Mode.TOGGLE);
    
    public final BooleanParameter nextGroup = 
            new BooleanParameter("NextGroup")
            .setDescription("Change the pattern to target the next PuppetPixelGroup")
            .setMode(Mode.MOMENTARY);

    public MantisPatternNormalized(LX lx) {
        super(lx);
        
        this.modelN = new MantisModelNormalized(model);
        
        addParameter(randGroup);
        addParameter(nextGroup);
        this.nextGroup.addListener(new LXParameterListener() {
            public void onParameterChanged(LXParameter p) {
                if (((BooleanParameter)p).getValueb()) {
                    goNextGroup();
                }
            }
            });
    }

    public MantisPatternNormalized(LX lx, PuppetPixelGroup[] groups) {
        super(lx);
        
        this.modelN = new MantisModelNormalized(model, groups);

        addParameter(randGroup);
        addParameter(nextGroup);
        this.nextGroup.addListener(new LXParameterListener() {
            public void onParameterChanged(LXParameter p) {
                if (((BooleanParameter)p).getValueb()) {
                    goNextGroup();
                }
            }
            });
    }
    
    public MantisPatternNormalized(LX lx, MantisModelNormalized modelN) {
        super(lx);
        
        this.modelN = modelN;
    
        addParameter(randGroup);
        addParameter(nextGroup);
        this.nextGroup.addListener(new LXParameterListener() {
            public void onParameterChanged(LXParameter p) {
                if (((BooleanParameter)p).getValueb()) {
                    goNextGroup();
                }
            }
            });
    }    
    
    public void goNextGroup() {
        this.modelN.goNext();
    }

    public void randomizeTargetGroup() {
        if (this.randGroup.getValueb()) {
            this.modelN.goRandom();
        }
    }

}

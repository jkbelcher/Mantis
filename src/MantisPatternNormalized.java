import java.util.ArrayList;
import java.util.List;

import heronarts.lx.LX;
import heronarts.lx.parameter.EnumParameter;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.LXParameterListener;

public abstract class MantisPatternNormalized extends MantisPattern {

    List<INormalizedScope> scopes;
    List<INormalizedScope> entireScope;
    
    public final EnumParameter<MantisScope> targetScope =
            new EnumParameter<MantisScope>("Scope", MantisScope.ENTIRE)
            .setDescription("Target scope for normalized pattern");
    
    public enum MantisScope {
        ENTIRE,
        SECTIONS,
        LIMBS
      };

    /*
    public final BooleanParameter randScope = 
            new BooleanParameter("RandScope", true)
            .setDescription("When ENABLED, the target scope will be included in the randomization.")
            .setMode(Mode.TOGGLE);
    */

    public MantisPatternNormalized(LX lx) {
        super(lx);
                
        this.entireScope = new ArrayList<INormalizedScope>();
        this.entireScope.add(this.model);
        
        addParameter(targetScope);
        //addParameter(randScope);

        this.targetScope.addListener(new LXParameterListener() {
            public void onParameterChanged(LXParameter p) {
                updateTargetScope();
            }
            });
        
        updateTargetScope();
    }    
    
    void updateTargetScope() {
        switch (this.targetScope.getEnum()) {
            case SECTIONS:
                this.scopes = this.model.getChildScope(1);
                break;
            case LIMBS:
                this.scopes = this.model.getChildScope(0);
                break;
            case ENTIRE:
                default:
                    this.scopes = this.entireScope;
                    break;                    
        }
    }

}

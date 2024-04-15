package fundamentals.mechanic;

/**
 * A variation of MechanicBase, InstantMechanics are mechanics that when instantiated, require a lambda function
 * that will describe the mechanic's behavior. 
 * 
 * @see 
 * Moreover, the lambda function will be called once at the event that the InstantMechanic
 * is scheduled; this mechanic does not offer initializing, executing, or ending phases, and does not offer the use of an ending condition!
 * The lambda function is the entire mechanic. 
 */
public class InstantMechanic extends MechanicBase 
{
    private MechanicBehavior behavior = null;

    @FunctionalInterface
    public interface MechanicBehavior {
        public void behavior();  
    }

    /**
     * A variation of MechanicBase, InstantMechanics are mechanics that when instantiated, require a lambda function
     * that will describe the mechanic's behavior. 
     * 
     * @see The lambda function will be called once at the event that the InstantMechanic
     * is scheduled; this mechanic does not offer initializing, executing, or ending phases, and does not offer the use of an ending condition!
     * The lambda function is the entire mechanic. 
     */
    public InstantMechanic(MechanicBehavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public void initialize() {
        behavior.behavior();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
package fundamentals.mechanic;

import java.util.function.Function;

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
    private Function<Void, Void> mechanic_def = null;

    /**
     * A variation of MechanicBase, InstantMechanics are mechanics that when instantiated, require a lambda function
     * that will describe the mechanic's behavior. 
     * 
     * @see The lambda function will be called once at the event that the InstantMechanic
     * is scheduled; this mechanic does not offer initializing, executing, or ending phases, and does not offer the use of an ending condition!
     * The lambda function is the entire mechanic. 
     */
    public InstantMechanic(Function<Void, Void> mechanic_def) {
        this.mechanic_def = mechanic_def;
    }

    @Override
    public void initialize() {
        mechanic_def.apply(null);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
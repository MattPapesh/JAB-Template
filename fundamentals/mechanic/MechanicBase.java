package fundamentals.mechanic;

import java.util.LinkedList;
import fundamentals.component.ComponentBase;

/**
 * The superclass to every app mechanic. Fundamentally, MechanicBase is broken up into four crucial phases: initialization before
 * before running a mechanic after being scheduled, execution of the mechanic itself, the condition that needs to be met to ensure that
 * the mechanic is finished running, and the ending. Moreover, MechanicBase also allows mechanic classes to inherit and override methods for 
 * controlling a mechanic's behavior. Finally, it is required of every mechanic to extend MechanicBase as a superclass while 
 * calling the addRequirements(GenericComponent... components) method as long as if that mechanic isn't already 
 * extending another Mechanic superclass for appropriate functionality.
 * 
 * @see Mechanic Superclasses: MechanicBase, InstantMechanic, and SequentialMechanicGroup.
 */
public class MechanicBase implements MechanicInterface
{
    private final double MECHANIC_ID = Math.random();
    private LinkedList<ComponentBase> components = new LinkedList<ComponentBase>();
    public boolean is_scheduled = false; 
    public boolean is_initialized = false;
    public int initial_periodic_millis = 0;
    private int executional_periodic_delay_millis = 0; 

    @Override public void initialize() {}
    @Override public void execute() {}
    @Override public boolean isFinished() {return false;}
    @Override public void end(boolean interrupted) {}

    /**
     * Once MechanicBase has been extended and become a superclass to a subclass, the subclass must call 
     * this method in order for the subclass to appropriately function as a app mechanic. Moreover, any components 
     * that will be used by the mechanic must be passed in.
     */
    public void addRequirements(ComponentBase... components) {
        if(components.length != 0 && components[0].getClass().getSuperclass().getName() == ComponentBase.class.getName()) {
            for(var comp : components) {
                this.components.addLast(comp);
            }
        }
    }

    /**
     * Once MechanicBase has been extended and become a superclass to a subclass, the subclass must call 
     * this method in order for the subclass to appropriately function as a app mechanic. Moreover, any components 
     * that will be used by the mechanic must be passed in.
     */
    public void addRequirements(LinkedList<ComponentBase> components) {
        if(!components.isEmpty() && components.getFirst().getClass().getSuperclass().getName() == ComponentBase.class.getName()) {
            for(var comp : components) {
                this.components.addLast(comp);
            }
        }
    }

    public LinkedList<ComponentBase> getRequirements() {
        return new LinkedList<ComponentBase>(components);
    }

    /**
     * @return The mechanic's personal ID; a specific and unique value that is assigned upon instantiation.
     */
    public double getMechanicID() {
        return MECHANIC_ID;
    }

    /**
     * The execute() method is continuously called once a mechanic is scheduled, and so this method 
     * is used to determine how often the execute() method should be called. 
     * 
     * @param millis (int) : The unsigned amount of periodic delay in milliseconds.
     */
    public void setExecutionalPeriodicDelay(int millis) {
        executional_periodic_delay_millis = Math.max(millis, 0);
    }

    /**
     * @return How often the execute() method is called  in milliseconds when it's 
     * continuously being called once a mechanic is scheduled. 
     */
    public int getExecutionalPeriodicDelay() {
        return executional_periodic_delay_millis;
    }
}
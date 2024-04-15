package fundamentals.mechanic;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

import fundamentals.appbase.AppBase;
import fundamentals.component.ComponentBase;

/**
 * Manages all mechanic variables through means of static methods. All types of mechanics inherit MechanicBase methods, regardless 
 * of the type of mechanic. Moreover, each mechanic scheduled when calling a mechanic's superclass method, schedule(), will 
 * "register" the mechanic with the MechanicScheduler either until the mechanic is interrupted, or has its ending condition met 
 * and the mechanic naturally ends. Lastly, all registered mechanics will be ran by the MechanicScheduler until the mechanic somehow ends,
 * allowing it to be de-registered. 
 */
public class MechanicScheduler 
{
    private Set<MechanicBase> mechanics = new HashSet<MechanicBase>();
    private Set<ComponentBase> components = new HashSet<ComponentBase>();
    private Vector<Function<Void, Void>> events = new Vector<Function<Void, Void>>();
    private static MechanicScheduler scheduler = new MechanicScheduler();

    /**
     * Schedules the specified mechanic. 
     * @param mechanic (MechanicBase) : The specified mechanic. 
     */
    public void scheduleMechanic(MechanicBase mechanic) {
        // Cancel all mechanics requiring the same components as the specified mechanic. 
        Set<ComponentBase> required_components = mechanic.getRequiredComponents();
        if(!required_components.isEmpty()) {
            // Collect all mechanics that share requirements. 
            Set<MechanicBase> canceled_mechanics = new HashSet<MechanicBase>();
            // Iterate through other scheduled mechanics:
            for(var other_mechanic : mechanics) {
                // Collect required components of other mechanic.
                Set<ComponentBase> other_required_components = other_mechanic.getRequiredComponents();
                // Iterate through other components to compare to required components:
                for(var other_component : other_required_components) {
                    for(var required_component : required_components) {
                        if(other_component == required_component) {
                            // Add canceled command for overlapping required components.
                            canceled_mechanics.add(other_mechanic);
                        }
                    }
                }
            }

            for(var canceled_mechanic : canceled_mechanics) {
                cancelMechanic(canceled_mechanic);
            }

            mechanics.add(mechanic);
        }
    }

    /**
     * Cancels the specified mechanic.
     * @param mechanic (MechanicBase) : The specified mechanic.
     */
    public void cancelMechanic(MechanicBase mechanic) {
        mechanic.end(true);
        mechanics.remove(mechanic);
    }

    public void cancelAllMechanics() {
        for(var mechanic : mechanics) {
            mechanic.end(true);
            mechanics.remove(mechanic);
        }
    }

    /**
     * This method is periodically called based on the application's refresh rate! This must happen!
     */
    public void runMechanics() {
        for(var mech : mechanics) {
            // Define mechanic lifetime policy:
            if(!mech.is_initialized) {
                mech.initialize();
                mech.is_initialized = true;
            }
            else if(mech.isFinished()) {
                mech.end(false);
                mechanics.remove(mech);
            }
            else if(AppBase.getMillis() - mech.initial_periodic_millis >= mech.getExecutionalPeriodicDelay()){
                mech.initial_periodic_millis = AppBase.getMillis();
                mech.execute();
            }
        }
    }

    public void registerEvent(Function<Void, Void> event_def) {
        events.add(event_def);
    }

    public void runEvents() {
        for(var event : events) {
            event.apply(null);
        }
    }

    public void registerComponent(ComponentBase component) {
        components.add(component);
    }

    public void runComponentPeriodics() {
        for(var component : components) {
            component.periodic();
        }
    }

    public static MechanicScheduler getInstance() {
        return scheduler;
    }
}
package fundamentals.mechanic;

import java.util.LinkedList;
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
    private LinkedList<MechanicBase> mechanics = new LinkedList<MechanicBase>();
    private LinkedList<ComponentBase> components = new LinkedList<ComponentBase>();
    private LinkedList<Function<Void, Void>> events = new LinkedList<Function<Void, Void>>();
    private static MechanicScheduler scheduler = new MechanicScheduler();

    /**
     * Schedules the specified mechanic. 
     * @param mechanic (MechanicBase) : The specified mechanic. 
     */
    public void scheduleMechanic(MechanicBase mechanic) {
        // Cancel all mechanics requiring the same components as the specified mechanic. 
        LinkedList<ComponentBase> required_components = mechanic.getRequiredComponents();
        if(!required_components.isEmpty()) {
            // Collect all mechanics that share requirements. 
            LinkedList<MechanicBase> canceled_mechanics = new LinkedList<MechanicBase>();
            // Iterate through other scheduled mechanics:
            for(var other_mechanic : mechanics) {
                // Collect required components of other mechanic.
                LinkedList<ComponentBase> other_required_components = other_mechanic.getRequiredComponents();
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

            mechanic.is_scheduled = true;
            mechanics.add(mechanic);
        }
    }

    /**
     * Cancels the specified mechanic.
     * @param mechanic (MechanicBase) : The specified mechanic.
     */
    public void cancelMechanic(MechanicBase mechanic) {
        if(mechanics.contains(mechanic)) {
            if(mechanic.is_initialized) {
                mechanic.end(true);
                mechanic.is_initialized = false;
            }

            mechanic.is_scheduled = false; 
            mechanics.remove(mechanic);
        }
    }

    public void cancelAllMechanics() {
        for(int i = 0; i < mechanics.size(); i++) {
            mechanics.get(i).end(true);
            mechanics.remove(i);
        }
    }

    /**
     * This method is periodically called based on the application's refresh rate! This must happen!
     */
    public void runMechanics() {
        for(int i = 0; i < mechanics.size(); i++) {
            // Define mechanic lifetime policy:
            if(!mechanics.get(i).is_initialized) {
                //System.out.println("init");
                mechanics.get(i).initialize();
                mechanics.get(i).is_initialized = true;
            }
            else if(mechanics.get(i).isFinished()) {
                //System.out.println("done");
                mechanics.get(i).end(false);
                mechanics.get(i).is_initialized = false; 
                mechanics.get(i).is_scheduled = false;
                mechanics.remove(i);
                i--; 
            }
            else if(AppBase.getMillis() - mechanics.get(i).initial_periodic_millis >= mechanics.get(i).getExecutionalPeriodicDelay()){
                //System.out.println("exe");
                mechanics.get(i).initial_periodic_millis = AppBase.getMillis();
                mechanics.get(i).execute();
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
package fundamentals.mechanic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import fundamentals.appbase.AppBase;

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
    private static MechanicScheduler scheduler = new MechanicScheduler();

    public static MechanicScheduler getInstance() {
        return scheduler;
    }

    /**
     * Schedules the specified mechanic. 
     * @param mechanic (MechanicBase) : The specified mechanic. 
     */
    public void scheduleMechanic(MechanicBase mechanic) {
        if(mechanic != null) {
            mechanics.add(mechanic);
        }
    }

    /**
     * Cancels the specified mechanic.
     * @param mechanic (MechanicBase) : The specified mechanic.
     */
    public void cancelMechanic(MechanicBase mechanic) {
        for(var mech : mechanics) {
            if(mech.getMechanicID() == mechanic.getMechanicID()) {
                mech.end(true);
                mechanics.remove(mech);
            }
        }
    }

    /**
     * This method is periodically called based on the application's refresh rate! This must happen!
     */
    public void run() {
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
}
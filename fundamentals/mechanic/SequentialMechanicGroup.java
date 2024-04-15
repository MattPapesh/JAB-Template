package fundamentals.mechanic;

import java.util.LinkedList;

import fundamentals.appbase.AppBase;

/**
 * A variation of MechanicBase, SequentialMechanicGroup mechanics are used to add or append other mechanics to it in a consecutive
 * fashion in order to schedule and run a sequence of mechanics in a orderly fashion. 
 * 
 * @see 
 * Moreover, this mechanic is used to make use of its addMechanics(GenericMechanic... mechanics) method. this mechanic does not offer 
 * initializing, executing, or ending phases, and does not offer the use of an ending condition! This mechanic is strictly meant for
 * sequentially scheduling and running added mechanics. 
 */
public class SequentialMechanicGroup extends MechanicBase
{
    private LinkedList<MechanicBase> mechanics = new LinkedList<MechanicBase>();
    private int current_index = 0;

    /**
     * A variation of MechanicBase, SequentialMechanicGroup mechanics are used to add or append other mechanics to it in a consecutive
     * fashion in order to schedule and run a sequence of mechanics in a orderly fashion. 
     * 
     * @see 
     * Moreover, this mechanic is used to make use of its addMechanics(GenericMechanic... mechanics) method. this mechanic does not offer 
     * initializing, executing, or ending phases, and does not offer the use of an ending condition! This mechanic is strictly meant for
     * sequentially scheduling and running added mechanics. 
     */
    public SequentialMechanicGroup() {
        setExecutionalPeriodicDelay(1);
    }

    /**
     * Used to add any type of mechanic instance to the SequentialMechanicGroup, where these mechanics
     * will be consecutively ran upon scheduling the SequentialMechanicGroup itself. Moreover, the order at which mechanics
     * are passed in as parameters determines what order that the mechanics will be consecutively scheduled and ran; 
     * the first argument will be the first mechanic scheduled, and the final argument passed in will be the last to be scheduled.
     * 
     * @see Given that added mechanics are ran one after the other, an added mechanic will only be scheduled and ran once the previous
     * added mechanic has either been interrupted or ended from meeting its ending condition. 
     *
     * @param mechanics (MechanicBase...) : The specified variable-argument mechanics to add. 
     */
    public void addMechanics(MechanicBase... mechanics) {
        for(var mech : mechanics) {
            this.mechanics.addLast(mech);
            addRequirements(mech.getRequiredComponents());
        }
    }
    
    @Override
    public void execute() {
        if(current_index < 0 || current_index >= mechanics.size()) {
            return;
        }

        if(!mechanics.get(current_index).is_initialized) {
            mechanics.get(current_index).initialize();
        }
        else if(!mechanics.get(current_index).isFinished()) {
            mechanics.get(current_index).end(false);
            current_index++;
        }
        else if(AppBase.getMillis() - mechanics.get(current_index).initial_periodic_millis >= mechanics.get(current_index).getExecutionalPeriodicDelay()) {
            mechanics.get(current_index).initial_periodic_millis = AppBase.getMillis();
            mechanics.get(current_index).execute();
        }
    }

    @Override
    public void end(boolean interrupted) {
        if(interrupted) {
            for(var mech : mechanics) {
                mech.end(true);
                mechanics.remove(mech);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return current_index >= mechanics.size();
    }
}
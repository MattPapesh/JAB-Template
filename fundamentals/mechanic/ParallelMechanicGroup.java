package fundamentals.mechanic;

import java.util.LinkedList;

import fundamentals.appbase.AppBase;

public class ParallelMechanicGroup extends MechanicBase
{
    private LinkedList<MechanicBase> mechanics = new LinkedList<MechanicBase>(); 
    
    public ParallelMechanicGroup() {
        setExecutionalPeriodicDelay(1);
    }

    public void addMechanics(MechanicBase... mechanics) {
        for(var mech : mechanics) {
            this.mechanics.addLast(mech);
            addRequirements(mech.getRequiredComponents());
        }
    }

    @Override
    public void execute() {
        for(int i = 0; i < mechanics.size(); i++) {
            if(!mechanics.get(i).is_initialized) {
                mechanics.get(i).is_scheduled = true;
                mechanics.get(i).initialize();
                mechanics.get(i).is_initialized = true;
            }
            else if(mechanics.get(i).isFinished()) {
                mechanics.get(i).end(false);
                mechanics.get(i).is_initialized = false;
                mechanics.get(i).is_scheduled = false;
                mechanics.remove(i);
                i--;
            }
            else if(AppBase.getMillis() - mechanics.get(i).initial_periodic_millis >= mechanics.get(i).getExecutionalPeriodicDelay()) {
                mechanics.get(i).initial_periodic_millis = AppBase.getMillis();
                mechanics.get(i).execute();
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        if(interrupted) {
            for(int i = 0; i < mechanics.size(); i++) {
                if(mechanics.get(i).is_initialized) {
                    mechanics.get(i).end(true);
                    mechanics.get(i).is_initialized = false; 
                }

                mechanics.get(i).is_scheduled = false;
                mechanics.remove(i);
                i--;
            }
        }
    }

    @Override
    public boolean isFinished() {
        for(var mech : mechanics) {
            if(!mech.isFinished())  {
                return false; 
            }
        }

        return true;
    }
}
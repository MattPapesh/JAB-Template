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
        for(var mech : mechanics) {
            if(!mech.is_initialized) {
                mech.initialize();
                mech.is_initialized = true;
            }
            else if(mech.isFinished()) {
                mech.end(false);
                mechanics.remove(mech);
            }
            else if(AppBase.getMillis() - mech.initial_periodic_millis >= mech.getExecutionalPeriodicDelay()) {
                mech.initial_periodic_millis = AppBase.getMillis();
                mech.execute();
            }
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
        for(var mech : mechanics) {
            if(!mech.isFinished())  {
                return false; 
            }
        }

        return true;
    }
}

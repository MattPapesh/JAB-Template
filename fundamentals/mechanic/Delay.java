package fundamentals.mechanic;

import fundamentals.appbase.AppBase;

/**
 * A mechanic that when scheduled, runs for a period of milliseconds while periodically and consecutively scheduling another mechanic
 * on a loop during the idle period. 
 * 
 * @see Note: This mechanic is most useful when used within a SequentialMechanicGroup, causing delay between other sequentially scheduled
 * mechanics used within the group. 
 */
public class Delay extends MechanicBase
{
    private double millis = 0;
    private double initial_millis = 0;
    private InstantMechanic idle_mechanic = null;

    /**
     * A mechanic that when scheduled, runs for a period of milliseconds while periodically and consecutively scheduling another mechanic
     * on a loop during the idle period. 
     * 
     * @param millis (int) The unsigned amount of delay in milliseconds.
     * @param idle_mechanic (InstantMechanic) : The specified mechanic to schedule during the idle period. 
     * 
     * @see Note: This mechanic is most useful when used within a SequentialMechanicGroup, causing delay between other sequentially scheduled
     * mechanics used within the group. 
     */
    public Delay(int millis, InstantMechanic idle_mechanic) {
        this.millis = Math.max(millis, 0);
        this.idle_mechanic = idle_mechanic;
        addRequirements();
        setExecutionalPeriodicDelay(1);
    }   

    @Override
    public void initialize() {
        initial_millis = AppBase.getMillis();
        idle_mechanic.initialize();
    }

    @Override
    public void execute() {
        idle_mechanic.execute();
    }

    @Override
    public void end(boolean interrupted) {
        idle_mechanic.end(interrupted);
    }

    @Override
    public boolean isFinished() {   
        return AppBase.getMillis() - initial_millis >= millis;
    }
}
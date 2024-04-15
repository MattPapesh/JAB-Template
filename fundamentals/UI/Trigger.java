package fundamentals.UI;

import java.util.function.Function;

import fundamentals.mechanic.MechanicBase;
import fundamentals.mechanic.MechanicScheduler;

public class Trigger 
{   
    private Function<Void, Boolean> condition_def = null; 

    public Trigger(Function<Void, Boolean> condition_def) {
        this.condition_def = condition_def;
    }  

    public boolean evaluateCondition() {
        return condition_def.apply(null);
    }

    public Trigger onTrue(MechanicBase mechanic) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(!mechanic.is_initialized && evaluateCondition()) {
                MechanicScheduler.getInstance().scheduleMechanic(mechanic);
            }

            return Void;
        });

        return this;
    }

    public Trigger onFalse(MechanicBase mechanic) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(!mechanic.is_initialized && !evaluateCondition()) {
                MechanicScheduler.getInstance().scheduleMechanic(mechanic);
            }

            return Void;
        });

        return this;
    }

    public Trigger whileTrue(MechanicBase mechanic) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(!mechanic.is_initialized && evaluateCondition()) {
                MechanicScheduler.getInstance().scheduleMechanic(mechanic);
            }
            else if(mechanic.is_initialized && !evaluateCondition() && !mechanic.isFinished()) {
                mechanic.end(true);
            }

            return Void;
        });

        return this;
    }

    public Trigger whileFalse(MechanicBase mechanic) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(!mechanic.is_initialized && !evaluateCondition()) {
                MechanicScheduler.getInstance().scheduleMechanic(mechanic);
            }
            else if(mechanic.is_initialized && evaluateCondition() && !mechanic.isFinished()) {
                mechanic.end(true);
            }

            return Void;
        });

        return this;
    }

    public Trigger andOther(Trigger trigger) {
        return new Trigger((Void) -> {
            return evaluateCondition() && trigger.evaluateCondition();
        });
    }

    public Trigger orOther(Trigger trigger) {
        return new Trigger((Void) -> {
            return evaluateCondition() || trigger.evaluateCondition();
        });
    }

    public Trigger negate() {
        return new Trigger((Void) -> {
            return !evaluateCondition();
        });
    }
}

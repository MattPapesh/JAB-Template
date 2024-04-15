package fundamentals.UI;

import java.util.function.Function;

import fundamentals.mechanic.MechanicBase;
import fundamentals.mechanic.MechanicScheduler;

public class Trigger 
{   
    private Function<Void, Boolean> condition_def = null; 
    private MechanicBase on_true_mechanic = null; 
    private MechanicBase on_false_mechanic = null;
    private MechanicBase while_true_mechanic = null; 
    private MechanicBase while_false_mechanic = null;

    public Trigger(Function<Void, Boolean> condition_def) {
        this.condition_def = condition_def;
    }  

    public boolean evaluateCondition() {
        return condition_def.apply(null);
    }

    public Trigger onTrue(Function<Void, MechanicBase> event) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(evaluateCondition()) {
                // Get potential mechanic:
                MechanicBase mechanic = event.apply(null);
                // If previous existing mechanic ended then reset mechanic.
                if(on_true_mechanic != null && !on_true_mechanic.is_scheduled) {
                    on_true_mechanic = null;
                }
                // Assign new mechanic and schedule:
                if(mechanic != null && on_true_mechanic == null) {
                    on_true_mechanic = mechanic;
                    MechanicScheduler.getInstance().scheduleMechanic(on_true_mechanic);
                }
            }

            return Void;
        });

        return this;
    }

    public Trigger onFalse(Function<Void, MechanicBase> event) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(!evaluateCondition()) {
                // Get potential mechanic:
                MechanicBase mechanic = event.apply(null);
                // If previous existing mechanic ended then reset mechanic.
                if(on_false_mechanic != null && !on_false_mechanic.is_scheduled) {
                    on_false_mechanic = null;
                }
                // Assign new mechanic and schedule:
                if(mechanic != null && on_false_mechanic == null) {
                    on_false_mechanic = mechanic;
                    MechanicScheduler.getInstance().scheduleMechanic(on_false_mechanic);
                }
            }

            return Void;
        });

        return this;
    }

    public Trigger whileTrue(Function<Void, MechanicBase> event) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(evaluateCondition()) {
                // Get potential mechanic:
                MechanicBase mechanic = event.apply(null);
                // If previous existing mechanic ended then reset mechanic.
                if(while_true_mechanic != null && !while_true_mechanic.is_scheduled) {
                    while_true_mechanic = null;
                }
                // Assign new mechanic and schedule:
                if(mechanic != null && while_true_mechanic == null) {
                    while_true_mechanic = mechanic;
                    MechanicScheduler.getInstance().scheduleMechanic(while_true_mechanic);
                }
            }
            else if(while_true_mechanic != null){
                MechanicScheduler.getInstance().cancelMechanic(while_true_mechanic);
                while_true_mechanic = null;
            }

            return Void;
        });

        return this;
    }

    public Trigger whileFalse(Function<Void, MechanicBase> event) {
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(!evaluateCondition()) {
                // Get potential mechanic:
                MechanicBase mechanic = event.apply(null);
                // If previous existing mechanic ended then reset mechanic.
                if(while_false_mechanic != null && !while_false_mechanic.is_scheduled) {
                    while_false_mechanic = null;
                }
                // Assign new mechanic and schedule:
                if(mechanic != null && while_false_mechanic == null) {
                    while_false_mechanic = mechanic;
                    MechanicScheduler.getInstance().scheduleMechanic(while_false_mechanic);
                }
            }
            else if(while_false_mechanic != null){
                MechanicScheduler.getInstance().cancelMechanic(while_false_mechanic);
                while_false_mechanic = null;
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

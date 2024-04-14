package fundamentals.UI;

import java.util.LinkedList;

/**
 * Manages all Controller and Button variables through means of static methods. All instantiated Controllers that have been 
 * "registered" may have Button instances. Schedulers are used to periodically return different Controller instances to continuously 
 * call their Buttons' run() methods, allowing Controllers to operate appropriately. Moreover, allowing these run() methods 
 * to be accessed in AppBase.java to be looped for Controller functionality. 
 */
public class ControllerScheduler 
{
    private LinkedList<Controller> controllers = new LinkedList<Controller>();
    private static ControllerScheduler scheduler = new ControllerScheduler();

    /**
     * Given that a Controller's Buttons' run() method must be continuously called in order to function, the GUIScheduler needs to be able to
     * have access to every Controller instance. All instances must be passed in for them to function appropriately. 
     * 
     * @param controller (Controller) : The specified instance to register. 
     */
    public void registerController(Controller controller) {
        if(controller != null) {
            controllers.addLast(controller);
        }
    }

    /**
     * Removes all instances of the Controller instance passed in from the ControllerScheduler's registered list 
     * of Controller instances. This may want to be done if a Controller instance will no longer be used.
     * 
     * @param controller (Controller) : The specified registered instance to remove.
     */
    public void removeController(Controller controller) {
        for(var cont : controllers) {
            if(cont.getControllerID() == controller.getControllerID()) {
                controllers.remove(cont);
            }
        }
    }

    public void run() {
        for(var controller : controllers) {
            controller.run();
        }
    }

    public static ControllerScheduler getInstance() {
        return scheduler;
    }
}
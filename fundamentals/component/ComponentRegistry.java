package fundamentals.component;

import java.util.LinkedList;

/**
 * Manages all Component variables through means of static methods. All instantiated Components that have been 
 * "registered" by calling a component's superclass method, addRequirements(...),
 * will be accessible from the ComponentRegistry for GraphicsRenderer in order to display every component on-screen. 
 * Moreover, GraphicsRenderer will always have access to all "registered" Component instances so that any changes that 
 * occur to any instance, such as updated coordinates or image, will immediately be reflected
 * on-screen when components are toggled to be active. 
 */
public class ComponentRegistry 
{
    private static LinkedList<ComponentBase> components = new LinkedList<ComponentBase>();
   
    /**
    * Allows the Component instance passed in to be accessible by the ComponentRegistry so that GraphicsRenderer can display all
    * registered Components on-screen. 
    *
    * @see This method is called by a Component's superclass method, addRequirements(...). Therefore requiring
    * addRequirements(...) to be called by every Component to function appropriately. 
    */
    protected static void registerComponent(ComponentBase component) {
        for(var comp : components) {
            if(component != null && comp == component) {
                return;   
            }
        }

        components.addLast(component);
    }

    /**
     * Removes the Component passed in from the ComponentRegistry's list of registered Components.
     */
    public static void removeComponent(ComponentBase component) {
        for(var comp : components) {
            if(comp == component) {
                components.remove(comp);
            }
        }
    }

    /**
     * @return A LinkedList of the ComponentRegistry's registered Components. 
     */
    public static LinkedList<ComponentBase> getComponents() {
        return components;
    }
}
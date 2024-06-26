package fundamentals.component;

import java.util.LinkedList;
import java.awt.*;

import fundamentals.Coordinates;
import fundamentals.animation.Animation;
import fundamentals.mechanic.MechanicScheduler;

/**
 * The superclass to every app component. ComponentBase allows component classes to inherit methods for setting its image's
 * Coordinates, Animation, opacity, and activity. It is required of every component to extend ComponentBase as a superclass while 
 * calling the addRequirements(int x, int y, int degrees, Animation... animations) function for appropriate functionality.
 * 
 * @see
 * Note: When ComponentBase is used as a superclass and it is extended to a subclass, that component subclass will immediately create 
 * its image on screen upon instantiation.   
 */
public class ComponentBase implements ComponentInterface
{
    private final double COMPONENT_ID = Math.random();
    private LinkedList<Animation> animations = new LinkedList<Animation>();
    private Animation current_animation = new Animation("");
    private Coordinates coordinates = new Coordinates(0, 0, 0);
    private double opacity_pct = 1.0;
    private boolean active = false;

    @Override
    public void periodic() {}

    /**
     * Once ComponentBase has been extended and become a superclass to a subclass, the subclass must call 
     * this method in order for the subclass to appropriately function as a app component. Moreover, the initial on-screen
     * coordinates of the component must be passed in; along with the component's rotation relative to the X-axis. Lastly, 
     * all Animation instances must be passed in. Animations will be PNGs or GIFs, and multiple can be passed into the parameters; 
     * these animations will be considered as all of the visual representations that the component can have at any time when
     * running the application.  
     * 
     * @see Animation is a vague term referring to any PNG or GIF image. 
     */
    public void addRequirements(int x, int y, int degrees, Animation... animations) {
        current_animation = animations[0];
        coordinates.setCoordinates(x, y, degrees);
        for(var animation : animations) {
            this.animations.addLast(animation);
        }

        toggleActivity(true);
        ComponentRegistry.registerComponent(this);
        MechanicScheduler.getInstance().registerComponent(this);
    }

    /**
     * The current image can be set by passing in the name of the image file; file type included. (EX: "myImage.png" )
     * @see If the method is never called, the image belonging to the first Animation instance passed into the constructor will be used.
     */
    public void setAnimation(String animation_name) {
        for(var animation : animations) {
            if(animation.getName() == animation_name) {
                current_animation = animation;
            }
        }
    }

    /**
     * Used to set the component's opacity when on-screen. Opacity is how "solid" or visible the image is. The opposite 
     * of transparency.  
     * 
     * @param pct (double) : How opaque the component's image should be on screen as a percentage that is represented as a decimal.
     * @see The percentage representation as a decimal must be a value within the interval: [0, 1].
     */
    public void setOpacity(double pct) {
        opacity_pct = pct;
    }

    /**
     * @return How opaque the component is on-screen as a percentage that is represented as a decimal.
     * 
     * @see
     * Note: The value returned will be within the interval: [0, 1].
     */
    public double getOpacity() {
        return opacity_pct;
    }

    /**
     * Toggles the component's activity to either be enabled or disabled.
     * @param active (boolean) : The specified new toggled active state for the controller.
     */
    public void toggleActivity(boolean active) {
        if(!this.active && active) {
            this.active = true;
            ComponentRegistry.registerComponent(this);
        }
        else if(this.active & !active) {
            this.active = false; 
            ComponentRegistry.removeComponent(this);
        }
    }

    /**
     * Sets the component's coordinates.
     */
    public void setCoordinates(int x, int y, int degrees) {
        coordinates.setCoordinates(x, y, degrees);
    }

    /**
     * @return The component's coordinates. 
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return The component's personal ID; a specific and unique value that is assigned upon instantiation.
     */
    public double getComponentID() {
        return COMPONENT_ID;
    }

    /**
     * @return An Image instance from the animation/image that the component is currently using. 
     */
    public Image getAnimation() {
        return current_animation.getAnimation();
    }

    /**
     * @return An the width of the animation/image that the component is currently using. 
     * @see Unit of measurement: Pixels.
     */
    public int getWidth() {
        return current_animation.getImageWidth();
    }

    /**
     * @return An the height of the animation/image that the component is currently using. 
     * @see Unit of measurement: Pixels.
     */
    public int getHeight() {
        return current_animation.getImageHeight();
    }

    /**
     * @return The activity status of the component. 
     */
    public boolean getActivity() {
        return active;
    }
}

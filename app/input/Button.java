package app.input;

import fundamentals.UI.Trigger;

/**
 * Used to determine the state of a button on a Controller instance, Button makes use of AppInput to read a specific keyboard key
 * to determine when it's idle, pressed, and released. Moreover, this is especially useful because AppInput can read many keys 
 * consecutively, but not simultaneously. So, Button instances can still change states in what appears to be in a simultaneous fashion
 * even though AppInput can only read a single key at time. Furthermore, allowing a Button to read the state of only its key from AppInput to interpret what the
 * Button's current state should be.
 */
public class Button
{
    private int key_id = 0;
    // determines if the button is currently being held down and is active
    private boolean current_is_active = false;

    /**
     * Used to determine the state of a button on a Controller instance, Button makes use of AppInput to read a specific keyboard key
     * to determine when it's idle, pressed, and released. Moreover, this is especially useful because AppInput can read many keys 
     * consecutively, but not simultaneously. So, Button instances can still change states in what appears to be in a simultaneous fashion
     * even though AppInput can only read a single key at time. Furthermore, allowing a Button to read the state of only its key from AppInput to interpret what the
     * Button's current state should be.
     * 
     * @param app_input
     * - The AppInput instance; the instance passed in should be instantiated in GameBase. 
     * 
     * @param key_id
     * - The key code of the keyboard key to use for the Button. 
     */
    public Button(int key_id) {
        this.key_id = key_id;
    }

    /**
     * @return the key code of the Button's keyboard key. 
     */
    public int getKey() {
        return key_id;
    }

    private boolean isActive() {
        if(AppInput.getInstance().isKeyPressed(key_id)) {
            current_is_active = true;
        }
        else if(AppInput.getInstance().isKeyReleased(key_id)) {
            current_is_active = false;
        }
        
        return current_is_active;
    }

    public Trigger getTrigger() {
        return new Trigger((Void) -> {return isActive();});
    }
}
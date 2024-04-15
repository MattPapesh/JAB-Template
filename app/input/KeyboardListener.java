package app.input;

import java.awt.event.*;
import java.util.LinkedList;

/**
 * KeyboardListener is responsible for logging all keyboard keys experiencing a key-pressed or key-released event 
 * by implementing KeyListener. Moreover, this allows the application to know this state of every key on the keyboard.
 */
public class KeyboardListener implements KeyListener
{
    private LinkedList<Integer> pressed_key_ids = new LinkedList<Integer>();
    private LinkedList<Integer> released_key_ids = new LinkedList<Integer>();
    private static KeyboardListener listener = new KeyboardListener();

    @Override 
    public void keyPressed(KeyEvent e) {   
        for(var released_key_id : released_key_ids) {
            if(e.getKeyCode() == released_key_id) {
                released_key_ids.remove(released_key_id);
            }
        }

        if(!pressed_key_ids.contains(e.getKeyCode())) {
            pressed_key_ids.addLast(e.getKeyCode());  
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {   
        for(var pressed_key_id : pressed_key_ids) {
            if(e.getKeyCode() == pressed_key_id) {
                pressed_key_ids.remove(pressed_key_id);
            }
        }

        if(!released_key_ids.contains(e.getKeyCode())) {
            released_key_ids.addLast(e.getKeyCode());
        }
    }

    /**
     * @return Whether or not the key associated with the key code passed in is currently pressed.
     */
    protected boolean isKeyPressed(int key_id) {
        for(var pressed_key_id : pressed_key_ids) {
            if(pressed_key_id == key_id) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return Whether or not the key associated with the key code passed in is currently released. 
     */
    protected boolean isKeyReleased(int key_id) {
        for(var released_key_id : released_key_ids) {
            if(released_key_id == key_id) {
                return true;
            }
        }

        return false; 
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * @return The implemented KeyListener instance.
     */
    public KeyListener getKeyListener() {
        return this;
    }

    /**
     * @return The amount of keyboard keys that are currently being pressed.
     */
    public int getPressedSize() {
        return pressed_key_ids.size();
    }

    /**
     * @return The amount of keyboard keys that have currently been released.  
     */
    public int getReleasedSize() {
        return released_key_ids.size();
    }

    public static KeyboardListener getInstance() {
        return listener;
    }
}
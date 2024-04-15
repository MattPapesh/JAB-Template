package fundamentals.UI;

import java.util.HashSet;
import java.util.Set;

import app.input.Button;

/**
 * Utilizes multiple keyboard keys. These keys are used together to represent a controller on a keyboard. Moreover, 
 * the status of each key in a controller can be determined with this class while also assigning mechanics to keys; either scheduling
 * each keys' respective mechanic once or continuously when finished whenever a key is pressed. 
 */
public class Controller 
{
    private Set<Button> buttons = new HashSet<Button>();

    public Controller() {}

    /**
     * Utilizes multiple keyboard keys. These keys are used together to represent a controller on a keyboard. Moreover, 
     * the status of each key in a controller can be determined with this class while also assigning mechanics to keys; either scheduling
     * each keys' respective mechanic once or continuously when finished whenever a key is pressed. 
     * 
     * @param buttons (Button...) : The specified buttons. 
     */
    public Controller(Button... buttons) {
        appendButtons(buttons);
    }

    public void appendButtons(Button... buttons) {
        for(var button : buttons) {
            this.buttons.add(button);
        }
    }

    /**
     * @return The amount of buttons in a controller.
     */
    public int getAmountOfButtons() {
        return buttons.size();
    }
}
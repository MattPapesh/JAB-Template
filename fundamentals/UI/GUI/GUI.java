package fundamentals.UI.GUI;

import java.util.LinkedList;
import java.util.function.Function;

import app.audio.AppAudio;
import fundamentals.appbase.AppBase;
import fundamentals.mechanic.MechanicBase;
import fundamentals.mechanic.MechanicScheduler;

/**
 *  The Graphical User Interface (GUI), is a collection of GUIOption instances. Each GUI option is passed into the constructor 
 *  in order to run the mechanic associated with the "current" GUI option that is selected. Moreover, the GUIOption instances are listed together
 *  and are all associated by index based on how they are passed into the constructor; the first instance passed in is index zero. Lastly,
 *  given that a GUIOption's superclass is the ComponentBase, each option is a component seen on-screen, where each option's behavior and their respective
 *  mechanics' behavior are controlled by the GUI.
 */
public class GUI extends AppBase
{
    private LinkedList<GUIOption> UI_options = new LinkedList<GUIOption>();
    private int current_selected_index = 0;
    private boolean active = false;    
    private boolean toggled_activity = false; 
    // Audio files:
    private String scroll_file_name = "";
    private String activate_option_file_name = "";
    // Function definitions for enabling scrolling UI:
    Function<Void, Boolean> scroll_left_def = null;
    Function<Void, Boolean> scroll_right_def = null;
    // Function definition for enabling option activation. 
    Function<Void, Boolean> activate_def = null;

    /**
     *  The GUI, or Graphical User Interface, is a collection of GUIOption instances. Each GUI option is passed into the constructor 
     *  in order to run the mechanic associated with the "current" GUI option that is selected. Moreover, the GUIOption instances are listed together
     *  and are all associated by index based on how they are passed into the constructor; the first instance passed in is index zero. Lastly,
     *  given that a GUIOption's superclass is the ComponentBase, each option is a component seen on-screen, where each option's behavior and their respective
     *  mechanics' behavior are controlled by the GUI
     * 
     *  @param UI_options (GUIOption...) : The specified options that will be included and controlled by the GUI. 
     */
    public GUI(Function<Void, Boolean> scroll_left_def, Function<Void, Boolean> scroll_right_def, Function<Void, Boolean> activate_def, GUIOption... UI_options) {
        this.scroll_left_def = scroll_left_def;
        this.scroll_right_def = scroll_right_def;
        this.activate_def = activate_def;
        for(var option : UI_options) {
            option.setParentGUI(this);
            this.UI_options.addLast(option);
        }
 
        MechanicScheduler.getInstance().registerEvent((Void) -> {
            if(active && toggled_activity) {
                toggled_activity = false; 
                setComponentActivity(active);
            }
            else if(active && !toggled_activity) {
                if(scroll_left_def.apply(null) && !scroll_right_def.apply(null) && !activate_def.apply(null)) {
                    AppAudio.getInstance().playAudioFile(scroll_file_name);
                    setSelectedOption(current_selected_index - 1);
                }
                else if(scroll_right_def.apply(null) && !scroll_left_def.apply(null) && !activate_def.apply(null)) {
                    AppAudio.getInstance().playAudioFile(scroll_file_name);
                    setSelectedOption(current_selected_index + 1);
                }
                else if(activate_def.apply(null) && !scroll_left_def.apply(null) && !scroll_right_def.apply(null))  {
                    AppAudio.getInstance().playAudioFile(activate_option_file_name);
                    MechanicBase mechanic = this.UI_options.get(current_selected_index).getAssociatedMechanic();
                    if(mechanic != null)
                        MechanicScheduler.getInstance().scheduleMechanic(this.UI_options.get(current_selected_index).getAssociatedMechanic());
                }
                     
                for(var option : UI_options) {
                    option.run();
                }
            }
            
            return Void;
        });

        setSelectedOption(0);
        toggleActivity(true);
    }

    private void setComponentActivity(boolean active) {
        for(var option : UI_options) {
            option.toggleActivity(active);
        }
    }

    /**
     * @return The current GUIOption selected in the GUI.
     */
    public GUIOption getOptionSelected() {
        return UI_options.get(current_selected_index);
    }

    /**
     * Used to toggle the activity of the Component instances that make up the GUIOptions and GUI.
     * @param active (boolean) : The specified active status to set. 
     */
    public void toggleActivity(boolean active) {
        this.active = active;
        toggled_activity = true;
    }

    /**
     * @return Whether or not the GUI is currently active.
     */
    public boolean getActivity() {
        return active;
    }

    /**
     * Used to make sure all GUIOptions are not selected. 
     */
    public void reset() {
        for(var option : UI_options) {
            option.toggleSelectedStatus(false);
        }
    }

    /**
     * @return The amount of GUIOptions. 
     */
    public int getAmountOfOptions() {
        return UI_options.size();
    }

    /**
     * Used to select one of the GUI's GUIOptions. Moreover, only one option can be selected at a time, and so all GUIOptions are
     * reset before selecting the desired option based on the index passed in; the GUIOptions are listed based on index, where the list
     * and the indices are determined based on the order of which each GUIOption is passed into the GUI's constructor. 
     * 
     * @param index (int) : The specified index associated with the GUIOption that should be selected. 
     */
    public void setSelectedOption(int index) {
        try {
            reset();
            index = Math.min(Math.max(index, 0), UI_options.size() - 1);
            UI_options.get(index).toggleSelectedStatus(true);
            current_selected_index = index;
        }
        catch(IndexOutOfBoundsException e) {}
    }

    /**
     * @return The index of the GUIOption that is currently selected.
     * @see The GUIOption instances are indexed based on how they 
     * are passed into the GUI's constructor. 
     */
    public int getSelectedOptionIndex() {
        return current_selected_index;
    }

     /**
     * Used to play WAV audio files for whenever the GUI is interacted with; whether a new GUIOption is selected, or an option is activated.
     * @param scroll_file_name (String) : The specified file to play once whenever another GUIOption is selected by scrolling from one option to another.
     * @param activate_option_file_name (String) : The specified file to play once whenever the currently-selected GUIOption is activated. 
     * @see Only WAV file types can be used!
     */
    public void setAudio(String scroll_file_name, String activate_option_file_name) {
        this.scroll_file_name = scroll_file_name;
        this.activate_option_file_name = activate_option_file_name;
    }
}
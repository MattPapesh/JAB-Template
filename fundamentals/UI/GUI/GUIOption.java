package fundamentals.UI.GUI;

import java.util.LinkedList;
import java.util.function.Function;

import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;
import fundamentals.mechanic.MechanicBase;

public class GUIOption extends ComponentBase
{
    private Animation[] UI_option = new Animation[2];
    private LinkedList<String> idle_file_names = new LinkedList<String>(); 
    private LinkedList<String> selected_file_names = new LinkedList<String>(); 
    private int current_idle_file_index = 0;
    private int current_selected_file_index = 0;

    private boolean is_selected = false; 
    private boolean update = true;
    private Function<Void, MechanicBase> event = null;
    private GUI parent_GUI = null;

    /**
     *  A GUIOption is a variable that is passed into a GUI instance. Given a GUIOption's superclass is the ComponentBase, each instance
     *  of this class is a component that is seen on screen. A GUIOption will require a image (png. file type) that is used to represent when
     * the option is idle along with when it is selected on-screen. Finally, coordinates and a mechanic can be passed into the constructor,
     * where the mechanic will be scheduled and be considered the behavior that occurs once the option is selected and used. 
     * 
     * @param idle_file_names (String) : File name(s) of the image(s) that will visually-represent the option.
     * @param selected_file_name (String) : File name(s) of the image(s) that will visually-represent the option when it is "selected".
     * @param x (int) : The specified unsigned x coordinate. 
     * @param y (int) : The specified unsigned y coordinate. 
     * @param event (Function<Void, MechanicBase>) : The mechanic event that will be scheduled by the parent GUI once the option is "selected".
     */

    public GUIOption(String idle_file_name, String selected_file_name, int x, int y, Function<Void, MechanicBase> event) {
        LinkedList<Animation> UI_options = getAnimations(new String[]{idle_file_name, selected_file_name});
        
        UI_option[0] = new Animation(idle_file_name);
        UI_option[1] = new Animation(selected_file_name);

        this.idle_file_names.addLast(idle_file_name);
        this.selected_file_names.addLast(selected_file_name);
        this.event = event;

        addRequirements(x, y, 0, getAnimationsArray(UI_options));
        setAnimation(UI_option[0].getName());
    }  

    /**
     *  A GUIOption is a variable that is passed into a GUI instance. Given a GUIOption's superclass is the ComponentBase, each instance
     *  of this class is a component that is seen on screen. A GUIOption will require a image (png. file type) that is used to represent when
     * the option is idle along with when it is selected on-screen. Finally, coordinates and a mechanic can be passed into the constructor,
     * where the mechanic will be scheduled and be considered the behavior that occurs once the option is selected and used. 
     * 
     * @param idle_file_names (String[]) : File name(s) of the image(s) that will visually-represent the option.
     * @param selected_file_name (String[]) : File name(s) of the image(s) that will visually-represent the option when it is "selected".
     * @param x (int) : The specified unsigned x coordinate. 
     * @param y (int) : The specified unsigned y coordinate. 
     * @param event (Function<Void, MechanicBase>) : The mechanic event that will be scheduled by the parent GUI once the option is "selected".
     */

    public GUIOption(String[] idle_file_names, String[] selected_file_names, int x, int y, Function<Void, MechanicBase> event) {
        LinkedList<Animation> UI_options = getAnimations(idle_file_names);
        UI_options.addAll(getAnimations(selected_file_names));

        UI_option[0] = new Animation(idle_file_names[0]);
        UI_option[1] = new Animation(selected_file_names[0]);

        this.idle_file_names = appendFileNames(this.idle_file_names, idle_file_names);
        this.selected_file_names = appendFileNames(this.selected_file_names, selected_file_names);
        this.event = event;

        addRequirements(x, y, 0, getAnimationsArray(UI_options));
        setAnimation(UI_option[0].getName());
    }  

    /**
     *  A GUIOption is a variable that is passed into a GUI instance. Given a GUIOption's superclass is the ComponentBase, each instance
     *  of this class is a component that is seen on screen. A GUIOption will require a image (png. file type) that is used to represent when
     * the option is idle along with when it is selected on-screen. Finally, coordinates and a mechanic can be passed into the constructor,
     * where the mechanic will be scheduled and be considered the behavior that occurs once the option is selected and used. 
     * 
     * @param idle_file_names (String[]) : File name(s) of the image(s) that will visually-represent the option.
     * @param selected_file_name (String) : File name(s) of the image(s) that will visually-represent the option when it is "selected".
     * @param x (int) : The specified unsigned x coordinate. 
     * @param y (int) : The specified unsigned y coordinate. 
     * @param event (Function<Void, MechanicBase>) : The mechanic event that will be scheduled by the parent GUI once the option is "selected".
     */


    public GUIOption(String[] idle_file_names, String selected_file_name, int x, int y, Function<Void, MechanicBase> event) {
        LinkedList<Animation> UI_options = getAnimations(idle_file_names);
        UI_options.addAll(getAnimations(new String[]{selected_file_name}));

        UI_option[0] = new Animation(idle_file_names[0]);
        UI_option[1] = new Animation(selected_file_name);

        this.idle_file_names = appendFileNames(this.idle_file_names, idle_file_names);
        this.selected_file_names.addLast(selected_file_name);
        this.event = event;

        addRequirements(x, y, 0, getAnimationsArray(UI_options));
        setAnimation(UI_option[0].getName());
    } 

    /**
     *  A GUIOption is a variable that is passed into a GUI instance. Given a GUIOption's superclass is the ComponentBase, each instance
     *  of this class is a component that is seen on screen. A GUIOption will require a image (png. file type) that is used to represent when
     * the option is idle along with when it is selected on-screen. Finally, coordinates and a mechanic can be passed into the constructor,
     * where the mechanic will be scheduled and be considered the behavior that occurs once the option is selected and used. 
     * 
     * @param idle_file_names (String) : File name(s) of the image(s) that will visually-represent the option.
     * @param selected_file_name (String[]) : File name(s) of the image(s) that will visually-represent the option when it is "selected".
     * @param x (int) : The specified unsigned x coordinate. 
     * @param y (int) : The specified unsigned y coordinate. 
     * @param event (Function<Void, MechanicBase>) : The mechanic event that will be scheduled by the parent GUI once the option is "selected".
     */

    public GUIOption(String idle_file_name, String[] selected_file_names, int x, int y, Function<Void, MechanicBase> event) {
        LinkedList<Animation> UI_options = getAnimations(new String[]{idle_file_name});
        UI_options.addAll(getAnimations(selected_file_names));

        UI_option[0] = new Animation(idle_file_name);
        UI_option[1] = new Animation(selected_file_names[0]);

        this.idle_file_names.addLast(idle_file_name);
        this.selected_file_names = appendFileNames(this.selected_file_names, selected_file_names);
        this.event = event;

        addRequirements(x, y, 0, getAnimationsArray(UI_options));
        setAnimation(UI_option[0].getName());
    } 

    private Animation[] getAnimationsArray(LinkedList<Animation> animations) {
        Animation[] animations_array = new Animation[animations.size()];
        for(int i = 0; i < animations.size(); i++) {
            animations_array[i] = animations.get(i);
        }

        return animations_array;
    }

    private LinkedList<Animation> getAnimations(String[] file_names) {
        LinkedList<Animation> animations = new LinkedList<Animation>();
        for(int i = 0; i < file_names.length; i++) {
            animations.addLast(new Animation(file_names[i]));
        }

        return animations;
    }

    private LinkedList<String> appendFileNames(LinkedList<String> list, String... file_names) {
        for(int i = 0; i < file_names.length; i++) {
            list.addLast(file_names[i]);
        }

        return list;
    } 

    /**
     * Cycles through all images used for the GUIOption's visual representation on-screen when the option is not "selected".
     * This allows different images to represent the option when it is "idle".
     */
    private void useNextIdleImage() {
        current_idle_file_index = (current_idle_file_index + 1 < idle_file_names.size()) ? current_idle_file_index + 1 : 0;
        UI_option[0] = new Animation(idle_file_names.get(current_idle_file_index));
        update = true;
    }

    /**
     * Cycles through all images used for the GUIOption's visual representation on-screen when the option is not "idle".
     * This allows different images to represent the option when it is "selected".
     */
    private void useNextSelectedImage() {
        current_selected_file_index = (current_selected_file_index + 1 < selected_file_names.size()) ? current_selected_file_index + 1 : 0;
        UI_option[1] = new Animation(selected_file_names.get(current_selected_file_index));
        update = true;
    }

    /**
     * @return The Animation that possesses the image that currently is used for the visual representation of the GUIOption on-screen
     *  when the option is "idle".
     */
    public Animation getIdleAnimation() {
        return UI_option[0];
    } 

    /**
     * @return The Animation that possesses the image that currently is used for the visual representation of the GUIOption on-screen
     *  when the option is "selected".
     */
    public Animation getSelectedAnimation() {
        return UI_option[1];
    }

    /**
     *  @return The dynamic mechanic that was passed into the constructor.
     */
    public MechanicBase getAssociatedMechanic() {
        return event.apply(null);
    }

    /**
     *  @return Whether or not this instance of GUIOption is "selected".
     */
    public boolean getSelectedStatus() {
        return is_selected;
    }
    
    /**
     * @return The GUI that possesses this instance of GUIOption.
     */
    public GUI getParentGUI() {
        return parent_GUI;
    }
    
    protected void setParentGUI(GUI GUI) {
        parent_GUI = GUI;
    }

    /**
     * Determines whether or not the GUIOption is currently "selected".
     */
    public void toggleSelectedStatus(boolean is_selected) {
        if(this.is_selected != is_selected) {
            this.is_selected = is_selected;
            update = true;
        }
    }

    /**
     * Allows the GUIOption to operate appropriately when continuously called. This method should only be called from 
     * GUIScheduler.java, by AppBase.java
     */
    public void run() {
        if(is_selected && update) {
            setAnimation(UI_option[1].getName());
            useNextSelectedImage();
            update = false;
        }
        else if(update) {
            setAnimation(UI_option[0].getName());
            useNextIdleImage();
            update = false; 
        }
    }
}
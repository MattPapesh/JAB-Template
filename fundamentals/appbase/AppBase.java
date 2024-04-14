package fundamentals.appbase;

import java.awt.Image;
import java.util.LinkedList;

import javax.swing.JFrame;

import app.AppGraphics;
import app.audio.AppAudio;
import app.input.AppInput;
import fundamentals.Constants;
import fundamentals.UI.Controller;
import fundamentals.UI.ControllerScheduler;
import fundamentals.UI.GUI.GUI;
import fundamentals.UI.GUI.GUIScheduler;
import fundamentals.animation.Animation;
import fundamentals.mechanic.MechanicBase;
import fundamentals.mechanic.MechanicScheduler;

/**
 * The base of every application. AppBase is what generates the application's window by using JFrame as its
 * superclass, while making use of AppGraphics, AppInput, AppAudio. Furthermore, every application is a program that
 * continuously loops until the window is closed, and so AppBase is responsible for continuously running GUIs, Controllers,
 * Mechanics, and Components from their respective Scheduler classes. Moreover, AppBase must be extended as the superclass
 * of App.java & AppContainer.java to return Controller instances, run app audio, and to determine the application's current
 * status. 
 */
public class AppBase extends JFrame implements AppInterface
{
    private LinkedList<Image> icons = new LinkedList<Image>();
    private int current_icon_index = 0;

    private AppGraphics app_graphics = new AppGraphics();
    private static AppInput app_input = new AppInput();
    private static AppAudio app_audio = new AppAudio();

    private PrioritizedAppStatus prev_app_status = null;
    private PrioritizedAppStatus app_status = null;
    
    private boolean determined_app_status = false;

    private interface PrioritizedAppStatus {
        public void prioritizedInit();
        public void prioritizedPeriodic();
        public int getStatusID();
    };

    private void prepareAppIcon() {
        for(int i = 0; i < Constants.WINDOW_CHARACTERISTICS.APP_ICON_IMAGES.length; i++) {
            icons.addLast(new Animation(Constants.WINDOW_CHARACTERISTICS.APP_ICON_IMAGES[i]).getAnimation());
        }
    }

    private void updateIconImage(int update_delay_millis) {
        if(getMillis() % update_delay_millis == 0) {
            super.setIconImage(icons.get(current_icon_index));
            current_icon_index++;
            if(current_icon_index >= icons.size()) {
                current_icon_index = 0;
            }
        }
    }

    public static int getMillis() {
        return (int)System.currentTimeMillis();
    }

    /**
     * Must be called once to begin running the application. 
     */
    public void startApp() {
        appBaseInit();
        while(true) {
            try {
                appBasePeriodic();
                Thread.sleep(Constants.WINDOW_CHARACTERISTICS.REFRESH_RATE_MILLIS);
            }
            catch(InterruptedException e) {}
        }
    }

    /**
     * Is called once to serve as initialization before running the application. 
     */
    private void appBaseInit() {
        prepareAppIcon();

        super.setSize(Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT);
        super.setResizable(false);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setTitle(Constants.WINDOW_CHARACTERISTICS.APP_TITLE);
        super.setIconImage(icons.getLast());
        super.setLocationRelativeTo(null);
        super.setVisible(true);
        
        super.add(app_graphics);
        super.addKeyListener(app_input.getKeyListener());
    }

    /**
     * Plays the audio file passed in once. 
     * @param file_name (String) : The specified name of the audio file to play. 
     * @see Only WAV audio files are compatible. Example argument: "file.wav" 
     */
    public static void playAudioFile(String file_name) {
        app_audio.playAudioFile(file_name);
    }

    /**
     * Consecutively plays the audio file passed in as many times as described.  
     * @param file_name (String) : The specified name of the audio file to play.
     * @param count (int) : The unsigned amount of times to play the described audio file. 
     * @see Only WAV audio files are compatible. Example argument: "file.wav"
     */
    public static void playAudioFileLoop(String file_name, int count) {
        app_audio.playAudioFileLoop(file_name, Math.max(count, 0));
    }

    /**
     * Consecutively plays the audio file passed continuously without stopping. 
     * @param file_name (String) : The specified name of the audio file to play.
     * @see Only WAV audio files are compatible. Example argument: "file.wav"
     */
    public static void playAudioFileLoopContinuously(String file_name) {
        app_audio.playAudioFileLoopContinuously(file_name);
    }

    /**
     * Immediately stops playing the audio file described. Moreover, if the audio file being stopped is being
     * consecutively replayed on a loop, all future replays that have not yet occurred will be immediately 
     * canceled. 
     * 
     * @param file_name (String) : The specified name of the audio file to stop.
     * @see Only WAV audio files are compatible. Example argument: "file.wav"
     */
    public static void stopAudioFile(String file_name) {
        app_audio.stopAudioFile(file_name);
    }

    /**
     * Stops playing all currently-playing audio files and cancels any replays for audio files that are 
     * being consecutively played on a loop. 
     * 
     * @see Only WAV audio files are compatible. Example argument: "file.wav"
     */
    public static void stopAllAudioFiles() {
        app_audio.stopAllAudioFiles();
    } 

    /**
     * Returns a Controller instance based on the key codes passed in. All Controllers must be returned instances
     * from this method and should never be manually instantiated in a application; AppBase will handle instantiation.
     * Finally, the key IDs passed in will be key codes associated with keyboard keys, and those keys will become the
     * group of buttons for the Controller instance returned.  
     * 
     * @param left_key_id (int) : The specified key code of the left key. 
     * @param right_key_id (int) : The specified key code of the right key. 
     * @param up_key_id (int) : The specified key code of the up key. 
     * @param down_key_id (int) : The specified key code of the down key. 
     * 
     * @see The keyboard keys used for the controller are intended to be neighboring keys that follow a WASD format.
     * 
     * @return A new Controller instance that possesses the intended keyboard keys in a WASD format.  
     */
    public Controller getController(int left_key_id, int right_key_id, int up_key_id, int down_key_id) {
        return new Controller(app_input, left_key_id, right_key_id, up_key_id, down_key_id);
    }

    private void runGUIs() {
       // GUI GUI = GUIScheduler.getGUIInstance(); 
        //for(int i = 0; GUI != null && i < GUI.getAmountOfOptions(); i++) {
         //   GUI.getOptionInstance().run();
       // }
    }

    private void runControllers() {
        Controller controller = ControllerScheduler.getControllerInstance();
        for(int i = 0; controller != null && i < controller.getAmountOfButtons(); i++) {
            controller.getButtonInstance().run();
        }
    }

    // Periodically called by app refresh rate (tick system)
    private void appBasePeriodic() {
        try {
            updateIconImage(1000);
            determineAppStatus();
            if(prev_app_status == null || prev_app_status.getStatusID() != app_status.getStatusID()) {
                app_status.prioritizedInit(); 
            }

            app_status.prioritizedPeriodic();
            MechanicScheduler.getInstance().run();
            runGUIs();
            runControllers();
        }
        catch(NullPointerException e) {}
    }

    /**
     * The application can possess different statuses at different times. This method is called
     * to assign the "menu status" as the application's current status. 
     */
    public void initiateAppStatus() {
        prev_app_status = app_status;
        app_status = new PrioritizedAppStatus() {

            @Override public void prioritizedInit() { appInit(); }
            @Override public void prioritizedPeriodic() { appPeriodic(); }
            @Override public int getStatusID() { return 1; }
        };
    }
 
    /**
     * Resets the application's current status. Calling this method will put the application in a "idle status", 
     * and any of the initiate status methods will need to be called to put the application into an active state
     * again. 
     */
    private void resetAppStatuses() {
        determined_app_status = false;
    }

    @Override public void determineAppStatus() {}
    public boolean getAppStatus() { return determined_app_status; }
    @Override public void appInit() {}
    @Override public void appPeriodic() {}
 
    /**
     * This method enables the application to possess the "app status" as a program state. This is most useful when
     * used when overriding the determineAppStatus() method when creating conditions for when certain initiate status
     * methods should be called. 
     * 
     * @see
     * WARNING: This method only enables the application to possess the mentioned status, BUT the method DOES NOT
     * PUT the application into the status mentioned; the initiate status methods must be used to transition between statuses!
     */
    public void enableMenuStatus() { 
        resetAppStatuses();
        determined_app_status = true; 
    }
}

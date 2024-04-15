package fundamentals.appbase;

import java.awt.Image;
import java.util.LinkedList;

import javax.swing.JFrame;

import app.GraphicsRenderer;
import app.input.KeyboardListener;
import fundamentals.Constants;
import fundamentals.animation.Animation;
import fundamentals.mechanic.MechanicScheduler;

/**
 * The base of every application. AppBase is what generates the application's window by using JFrame as its
 * superclass, while making use of AppGraphics, KeyboardListener, AppAudio. Furthermore, every application is a program that
 * continuously loops until the window is closed, and so AppBase is responsible for continuously running GUIs, Controllers,
 * Mechanics, and Components from their respective Scheduler classes. Moreover, AppBase must be extended as the superclass
 * of App.java & AppContainer.java to return Controller instances, run app audio, and to determine the application's current
 * status. 
 */
public class AppBase extends JFrame implements AppInterface
{
    private LinkedList<Image> icons = new LinkedList<Image>();
    private int current_icon_index = 0;
    private PrioritizedAppStatus prev_app_status = null;
    private PrioritizedAppStatus app_status = null;
    private boolean determined_app_status = false;
    // Initial millis start time:
    private static long program_start_millis = System.currentTimeMillis();

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
            current_icon_index = (current_icon_index + 1 < icons.size()) ? current_icon_index + 1 : 0;
        }
    }

    public static int getMillis() {
        return (int)(System.currentTimeMillis() - program_start_millis);
    }

    /**
     * Must be called once to begin running the application. 
     */
    public void startApp() {
        int initial_millis = getMillis();
        appBaseInit();
        while(true) {
            try {
                appBasePeriodic();
                Thread.sleep(Math.max(Constants.WINDOW_CHARACTERISTICS.REFRESH_RATE_MILLIS - getMillis() + initial_millis, 0));
                initial_millis = getMillis();
            }
            catch(InterruptedException e) {}
        }
    }

    /**
     * Is called once to serve as initialization before running the application. 
     */
    private void appBaseInit() {
        prepareAppIcon();
        initiateAppStatus();

        super.setSize(Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT);
        super.setResizable(false);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setTitle(Constants.WINDOW_CHARACTERISTICS.APP_TITLE);
        super.setIconImage(icons.getLast());
        super.setLocationRelativeTo(null);
        super.setVisible(true);
        
        super.add(GraphicsRenderer.getInstance());
        super.addKeyListener(KeyboardListener.getInstance().getKeyListener());
    }

    // Periodically called by app refresh rate (tick system)
    private void appBasePeriodic() {
        updateIconImage(1000);
        determineAppStatus();
        if(prev_app_status == null || prev_app_status.getStatusID() != app_status.getStatusID()) {
            app_status.prioritizedInit(); 
            prev_app_status = app_status;
        }

        app_status.prioritizedPeriodic();
        
        MechanicScheduler.getInstance().runComponentPeriodics();
        MechanicScheduler.getInstance().runEvents();
        MechanicScheduler.getInstance().runMechanics();
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
     * @see This method only enables the application to possess the mentioned status, BUT the method DOES NOT
     * PUT the application into the status mentioned; the initiate status methods must be used to transition between statuses!
     */
    public void enableMenuStatus() { 
        resetAppStatuses();
        determined_app_status = true; 
    }
}

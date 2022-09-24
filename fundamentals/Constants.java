package fundamentals;

/**
 * All global constants defined in specific sub-classes that were statically declared and defined. 
 */
public class Constants 
{
    /*
     * Key codes for each key used in the application. 
     */
    public static final class KEY_ID
    {
        /*
         * Key code IDs for keys binded to player 1/ the blue robot.
         */
        public static final class BLUE_BOT
        {
            public static final int LEFT = 65;
            public static final int RIGHT = 68;
            public static final int CARGO_UP = 87;
            public static final int CARGO_DOWN = 83;

            public static final int CLIMB_A = LEFT;
            public static final int CLIMB_B = RIGHT;
            public static final int CLIMB_C = CARGO_UP;
            public static final int CLIMB_D = CARGO_DOWN;
        }

        /*
         * Key code IDs for keys binded to player 2/ the red robot.
         */
        public static final class RED_BOT
        {
            public static final int LEFT = 74;
            public static final int RIGHT = 76;
            public static final int CARGO_UP = 73;
            public static final int CARGO_DOWN = 75;

            public static final int CLIMB_A = LEFT;
            public static final int CLIMB_B = RIGHT;
            public static final int CLIMB_C = CARGO_UP;
            public static final int CLIMB_D = CARGO_DOWN;
        }
    }

    /*
     * All root directories for any assets used. 
     */
    public static final class FILE_ROOT_DIRECTORIES
    {
        public static final String IMAGE_ROOT_DIRECTORY = "assets/images/";
        public static final String AUDIO_ROOT_DIRECTORY = "assets/audio/";
    }

    /*
     * Window characteristics for the window that the application displays on-screen.
     */
    public static final class WINDOW_CHARACTERISTICS
    {
        public static final String[] APP_ICON_IMAGES = {"icon_A.png", "icon_B.png"};
        public static final String APP_TITLE = "Rapid React: Shots & Bots";
        public static final int WINDOW_WIDTH = 1154;
        public static final int WINDOW_HEIGHT = 595;
        public static final int REFRESH_RATE_MILLIS = 5;
        public static final double GRAPHICS_TRANSFORMATION_SCALER = 1.25;
    }
}

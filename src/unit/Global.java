package unit;


public class Global {


    public static final int UPDATE_TIMES_PER_SEC=60;
    public static final int FRAME_LIMIT = 60;


    public final static int UNIT_X = 32;
    public final static int UNIT_Y = 32;

    private static int SCREEN_X;
    private static int SCREEN_Y;

    public final static boolean IS_DEBUG = false;


    //地圖大小
    public static final int MAP_WIDTH = 2048;
    public static final int MAP_HEIGHT = 1024;

    //視窗大小
    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 900;

    //追焦對象大小
    public static final int CENTER_WIDTH = 32;
    public static final int CENTER_HEIGHT = 32;

    public enum Actor{
        FIRST("/actorrun.png"),
        SECOND("/actorrun.png"),
        THIRD("/actorrun.png");

        String path;

        public String getPath(){
            return path;
        }

        Actor(String path){
            this.path = path;
        }
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NO;
    }

    public enum Active{
        NUMBER_ONE(-1),
        NUMBER_TWO(-2),
        NUMBER_THREE(-3),
        NUMBER_FORE(-4),
        FLASH(30),
        RELOADING(31),
        CATCH_ITEM(32),
        SPACE(0);


        private int commandCode;

        public int getCommandCode(){
            return commandCode;
        }

        Active(int num){
            commandCode = num;
        }
    }

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    //public static final int SINGLE_MODE = 8;
    //public static final int CONNECT_NET_MODE = 9;


    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }

    //網路
    public static boolean isServer = false;
    public static class NetCommandCode{
        public static final int AIRCRAFT_CONNECT = 0;
        public static final int AIRCRAFT_MOVE = 1;
        public static final int AIRCRAFT_DISCONNECT = 2;
        public static final int ENEMY_ADD = 3;
        public static final int ENEMY_REMOVE =4;
        public static final int SHOT_BOOM = 5;
    }

    private Global(final boolean fullScreen, final int SCREEN_X, final int SCREEN_Y) {
        if (fullScreen) {
            return;
        }
        Global.SCREEN_X = SCREEN_X;
        Global.SCREEN_Y = SCREEN_Y;

    }

    public static int getScreenY() {
        return SCREEN_Y;
    }

    public static int getScreenX() {
        return SCREEN_X;
    }

    public static class Builder {
        private boolean fullScreen;
        private int WIDTH;
        private int HEIGHT;

        public Builder() {
            this.WIDTH = 800;
            this.HEIGHT = 600;
            this.fullScreen = false;
        }

        public Builder setScreenX(final int WIDTH) {
            this.WIDTH = WIDTH;
            return this;
        }

        public Builder setScreenY(final int HEIGHT) {
            this.HEIGHT = HEIGHT;
            return this;
        }

        public Builder setFullScreen(final boolean fullScreen) {
            this.fullScreen = fullScreen;
            return this;
        }

        public Global gen() {
            return new Global(this.fullScreen, this.WIDTH, this.HEIGHT);
        }
    }
}

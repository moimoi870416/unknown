package unit;

public class Global {
    public final static int UNIT_X = 32;
    public final static int UNIT_Y = 32;
    private static int SCREEN_X;
    private static int SCREEN_Y;
    public final static boolean IS_DEBUG = false;
    //  視野大小
    public static final int CAMERA_WIDTH = 400;
    public static final int CAMERA_HEIGHT = 400;

    //追焦對象大小
    public static final int CENTER_WIDTH = 32;
    public static final int CENTER_HEIGHT = 32;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NO
    }

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    public static final int UP = 6;
    public static final int DOWN = -6;
    public static final int LEFT = -7;
    public static final int RIGHT = 7;

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

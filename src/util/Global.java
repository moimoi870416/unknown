package util;

public class Global {

    public static final int UPDATE_TIMES_PER_SEC=60;
    public static final int FRAME_LIMIT = 60;

    public static int mouseX;
    public static int mouseY;

    public static int actorX;
    public static int actorY;
    public static int shootCount = 0;

    public final static int UNIT_X = 32;
    public final static int UNIT_Y = 32;

    public final static boolean IS_DEBUG = true;


    //地圖大小
    public static final int MAP_WIDTH = 2048*5;
    public static final int MAP_HEIGHT = 1024;

    //視窗大小
    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 900;

    //追焦對象大小
    public static final int CENTER_WIDTH = 32;
    public static final int CENTER_HEIGHT = 32;

    public enum MapPath{
        BEGIN("/map/map-begin.png"),
        SECOND("/map/map-second.png"),
        THIRD("/map/map-second.png"),
        FOURTH("/map/map-second.png");
        //END;

        public String mapPath;

        private MapPath(String paht){
            mapPath = paht;
        }
    }

    public enum Actor{
        FIRST("/actor/actorStand.png"),
        SECOND("/actor/actorrun.png"),
        THIRD("/actor/actorrun.png");

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
        NO
    }

    public enum Active{
        NUMBER_ONE(-1),
        NUMBER_TWO(-2),
        NUMBER_THREE(-3),
        NUMBER_FORE(-4),
        SKILL(30),
        RELOADING(31),
        CATCH_ITEM(32),
        SPACE(5);

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

}

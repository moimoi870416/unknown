package util;

public class Global {

    public static final int UPDATE_TIMES_PER_SEC = 60;
    public static final int FRAME_LIMIT = 45;

    public static int actorX;
    public static int actorY;
    public static int shootCount = 0;
    public static int playerNum;

    public final static int UNIT_X = 32;
    public final static int UNIT_Y = 32;

    public static boolean I_AM_MAC = false;
    public final static boolean IS_DEBUG = false;
    //網路
    public static boolean isSingle = true;
    public static boolean isNormal = true;//是不是一般
    public static boolean isAdd = false;  //是不是創建房間
    public static boolean isServer = false;
    public static boolean bossDead = false;

    public final static int BUTTON_WIDTH = 375;
    public final static int BUTTON_HEIGHT = 750;

    public final static int BUTTON_X1 = 250;
    public final static int BUTTON_X2 = 850;
    public final static int BUTTON_Y = 50;

    public final static int LABEL_WIDTH = 300;
    public final static int LABEL_HEIGHT = 100;


    //地圖大小
    public static int MAP_WIDTH = 2048;
    public static int MAP_HEIGHT = 1024;
    public static final int MAP_UNIT_WIDTH = 4096;

    //視窗大小
    public static final int WINDOW_WIDTH = 1440;
    public static final int WINDOW_HEIGHT = 900;

    //追焦對象大小
    public static final int CENTER_WIDTH = 32;
    public static final int CENTER_HEIGHT = 32;

    public static int stoneCount = 0;
    public static int stoneDead = 0;

    public static class NetEvent {
        public static final int CONNECT = 100;

        public static final int ACTOR = 200;
        public static final int ACTOR_MOVE = 201;
        public static final int ACTOR_LIFE = 202;
        public static final int ACTOR_STATE = 203;
        public static final int ACTOR_DIR = 204;
        public static final int ACTOR_FLASH = 205;
        public static final int ACTOR_HEAL = 206;
        public static final int ACTOR_BAR_LEFT = 207;
        public static final int ACTOR_BAR_TOP = 208;
        public static final int ACTOR_CHANGE_GUN = 209;
        public static final int ACTOR_LYSU = 210;

        public static final int BULLET = 300;
        public static final int BULLET_NEW = 301;
        public static final int BULLET_MOVE = 302;
        public static final int BULLET_ATK = 303;
        public static final int BULLET_STATE = 304;
        public static final int BULLET_PENETRATION = 305;

        public static final int MONSTER = 400;
        public static final int MONSTER_NEW = 401;
        public static final int MONSTER_MOVE = 402;
        public static final int MONSTER_LIFE = 403;
        public static final int MONSTER_STATE = 404;
        public static final int MONSTER_DIR = 405;
        public static final int MONSTER_BOSS_ATTACK_TYPE = 406;
        public static final int MONSTER_DEAD = 407;
        public static final int MONSTER_IS_CHASE = 408;
        public static final int MONSTER_FOR_RINO = 409;
        public static final int MONSTER_CHECK = 410;

        public static final int EVENT_CHANGE_SCENE = 500;
        public static final int EVENT_SCENE_MODE = 501;
        public static final int SCENE_NORMAL_MODE = 502;
        public static final int SCENE_LIMIT_MODE = 503;
        public static final int EVENT_CHANGE_BOSS_SCENE = 504;

        public static int MONSTER_CONNECT_ID = 600;
    }

    public enum State {
        ZERO,
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        FIFTH,
        SIXTH,
    }

    public enum MapPath {
        BEGIN("/pictures/map/map-begin.png"),
        FOREST("/pictures/map/map-forest.png"),
        DESERT("/pictures/map/map-desert.png"),
        CHANGE("/pictures/map/map-change.png"),
        END("/pictures/map/map-boss-down.png"),
        LIMIT("/pictures/map/map-limit.png");

        public String mapPath;

        MapPath(String paht) {
            mapPath = paht;
        }
    }


    public enum Actor {
        FIRST("/pictures/actor/actorStand.png", 58, 58),
        SECOND("/pictures/actor/actor2Stand.png", 64, 77),
        THIRD("/pictures/actor/actor3Stand.png", 80, 82);


        String path;
        private int width;
        private int height;

        public String getPath() {
            return path;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        Actor(String path, int width, int height) {
            this.path = path;
            this.width = width;
            this.height = height;
        }
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NO
    }

    public enum Active {
        NUMBER_ONE(-1),
        NUMBER_TWO(-2),
        NUMBER_THREE(-3),
        NUMBER_FORE(-4),
        SKILL(30),
        RELOADING(31),
        CATCH_ITEM(32),
        SPACE(5),
        ENTER(100),
        F1(101),
        F2(102);

        private int commandCode;

        public int getCommandCode() {
            return commandCode;
        }

        Active(int num) {
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

    public static class NetCommandCode {
        public static final int AIRCRAFT_CONNECT = 0;
        public static final int AIRCRAFT_MOVE = 1;
        public static final int AIRCRAFT_DISCONNECT = 2;
        public static final int ENEMY_ADD = 3;
        public static final int ENEMY_REMOVE = 4;
        public static final int SHOT_BOOM = 5;
    }

}

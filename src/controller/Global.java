package controller;

public class Global {
    public final static int UNIT_X = 32;
    public final static int UNIT_Y = 32;
    private static int SCREEN_X;
    private static int SCREEN_Y;

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

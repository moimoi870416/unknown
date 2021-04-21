package menu;

import sence.Scene;
import util.CommandSolver;

import java.awt.*;

public class PopupWindow extends Scene {

    private boolean isShow;
    private boolean isCancelable;

    private int x;
    private int y;
    private int width;
    private int height;

    public PopupWindow(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShow = false;
        this.isCancelable = false;
    }

    public void setCancelable() {
        isCancelable = true;
    }

    public void show() {
        isShow = true;
    }

    public void hide() {
        isShow = false;
    }

    public boolean isShow() {
        return this.isShow;
    }

    @Override
    public void sceneBegin() {

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {

    }
}

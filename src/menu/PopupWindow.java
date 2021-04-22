package menu;

import sence.Scene;
import util.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class PopupWindow extends Scene {

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
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (e == null) {
                return;
            }
            if (isCancelable) { //滑鼠點外面他會hide()
                isCancelableHide(e, state, trigTime);
            }
            e.translatePoint(-x, -y);
            mouseTrig(e, state, trigTime);
            if (isShow == false) {
                sceneEnd();
            }
        };
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void update() {
    }

    public void isCancelableHide(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (state == CommandSolver.MouseState.PRESSED) {
            if (e.getX() < x || e.getX() > x + width || e.getY() < y || e.getY() > y + height) {
                hide();
            }
        }
    }
    protected abstract void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime);
}

package sence;

import controller.ImageController;
import menu.*;
import menu.Button;
import menu.Label;
import util.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuPopupScene extends PopupWindow {
    private BackgroundType.BackgroundImage backgroundImage;
    private Button back;

    public MenuPopupScene(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void sceneBegin() {
        backgroundImage = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-3.png"));
        back = new Button(50, 50, Theme.get(4));
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {

        this.backgroundImage.paintBackground(g, false, true, 190, 15, 1020, 820);
        back.paint(g);
    }

    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state != null) {
                switch (state) {
                    case MOVED -> isMove(back, e.getX(), e.getY());

                    case PRESSED -> {
                        isPress(back, e.getX(), e.getY());
                        if (this.back.getIsFocus()) {
                            hide();
                        }
                    }
                }
            }
            if (isShow() == false) {
                sceneEnd();
            }
        };
    }


    private boolean isOverLap(Label obj, int eX, int eY) {
        return eX <= obj.right() && eX >= obj.left() && eY >= obj.top() && eY <= obj.bottom();
    }

    private void isPress(Label obj, int eX, int eY) {
        if (isOverLap(obj, eX, eY)) {
            obj.isFocus();
            if (obj.getClickedAction() != null) {
                obj.clickedActionPerformed();
            }
        } else {
            obj.unFocus();
        }
    }

    //確認滑鼠移動判定
    private void isMove(Label obj, int eX, int eY) {
        if (isOverLap(obj, eX, eY)) {
            obj.isHover();
        } else {
            obj.unHover();
        }
    }

}

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
    private Button input;
    private Style style;

    public MenuPopupScene(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void sceneBegin() {
        backgroundImage = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-3.png"));
        this.style = new Style.StyleRect(300, 100, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/multiButton-3.png")));
        input = new Button(600, 400, style);
    }

    @Override
    public void sceneEnd() {
        this.input = null;
    }

    @Override
    public void paint(Graphics g) {
        this.backgroundImage.paintBackground(g, false, true, 200, 100, 1000, 650);
        this.input.paint(g);
    }

    @Override
    protected void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state != null) {
                switch (state) {
                    case PRESSED:
                        checkPress(e, this.input);
                        if (this.input.getIsFocus()) {
                            hide();
                        }
                        break;
                }
            }
            if (isShow() == false) {
                sceneEnd();
            }
        };
    }

    //確認滑鼠點擊判定
    public void checkPress(final MouseEvent e, final Label label) {
        if (e.getX() <= label.right() && e.getX() >= label.left() && e.getY() >= label.top() && e.getY() <= label.bottom()) {
            label.isFocus();
            if (label.getClickedAction() != null) {
                label.clickedActionPerformed();
            }
        } else {
            label.unFocus();
        }
    }

}

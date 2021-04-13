package sence;

import controller.ImageController;
import menu.*;
import menu.Button;
import menu.Label;
import menu.impl.MouseTriggerImpl;
import unit.CommandSolver;

import static unit.Global.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScene2 extends Scene {

    private menu.Label a;
    private Button b;
    private BackgroundType.BackgroundImage menuImg;

    @Override
    public void sceneBegin() {
        menuImg = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu-10.png"));

        a = new Label(413, 122);
        b = new Button(430, 410, Theme.get(0));
        b.setClickedActionPerformed((int x, int y) -> System.out.println("ClickedAction"));

        //使用格式：
        //第一行： new Label and set all the Style(normal & hover & focused )
        //第一行：set MouscCommandedListener and KeyListerner
        //一定要分開設定

        Style et = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(Color.YELLOW))
                .setHaveBorder(true)
                .setTextColor(Color.BLACK)
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);

        Style eHover = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(Color.WHITE))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5)
                .setTextColor(Color.BLACK)
                .setTextFont(new Font("", Font.BOLD, 20))
                .setText("HOVER");

        Style eNormal = new Style.StyleRect(200, 50, true, new BackgroundType.BackgroundColor(new Color(128, 128, 128)))
                .setHaveBorder(true)
                .setTextColor(Color.LIGHT_GRAY)
                .setText("請點擊")
                .setTextFont(new Font("", Font.BOLD, 20))
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);

//        this.ee = new EditText(430, 290, "請在此輸入");
//        ee.setStyleNormal(eNormal);
//        ee.setStyleHover(eHover);
//        ee.setStyleFocus(et);
//        ee.setEditLimit(10);   //設定文字輸入長度限制

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
            }

            @Override
            public void keyTyped(char c, long trigTime) {
//                ee.keyTyped(c);
            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(a, e, state);
            MouseTriggerImpl.mouseTrig(b, e, state);
//            MouseTriggerImpl.mouseTrig(ee, e, state);

        };
    }

    @Override
    public void paint(Graphics g) {
        menuImg.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        a.paint(g);
        b.paint(g);
//        ee.paint(g);
    }

    @Override
    public void update() {

    }

}

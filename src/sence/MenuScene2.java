package sence;

import controller.ImageController;
import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;
import menu.impl.MouseTriggerImpl;
import unit.CommandSolver;

import static unit.Global.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScene2 extends Scene {

    private Button singleMode;
    private Button multiplayer;
    private Label back;
    private Button normalMode;
    private Button limitMode;

    private BackgroundType.BackgroundImage menuImg;

    @Override
    public void sceneBegin() {
        menuImg = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-10.png"));
        singleMode = new Button(200, 25, Theme.get(0));
        multiplayer = new Button(800, 25, Theme.get(1));
        back = new Label(50, 50, Theme.get(4));
        normalMode = new Button(200, 25, Theme.get(3));
        limitMode = new Button(800, 25, Theme.get(2));


//        b.setClickedActionPerformed((int x, int y) -> System.out.println("ClickedAction"));

        //使用格式：
        //第一行： new Label and set all the Style(normal & hover & focused )
        //第一行：set MouscCommandedListener and KeyListerner
        //一定要分開設定

//        Style et = new Style.StyleRect(400, 800, true, new BackgroundType.BackgroundColor(Color.YELLOW))
//                .setHaveBorder(true)
//                .setTextColor(Color.BLACK)
//                .setTextFont(new Font("", Font.BOLD, 20))
//                .setBorderColor(Color.BLACK)
//                .setBorderThickness(5);
//
//        Style eHover = new Style.StyleRect(400, 800, true, new BackgroundType.BackgroundColor(Color.WHITE))
//                .setHaveBorder(true)
//                .setBorderColor(Color.BLACK)
//                .setBorderThickness(5)
//                .setTextColor(Color.BLACK)
//                .setTextFont(new Font("", Font.BOLD, 20))
//                .setText("HOVER");
//
//        Style eNormal = new Style.StyleRect(400, 800, true, new BackgroundType.BackgroundColor(new Color(128, 128, 128)))
//                .setHaveBorder(true)
//                .setTextColor(Color.LIGHT_GRAY)
//                .setText("請點擊")
//                .setTextFont(new Font("", Font.BOLD, 20))
//                .setBorderColor(Color.WHITE)
//                .setBorderThickness(5);
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
            MouseTriggerImpl.mouseTrig(singleMode, e, state);
            MouseTriggerImpl.mouseTrig(multiplayer, e, state);
            MouseTriggerImpl.mouseTrig(back, e, state);
            MouseTriggerImpl.mouseTrig(normalMode, e, state);
            MouseTriggerImpl.mouseTrig(limitMode, e, state);
            System.out.println(normalMode.IsUse(limitMode));
//            MouseTriggerImpl.mouseTrig(ee, e, state);
            limitMode.setClickedActionPerformed(new Label.ClickedAction() {
                @Override
                public void clickedActionPerformed(int x, int y) {
                    SenceController.getSenceController().change(new MapScene());
                }
            });
        };
    }

    @Override
    public void paint(Graphics g) {
        menuImg.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (!singleMode.IsUse(multiplayer)) {
            multiplayer.paint(g);
            singleMode.paint(g);
        } else {
            normalMode.paint(g);
            limitMode.paint(g);
        }
        back.paint(g);

//        ee.paint(g);
    }

    @Override
    public void update() {
    }

}

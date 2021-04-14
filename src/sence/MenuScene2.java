package sence;

import controller.ImageController;

import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;

import unit.CommandSolver;

import static unit.Global.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuScene2 extends Scene {

    private ArrayList<Label> labels;
    private Button singleMode;
    private Button multiplayer;
    private Button back;
    private Button normalMode;
    private Button limitMode;
    private boolean isSecond;
    private BackgroundType.BackgroundImage menuImg;

    @Override
    public void sceneBegin() {
        menuImg = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-10.png"));
        labels = new ArrayList<>();
        singleMode = new Button(200, 25, Theme.get(0));
        multiplayer = new Button(800, 25, Theme.get(1));
        labels.add(singleMode);
        labels.add(multiplayer);
        isSecond = false;

//        b.setClickedActionPerformed((int x, int y) -> System.out.println("ClickedAction"));
        //使用格式：
        //第一行： new Label and set all the Style(normal & hover & focused )
        //第一行：set MouseListener and KeyListener
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

    private boolean isOverLao(Label obj, int eX, int eY) {
        return eX <= obj.right() && eX >= obj.left() && eY >= obj.top() && eY <= obj.bottom();
    }

    private void isMove(Label obj, int eX, int eY) {
        if (isOverLao(obj, eX, eY)) {
            obj.isHover();
        } else {
            obj.unHover();
        }
    }

    private void isPress(Label obj, int eX, int eY) {
        if (isOverLao(obj, eX, eY)) {
            obj.isFocus();
            if (obj.getClickedAction() != null) {
                obj.clickedActionPerformed();
            }
        } else {
            obj.unFocus();
        }
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

    private void addSecond() {
        normalMode = new Button(200, 25, Theme.get(2));
        limitMode = new Button(800, 25, Theme.get(3));
        back = new Button(50, 50, Theme.get(4));
        labels.add(normalMode);
        labels.add(limitMode);
        labels.add(back);
        isSecond = true;
    }

    private void release(){
        for(int i=0;i<labels.size() ; i++){
            labels.get(i).unFocus();
        }
    }


    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (state != null) {
                switch (state) {
                    case MOVED -> {
                        for (int i = 0; i < labels.size(); i++) {
                            isMove(labels.get(i), e.getX(), e.getY());
                        }
                    }
                    case PRESSED -> {
                        if (isSecond) {
                            for (int i = 2; i < labels.size(); i++) {
                                isPress(labels.get(i), e.getX(), e.getY());
                            }
                            if (back.getIsFocus()) {
                                isSecond = false;
                                return;
                            }
                            if (limitMode.IsUse(normalMode)) {
                                SenceController.getSenceController().change(new MapScene());
                            }
                            return;
                        }
                        for (int i = 0; i < labels.size(); i++) {
                            isPress(labels.get(i), e.getX(), e.getY());
                        }
                        if (singleMode.IsUse(multiplayer)) {
                            addSecond();
                        }
                        release();
                    }

                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        menuImg.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (isSecond) {
            back.paint(g);
            normalMode.paint(g);
            limitMode.paint(g);
            return;
        }
        multiplayer.paint(g);
        singleMode.paint(g);
    }

    @Override
    public void update() {

    }
}
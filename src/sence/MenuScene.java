package sence;

import controller.ImageController;

import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;
import sence.gameScene.LimitModel;
import sence.gameScene.normalMode.NormalMode;
import util.CommandSolver;
import util.Delay;

import static util.Global.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class MenuScene extends Scene {
    private BackgroundType.BackgroundImage menuImg1;
    private ArrayList<Label> labels;
    private Button singleMode;
    private Button multiplayer;
    private Button back;
    private Button normalMode;
    private Button limitMode;
    private boolean isSecond;
    private BackgroundType.BackgroundImage menuImg2;
    private Delay delay;//給封面用的
    private MenuPopupScene multiPop; //跳出視窗
    private Button crateServer;
    private Button addServer;
    private EditText input;
    private boolean isSingle;
    private boolean isNormal;

    @Override
    public void sceneBegin() {
        menuImg1 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-1.png"));
        menuImg2 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-2.png"));
        initTheme();
        labels = new ArrayList<>();
        delay = new Delay(120);
        delay.play();
        singleMode = new Button(200, 25, Theme.get(0));
        multiplayer = new Button(800, 25, Theme.get(1));
        labels.add(singleMode);
        labels.add(multiplayer);
        stateUpdate();
        isSecond = false;
        this.multiPop = new MenuPopupScene(200, 100, 1000, 650);//代表碰撞即點及位置

//        b.setClickedActionPerformed((int x, int y) -> System.out.println("ClickedAction"));
        //使用格式：
        //第一行： new Label and set all the Style(normal & hover & focused )
        //第一行：set MouseListener and KeyListener
        //一定要分開設定

//        this.ee = new EditText(430, 290, "請在此輸入");
//        ee.setStyleNormal(eNormal);
//        ee.setStyleHover(eHover);
//        ee.setStyleFocus(et);
//        ee.setEditLimit(10);   //設定文字輸入長度限制
    }

    @Override
    public void sceneEnd() {
        menuImg1 = null;
        singleMode = null;
        multiplayer = null;
        labels.clear();
        back = null;
        normalMode = null;
        limitMode = null;
        menuImg2 = null;
        multiPop = null;
        crateServer = null;
        addServer = null;
        input = null;
    }

    //初始化主題
    private void initTheme() {
        Theme.add(setTheme(400, 800, "/menu/button-3.png"));
        Theme.add(setTheme(400, 800, "/menu/button-4.png"));
        Theme.add(setTheme(400, 800, "/menu/button-1.png"));
        Theme.add(setTheme(400, 800, "/menu/button-2.png"));
        Theme.add(setTheme(100, 50, "/menu/button-0.png"));
        Theme.add(setTheme(200, 400, "/menu/multiButton-1.png"));
        Theme.add(setTheme(200, 400, "/menu/multiButton-2.png"));
    }

    //設定主題
    public static Theme setTheme(int width, int height, String path) {
        return new Theme(Style.getGeneralStyle(width, height, path, true, Color.WHITE, 5)
                , Style.getGeneralStyle(width, height, path, true, new Color(255, 215, 0), 5)
                , Style.getGeneralStyle(width, height, path, true, Color.BLACK, 5));
    }


    //彈跳視窗
    public void stateUpdate() {
//       this.labels.get(0).setClickedActionPerformed((int x, int y) -> SceneController.getInstance().change(new SoloScene1()));
        this.labels.get(1).setClickedActionPerformed((int x, int y) -> {
            this.multiPop.sceneBegin();
            this.multiPop.show();
        });
        addThird();

    }

    private void sceneChange() {
        if (isSingle) {
            if (isNormal) {
                SenceController.getSenceController().change(new NormalMode());
                return;
            }
            SenceController.getSenceController().change(new LimitModel());
        }

    }

    //  碰撞
    private boolean isOverLap(Label obj, int eX, int eY) {
        return eX <= obj.right() && eX >= obj.left() && eY >= obj.top() && eY <= obj.bottom();
    }

    //確認滑鼠移動判定
    private void isMove(Label obj, int eX, int eY) {
        if (isOverLap(obj, eX, eY)) {
            obj.isHover();
        } else {
            obj.unHover();
        }
    }

    //確認滑鼠點擊判定
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

    //一般或極限模式地圖選擇
    private void addSecond() {
        normalMode = new Button(200, 25, Theme.get(2));
        limitMode = new Button(800, 25, Theme.get(3));
        back = new Button(50, 50, Theme.get(4));
        labels.add(normalMode);
        labels.add(limitMode);
        labels.add(back);
        isSecond = true;

    }

    //連線模式
    private void addThird() {
        crateServer = new Button(300, 200, Theme.get(5));
        addServer = new Button(900, 200, Theme.get(6));
        input = new EditText(600, 400, "請按Enter");
        input.setEditLimit(12);//設定文字輸入長度限制
        input.setCursorSpeed(10);
        input.setEditLimit(70);//游標閃爍位置
        labels.add(crateServer);
        labels.add(addServer);
        labels.add(input);
    }

    //返回釋放
    private void release() {
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).unFocus();
        }
    }

    //滑鼠監聽
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
                        if (MenuScene.this.multiPop.isShow()) {
                            MenuScene.this.multiPop.mouseListener().mouseTrig(e, state, trigTime);
                            for (int i = 5; i < labels.size(); i++) {
                                isPress(labels.get(i), e.getX(), e.getY());
                            }
                        }
                        if (isSecond) {
                            for (int i = 2; i < labels.size(); i++) {
                                isPress(labels.get(i), e.getX(), e.getY());
                            }
                            if (back.getIsFocus()) {
                                isSecond = false;
                                return;
                            }
                            if (normalMode.getIsFocus()) {
                                isNormal = true;

                            }
                            sceneChange();
                            return;
                        }
                        for (int i = 0; i < labels.size(); i++) {
                            isPress(labels.get(i), e.getX(), e.getY());
                        }

                        if (singleMode.getIsFocus()) {
                            isSingle = true;
                            addSecond();
                        }
                        release();
                    }
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(final int commandCode, final long trigTime) {
//                moveKey(commandCode); //偵測目前鍵盤位置
//                switch (commandCode) {
//                    case Global.ENTER:
//                        if (MenuScene.this.input.getIsFocus()) { //如果在輸入階段，按下Enter後則存成IP，並且input變成unFocus
//                            MenuScene.this.connectIP = MenuScene.this.input.getEditText();
//                            MenuScene.this.input.unFocus();
//                            try {
//                                ClientClass.getInstance().connect(MenuScene.this.connectIP, 12345); // ("SERVER端IP", "SERVER端PORT")
//                                SceneController.getInstance().change(new WaitScene());
//                            } catch (final IOException ex) {
//                                System.out.println("輸入錯誤");
//                            }
//                            break;
//                        }
//                }
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
    public void paint(Graphics g) {
        //delay才播放
        if (delay.isPlaying()) {
            menuImg1.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        } else {
            menuImg2.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            if (isSecond) {
                back.paint(g);
                normalMode.paint(g);
                limitMode.paint(g);
                return;
            }
            multiplayer.paint(g);
            singleMode.paint(g);
            if (multiPop.isShow()) {
                this.multiPop.paint(g);
                this.addServer.paint(g);
                this.crateServer.paint(g);
                if (this.addServer.getIsFocus()) {
                    this.input.paint(g);
                }
            }

        }
    }

    @Override
    public void update() {
        //更新倒數時間
        delay.count();
    }
}

package sence;

import controller.ImageController;

import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;
import sence.gameScene.LimitModel;
import util.CommandSolver;
import util.Delay;

import static util.Global.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuScene extends Scene {
    private BackgroundType.BackgroundImage menuImg1;//封面
    private BackgroundType.BackgroundImage menuImg2; //背景圖

    private Button singleMode;//單人
    private Button multiplayer;//多人
    private Button normalMode;//一般模式
    private Button limitMode;//極限
    private Button backToSec;//返回
    private Button crateServer;//創建房間
    private Button addServer;   //加入房間
    private EditText inputText;//輸入ip
    private Button backToTir;//返回

    private ArrayList<Label> labels;

    private boolean isSingle;//是不是單人
    private boolean isNormal;//是不是一般
    private boolean isCrate;  //是不是創建房間

    private State state;//此刻的模式絕 決定會出現哪些按鈕

    private Delay delay;//給封面用的
    private Style IpStyle;//輸入ip的模式


    @Override
    public void sceneBegin() {
        menuImg1 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-1.png"));
        menuImg2 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-2.png"));
        delay = new Delay(120);
        delay.play();

        initTheme();
        initStyle();
        addLabels();

        this.isCrate = false;
        isSingle = false;
        isNormal = false;
        state = State.SECOND;
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
        labels.clear();
        menuImg1 = null;
        menuImg2 = null;

        singleMode = null;
        multiplayer = null;
        backToSec = null;
        normalMode = null;
        limitMode = null;
        crateServer = null;
        addServer = null;
        inputText = null;
        backToTir = null;
    }

    //初始化主題
    private void initTheme() {
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/menu/button-1.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/menu/button-2.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/menu/button-3.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/menu/button-4.png"));
        Theme.add(setTheme(100, 50, "/menu/button-5.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/menu/button-6.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/menu/button-7.png"));
    }

    private void initStyle() {
        IpStyle = new Style.StyleRect(300, 100, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/IPButton.png")));
        inputText = new EditText(600, 400, "請按Enter", IpStyle);
        inputText.setEditLimit(12);//設定文字輸入長度限制
        inputText.setCursorSpeed(10);
        inputText.setEditLimit(20);//游標閃爍位置
    }

    //設定主題
    public static Theme setTheme(int width, int height, String path) {
        return new Theme(Style.getGeneralStyle(width, height, path, true, Color.WHITE, 5)
                , Style.getGeneralStyle(width, height, path, true, new Color(255, 215, 0), 5)
                , Style.getGeneralStyle(width, height, path, true, Color.BLACK, 5));
    }

    //加入所有按鈕
    private void addLabels() {
        singleMode = new Button(BUTTON_X1, BUTTON_Y, Theme.get(0));
        multiplayer = new Button(BUTTON_X2, BUTTON_Y, Theme.get(1));
        normalMode = new Button(BUTTON_X1, BUTTON_Y, Theme.get(2));
        limitMode = new Button(BUTTON_X2, BUTTON_Y, Theme.get(3));
        backToSec = new Button(50, BUTTON_Y, Theme.get(4));
        crateServer = new Button(BUTTON_X1, BUTTON_Y, Theme.get(5));
        addServer = new Button(BUTTON_X2, BUTTON_Y, Theme.get(6));
        backToSec = new Button(50, BUTTON_Y, Theme.get(4));

        labels.add(singleMode);
        labels.add(multiplayer);
        labels.add(normalMode);
        labels.add(limitMode);
        labels.add(backToSec);
        labels.add(crateServer);
        labels.add(addServer);
        labels.add(inputText);
        labels.add(backToTir);
    }

    //一般或極限模式地圖選擇

    //彈跳視窗
    private void changState() {
        singleMode.setClickedActionPerformed((x, y) -> {
            isSingle = true;
            state = State.THIRD;
        });
        multiplayer.setClickedActionPerformed((x, y) ->
                state = State.FOURTH);
        normalMode.setClickedActionPerformed((x, y) ->
                isNormal = true);
        backToSec.setClickedActionPerformed((x, y) ->
                state = State.SECOND
        );
        crateServer.setClickedActionPerformed((x, y) -> {
                    isCrate = true;
                    state = State.THIRD;
                }
        );
        backToTir.setClickedActionPerformed((x, y) ->
                state = State.SECOND
        );
//        addServer.setClickedActionPerformed((x, y) ->
//
//        );

//        addServer.setClickedActionPerformed((x, y) ->
//                );

    }

    //返回釋放
    private void release() {
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).unFocus();
        }
    }

    //換背景
    private void sceneChange() {
        if (isSingle) {
            if (isNormal) {
                SenceController.getSenceController().change(new ConnectScene());
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
                        changState();
                        switch (this.state) {
                            case SECOND -> {
                                isPress(singleMode, e.getX(), e.getY());
                            }
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
                MenuScene.this.inputText.keyTyped(c);
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
            switch (state) {
                case SECOND -> {
                    singleMode.paint(g);
                    multiplayer.paint(g);
                }
                case THIRD -> {
                    backToSec.paint(g);
                    normalMode.paint(g);
                    limitMode.paint(g);
                }
                case FOURTH -> {
                    backToTir.paint(g);
                    this.addServer.paint(g);
                    this.crateServer.paint(g);
                    if (!isCrate) {
                        this.inputText.paint(g);
                    }
                }
                case FIFTH -> {
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


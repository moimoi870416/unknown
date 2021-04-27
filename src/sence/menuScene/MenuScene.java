package sence.menuScene;

import client.ClientClass;
import controller.AudioResourceController;
import controller.ImageController;

import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;
import sence.ConnectScene;
import sence.Scene;
import sence.gameScene.LimitMode;
import sence.gameScene.normalMode.NormalMode;
import server.Server;
import util.CommandSolver;


import static util.Global.*;
import static util.Global.State.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuScene extends Scene {
    private BackgroundType.BackgroundImage menuImg0;//說明
    private BackgroundType.BackgroundImage menuImg1;//封面
    private BackgroundType.BackgroundImage menuImg2; //背景圖

    private Button enter;
    private Button enter2;
    private Button singleMode;//單人
    private Button multiplayer;//多人
    private Button normalMode;//一般模式
    private Button limitMode;//極限
    private Button crateServer;//創建房間
    private Button addServer;   //加入房間
    private EditText inputText;//輸入ip
    private Button backToFir;//返回狀態一
    private Button backToSec;//返回狀態二
    private Button backToFou;//返回四
    private String connectIP;
    private String buttonSound;

    private ArrayList<Label> labels;

    //    private boolean isSingle;//是不是單人
    private boolean isNormal;//是不是一般
    private boolean isAdd;  //是不是創建房間
    private boolean isCrate;

    private State ModeState;//此刻的模式 決定會出現哪些按鈕
    private Style IpStyle;//輸入ip的模式

    @Override
    public void sceneBegin() {
        menuImg0 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/menu-0.png"));
        menuImg1 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/menu-1.png"));
        menuImg2 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/menu-2.png"));
        this.labels = new ArrayList<>();
        initTheme();
        initStyle();
        addLabels();
        this.isAdd = false;
        isSingle = false;
        isNormal = true;
        isCrate = false;
        ModeState = FIRST;
        buttonSound = "/sounds/button01a.wav";
    }

    @Override
    public void sceneEnd() {
        labels.clear();
        menuImg0 = null;
        menuImg1 = null;
        menuImg2 = null;
        enter = null;
        enter2 = null;
        backToFir = null;
        singleMode = null;
        multiplayer = null;
        normalMode = null;
        limitMode = null;
        crateServer = null;
        addServer = null;
        inputText = null;
        backToFir = null;
        backToSec = null;
        backToFou = null;
        connectIP = null;
    }

    //設定主題
    public static Theme setTheme(int width, int height, String path) {
        return new Theme(Style.getGeneralStyle(width, height, path, true, Color.WHITE, 5)
                , Style.getGeneralStyle(width, height, path, true, new Color(255, 215, 0), 5)
                , Style.getGeneralStyle(width, height, path, true, Color.BLACK, 5));
    }

    //初始化主題
    public static void initTheme() {
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/pictures/menu/button-1.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/pictures/menu/button-2.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/pictures/menu/button-3.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/pictures/menu/button-4.png"));
        Theme.add(setTheme(100, 50, "/pictures/menu/button-5.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/pictures/menu/button-6.png"));
        Theme.add(setTheme(BUTTON_WIDTH, BUTTON_HEIGHT, "/pictures/menu/button-7.png"));
        Theme.add(setTheme(LABEL_WIDTH, LABEL_HEIGHT, "/pictures/menu/button-00.png"));
        Theme.add(setTheme(LABEL_WIDTH, LABEL_HEIGHT, "/pictures/menu/button-001.png"));
        Theme.add(setTheme(LABEL_WIDTH, LABEL_HEIGHT, "/pictures/menu/button-002.png"));
    }

    private void initStyle() {
        IpStyle = new Style.StyleRect(300, 100, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/IPButton2.png")));
        inputText = new EditText(825, 500, "", IpStyle);
        inputText.setEditLimit(12);//設定文字輸入長度限制
        inputText.setCursorColor(Color.black);
        inputText.setTranX(50);
    }


    //加入所有按鈕
    private void addLabels() {
        singleMode = new Button(BUTTON_X1, BUTTON_Y, Theme.get(0));
        multiplayer = new Button(BUTTON_X2, BUTTON_Y, Theme.get(1));
        normalMode = new Button(BUTTON_X1, BUTTON_Y, Theme.get(2));
        limitMode = new Button(BUTTON_X2, BUTTON_Y, Theme.get(3));

        crateServer = new Button(BUTTON_X1, BUTTON_Y, Theme.get(5));
        addServer = new Button(BUTTON_X2, BUTTON_Y, Theme.get(6));

        enter = new Button(1100, 700, Theme.get(7));
        enter2 = new Button(50, 700, Theme.get(8));
        backToFir = new Button(50, BUTTON_Y, Theme.get(4));
        backToSec = new Button(50, BUTTON_Y, Theme.get(4));
        backToFou = new Button(50, BUTTON_Y, Theme.get(4));

        this.labels.add(singleMode);
        this.labels.add(multiplayer);
        this.labels.add(normalMode);
        this.labels.add(limitMode);

        this.labels.add(crateServer);
        this.labels.add(addServer);
        this.labels.add(inputText);

        this.labels.add(enter);
        this.labels.add(enter2);
        this.labels.add(backToFir);
        this.labels.add(backToSec);
        this.labels.add(backToFou);
    }

    private void changState() {
        enter.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            ModeState = SECOND;
        });
        enter2.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            ModeState = ZERO;
        });
        backToFir.setClickedActionPerformed((x, y) -> {
                    AudioResourceController.getInstance().shot(buttonSound);
                    ModeState = FIRST;
                    backToFir.unFocus();
                    singleMode.unFocus();
                    multiplayer.unFocus();
                    enter.unFocus();
                }
        );
        singleMode.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
//            ModeState = THIRD;
            isSingle = true;
        });
        multiplayer.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            ModeState = FOURTH;
            isSingle = false;
        });
        backToSec.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            ModeState = SECOND;
            backToSec.unFocus();
            singleMode.unFocus();
            multiplayer.unFocus();
            inputText.unFocus();
            isAdd = false;
        });
        normalMode.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            isNormal = true;
        });
        backToFou.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            ModeState = FOURTH;
            backToFou.unFocus();
            crateServer.unFocus();
            addServer.unFocus();
            inputText.unFocus();
            isAdd = false;
            isServer = false;

        });
        crateServer.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            isCrate = true;
            isSingle = false;
            isServer = true;
            isAdd = false;
            inputText.unFocus();
            addServer.unFocus();
            crateConnectLanArea();
        });
        addServer.setClickedActionPerformed((x, y) -> {
            AudioResourceController.getInstance().shot(buttonSound);
            isAdd = true;
            inputText.isFocus();
            isServer = false;
            isSingle = false;
        });
    }

    //換背景
    private void multiSceneChange() {
        if (!isAdd && isCrate) {
            SenceController.getSenceController().change(new EnterScene(isSingle, isNormal, isAdd));
        }
    }

    private void singleSceneChange() {
        if (isSingle) {
            SenceController.getSenceController().change(new NormalMode());
        }
    }

    private boolean isOverLap(Label obj, int eX, int eY) {
        return eX <= obj.right() && eX >= obj.left() && eY >= obj.top() && eY <= obj.bottom();
    }

    private void isMove(Label obj, final MouseEvent e) {
        if (isOverLap(obj, e.getX(), e.getY())) {
            obj.isHover();
        } else {
            obj.unHover();
        }
    }

    private void isPress(Label obj, final MouseEvent e) {
        if (isOverLap(obj, e.getX(), e.getY())) {
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
        return (final MouseEvent e, final CommandSolver.MouseState state, final long trigTime) -> {
            if (state != null) {
                switch (state) {
                    case MOVED -> {
                        for (int i = 0; i < labels.size(); i++) {
                            isMove(labels.get(i), e);
                        }
                    }
                    case CLICKED -> {
                        changState();
                        switch (ModeState) {
                            case FIRST -> {
                                isPress(enter, e);
                                isPress(enter2, e);
                            }
                            case ZERO -> isPress(backToFir, e);
                            case SECOND -> {
                                isPress(backToFir, e);
                                isPress(singleMode, e);
                                isPress(multiplayer, e);
                                singleSceneChange();
                            }
//                            case THIRD -> {
//                                isPress(normalMode, e);
//                                isPress(limitMode, e);
//                                isPress(backToSec, e);
//                                if (singleMode.IsUse(limitMode)) {
//
//                                }
//                            }
                            case FOURTH -> {
                                isPress(backToSec, e);
                                isPress(crateServer, e);
                                isPress(addServer, e);
                                multiSceneChange();
                            }
//                            case FIFTH -> {
//                                isPress(backToFou, e);
//                                isPress(normalMode, e);
//                                isPress(limitMode, e);
////                                isPress(inputText, e);
//                                if (normalMode.IsUse(limitMode)) {
//
//                                }
//                            }
                        }
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
                connectIP = inputText.getEditText();
                if (commandCode == Active.ENTER.getCommandCode()) {
//                    if (inputText.getIsFocus()) { //如果在輸入階段，按下Enter後則存成IP，並且input變成unFocus
                    if (IS_DEBUG) {
                        connectIP = "192.168.1.16";
                        addConnectLanArea();
                        return;
                    }
                    inputText.unFocus();
                    inputText.setEditText("");
                    addConnectLanArea();
                }
            }
//            }

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
        menuImg2.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        switch (ModeState) {
            case FIRST -> {
                menuImg1.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                enter.paint(g);
                enter2.paint(g);
            }
            case ZERO -> {
                menuImg0.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                backToFir.paint(g);
            }

            case SECOND -> {
                backToFir.paint(g);
                singleMode.paint(g);
                multiplayer.paint(g);
            }
//            case THIRD -> {
//                backToSec.paint(g);
//                normalMode.paint(g);
//                limitMode.paint(g);
//            }
            case FOURTH -> {
                backToSec.paint(g);
                addServer.paint(g);
                crateServer.paint(g);
                if (isAdd) {
                    this.inputText.paint(g);
                }
            }
//            case FIFTH -> {
//                backToFou.paint(g);
//                limitMode.paint(g);
//                normalMode.paint(g);
//            }
        }
    }

    @Override
    public void update() {
    }

    private void crateConnectLanArea() {
        Server.instance().create(12345);
        Server.instance().start();
        System.out.println(Server.instance().getLocalAddress()[0]);
        try {
            ClientClass.getInstance().connect("127.0.0.1", 12345); // ("SERVER端IP", "SERVER端PORT")
        } catch (IOException ex) {
            Logger.getLogger(ConnectScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addConnectLanArea() {
        try {
            ClientClass.getInstance().connect(connectIP, 12345); // ("SERVER端IP", "SERVER端PORT")
            SenceController.getSenceController().change(new EnterScene(isSingle, isNormal, isAdd));
        } catch (IOException ex) {
            addServer.unFocus();
            isAdd = false;
            Logger.getLogger(ConnectScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

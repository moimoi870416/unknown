package sence;

import controller.ImageController;

import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;
import util.CommandSolver;
import util.Delay;

import static util.Global.*;
import static util.Global.State.*;

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
    private Button backToSec;//返回狀態二
    private Button crateServer;//創建房間
    private Button addServer;   //加入房間
    private EditText inputText;//輸入ip
    private Button backToFou;//返回

    private ArrayList<Label> labels;

    private boolean isSingle;//是不是單人
    private boolean isNormal;//是不是一般
    private boolean isAdd;  //是不是創建房間

    private State ModeState;//此刻的模式 決定會出現哪些按鈕

    private Style IpStyle;//輸入ip的模式


    @Override
    public void sceneBegin() {
        menuImg1 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-1.png"));
        menuImg2 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-2.png"));
        this.labels = new ArrayList<>();
        initTheme();
        initStyle();
        addLabels();
        this.isAdd = false;
        isSingle = false;
        isNormal = false;
        ModeState = FIRST;
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
        backToFou = null;
    }

    //設定主題
    public static Theme setTheme(int width, int height, String path) {
        return new Theme(Style.getGeneralStyle(width, height, path, true, Color.WHITE, 5)
                , Style.getGeneralStyle(width, height, path, true, new Color(255, 215, 0), 5)
                , Style.getGeneralStyle(width, height, path, true, Color.BLACK, 5));
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
        inputText = new EditText(1100, 700, "請按Enter", IpStyle);
        inputText.setEditLimit(12);//設定文字輸入長度限制
        inputText.setCursorSpeed(10);
        inputText.setEditLimit(20);//游標閃爍位置
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
        backToFou = new Button(50, BUTTON_Y, Theme.get(4));
        this.labels.add(singleMode);
        this.labels.add(multiplayer);
        this.labels.add(normalMode);
        this.labels.add(limitMode);
        this.labels.add(backToSec);
        this.labels.add(crateServer);
        this.labels.add(addServer);
        this.labels.add(inputText);
        this.labels.add(backToFou);
    }

    private void changState() {
        singleMode.setClickedActionPerformed((x, y) -> {
            isSingle = true;
            ModeState = State.THIRD;
        });
        multiplayer.setClickedActionPerformed((x, y) ->
                ModeState = State.FOURTH);
        backToSec.setClickedActionPerformed((x, y) -> {
                    backToSec.unFocus();
                    singleMode.unFocus();
                    multiplayer.unFocus();
                    inputText.unFocus();
                    ModeState = SECOND;
                    isAdd = false;

                }
        );
        normalMode.setClickedActionPerformed((x, y) ->
                isNormal = true);

        backToFou.setClickedActionPerformed((x, y) -> {
                    backToFou.unFocus();
                    crateServer.unFocus();
                    addServer.unFocus();
                    ModeState = FOURTH;
                    inputText.unFocus();
                    isAdd = false;
                }
        );
        crateServer.setClickedActionPerformed((x, y) -> {
                    ModeState = FIFTH;
                }
        );
        addServer.setClickedActionPerformed((x, y) -> {
                    isAdd = true;
                    inputText.isFocus();
                }
        );
    }

    //換背景
    private void sceneChange() {
        if (isSingle) {
            if (isNormal) {
                SenceController.getSenceController().change(new ConnectScene());
                return;
            }
            SenceController.getSenceController().change(new ConnectScene());
        }
        if (isNormal) {
            SenceController.getSenceController().change(new ConnectScene());
            return;

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
                            case SECOND -> {
                                isPress(singleMode, e);
                                isPress(multiplayer, e);
                            }
                            case THIRD -> {
                                isPress(normalMode, e);
                                isPress(limitMode, e);
                                isPress(backToSec, e);
                                sceneChange();
                            }
                            case FOURTH -> {
                                isPress(backToSec, e);
                                isPress(crateServer, e);
                                isPress(addServer, e);

                            }
                            case FIFTH -> {
                                isPress(backToFou, e);
                                isPress(normalMode, e);
                                isPress(limitMode, e);
                                isPress(inputText, e);
                            }
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

        menuImg2.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        switch (ModeState) {
            case FIRST -> {
                menuImg1.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            }
//                case ZERO ->i
//
//
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
                backToSec.paint(g);
                addServer.paint(g);
                crateServer.paint(g);
                if (isAdd) {
                    this.inputText.paint(g);
                }
            }
            case FIFTH -> {
                backToFou.paint(g);
                limitMode.paint(g);
                normalMode.paint(g);
            }
        }
    }

    @Override
    public void update() {
    }

}


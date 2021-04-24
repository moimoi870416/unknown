package sence.menuScene;

import client.ClientClass;
import client.CommandReceiver;
import controller.ConnectController;
import controller.ImageController;
import menu.BackgroundType;

import menu.Button;
import menu.Label;
import menu.Style;
import menu.Theme;
import object.actor.GameActor;

import sence.Scene;

import util.CommandSolver;
import util.Global;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static util.Global.*;

public class EnterScene extends Scene {
    private BackgroundType.BackgroundImage menuImg2; //背景圖
    private Style playStyle1Light;//人物1
    private Style playStyle2Light;//人物2的亮圖
    private Style playStyle2Drank;//人物2的暗圖
    private Style playStyle3Light;//人物3的亮圖
    private Style playStyle3Drank;//人物3的暗圖
    private Label play1;
    private Label play2;
    private Label play3;

    private ArrayList<GameActor> gameActorArr;
    private boolean isSingle;
    private boolean isNormal;
    private boolean isAdd;
    private int playerCount;
    private Button start;

    public EnterScene(boolean isSingle, boolean isNormal, boolean isAdd) {
        this.isSingle = isSingle;
        this.isNormal = isNormal;
        this.isAdd = isAdd;
        gameActorArr = new ArrayList<>();
        playerCount = 0;
        gameActorArr.add(new GameActor(Global.Actor.FIRST, 500, 500));
        gameActorArr.get(playerCount++).setConnectID(ClientClass.getInstance().getID());
    }

    private void initStyle() {
        this.start = new Button(1100, 720, Theme.get(9));
        playStyle1Light = new Style.StyleRect(325, 600, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/play-1.png")));
        playStyle2Light = new Style.StyleRect(325, 600, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/play-2.png")));
        playStyle3Light = new Style.StyleRect(325, 600, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/Play-3.png")));
        playStyle2Drank = new Style.StyleRect(325, 600, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/play-2Drank.png")));
        playStyle3Drank = new Style.StyleRect(325, 600, true,
                new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/play-3Drank.png")));
        play1 = new Label(100, 90, playStyle1Light);
        if (isServer) {
            play2 = new Label(550, 90, playStyle2Drank);
            play3 = new Label(1000, 90, playStyle3Drank);
            return;
        }
        play2 = new Label(550, 90, playStyle2Light);
        play3 = new Label(1000, 90, playStyle3Drank);
    }

    @Override
    public void sceneBegin() {
        menuImg2 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/menu-2.png"));
        initStyle();
        start.setClickedActionPerformed((x, y) -> {
            if(isServer) {
                ConnectController.getInstance().changeSceneSend(isNormal);
            }
        });

    }

    @Override
    public void sceneEnd() {
        menuImg2 = null;
        play1 = null;
        play2 = null;
        play3 = null;
        start = null;

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
            }
        };
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state != null) {
                switch (state) {
                    case MOVED -> isMove(start, e);
                    case PRESSED -> isPress(start, e);
                }
            }

        };
    }

    @Override
    public void paint(Graphics g) {
        menuImg2.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if (isServer) {
            start.paint(g);
        }
        play1.paint(g);
        play2.paint(g);
        play3.paint(g);

    }

    @Override
    public void update() {
        ClientClass.getInstance().sent(NetEvent.CONNECT, null);
        connectUpdate();
    }


    protected void connectUpdate() {
        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int commandCode, ArrayList<String> strs) {


                if(commandCode == Global.NetEvent.EVENT_CHANGE_SCENE){
                    ConnectController.getInstance().changeSceneReceive(strs,gameActorArr);
                }
                if(serialNum == gameActorArr.get(0).getConnectID()){
                    return;
                }
                switch (commandCode) {
                    case Global.NetEvent.CONNECT: //自行定義所接收之指令代碼需要做什麼任務
                        if (playerCount >= 3) {
                            break;
                        }
                        boolean isBorn = false;
                        for (int i = 0; i < gameActorArr.size(); i++) {
                            if (gameActorArr.get(i).getConnectID() == serialNum) {
                                isBorn = true;
                                break;
                            }
                        }
                        if (!isBorn) {
                            gameActorArr.add(new GameActor(Global.Actor.values()[playerCount],0, 0));
                            gameActorArr.get(playerCount++).setConnectID(serialNum);
                        }
                        break;
                    case Global.NetEvent.EVENT_CHANGE_SCENE:
                        ConnectController.getInstance().changeSceneReceive(strs, gameActorArr);

                }
            }
        });
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

}

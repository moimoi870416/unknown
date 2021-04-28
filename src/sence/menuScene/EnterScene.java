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
    private Image play1o;
    private Image play1l;
    private Image play2l;
    private Image play2d;
    private Image play2o;
    private Image play3o;
    private Image play3l;
    private Image play3d;

    private ArrayList<GameActor> gameActorArr;
    private boolean isSingle;
    private boolean isNormal;
    private boolean isAdd;
    private int playerCount;
    private Button start;
    private boolean change;

    public EnterScene(boolean isSingle, boolean isNormal, boolean isAdd) {
        this.isSingle = isSingle;
        this.isNormal = isNormal;
        this.isAdd = isAdd;
        gameActorArr = new ArrayList<>();
        playerCount = 0;
        gameActorArr.add(new GameActor(Global.Actor.FIRST, 500, 500));
        gameActorArr.get(playerCount++).setConnectID(ClientClass.getInstance().getID());
        change = false;
    }

    @Override
    public void sceneBegin() {
        menuImg2 = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/pictures/menu/menu-2.png"));
        addImg();
        this.start = new Button(1100, 720, Theme.get(9));
        start.setClickedActionPerformed((x, y) -> {
            if (isServer && !change) {

                ConnectController.getInstance().changeSceneSend(isNormal);
                change = true;
            }
        });

    }

    @Override
    public void sceneEnd() {
        menuImg2 = null;
        start = null;
        play1o = null;
        play1l = null;
        play2l = null;
        play2d = null;
        play2o = null;
        play3l = null;
        play3o = null;
        play3d = null;
    }

    private void addImg() {
        play1l = ImageController.getInstance().tryGet("/pictures/menu/play-1.png");
        play1o = ImageController.getInstance().tryGet("/pictures/menu/play-1own.png");
        play2l = ImageController.getInstance().tryGet("/pictures/menu/play-2.png");
        play2o = ImageController.getInstance().tryGet("/pictures/menu/play-2own.png");
        play2d = ImageController.getInstance().tryGet("/pictures/menu/play-2Drank.png");
        play3l = ImageController.getInstance().tryGet("/pictures/menu/play-3.png");
        play3o = ImageController.getInstance().tryGet("/pictures/menu/play-3own.png");
        play3d = ImageController.getInstance().tryGet("/pictures/menu/play-3Drank.png");
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode == Active.ENTER.getCommandCode()) {
                    if(isServer) {
                        ConnectController.getInstance().changeSceneSend(isNormal);
                    }
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if(commandCode == Active.F1.getCommandCode()){
                    I_AM_MAC = true;
                }
                if(commandCode == Active.F2.getCommandCode()){
                    I_AM_MAC = false;
                }
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
        switch (playerCount) {
            case 1 -> {
                g.drawImage(play1o, 100, 90, null);
                g.drawImage(play2d, 550, 90, null);
                g.drawImage(play3d, 1000, 90, null);
            }
            case 2 -> {
                if (isServer) {
                    g.drawImage(play1o, 100, 90, null);
                    g.drawImage(play2l, 550, 90, null);
                    g.drawImage(play3d, 1000, 90, null);
                    return;
                }
                g.drawImage(play1l, 100, 90, null);
                g.drawImage(play2o, 550, 90, null);
                g.drawImage(play3d, 1000, 90, null);
            }
            case 3 -> {
                switch (gameActorArr.get(0).getConnectID()) {
                    case 100 -> {
                        g.drawImage(play1o, 100, 90, null);
                        g.drawImage(play2l, 550, 90, null);
                        g.drawImage(play3l, 1000, 90, null);
                    }
                    case 101 -> {
                        g.drawImage(play1l, 100, 90, null);
                        g.drawImage(play2o, 550, 90, null);
                        g.drawImage(play3l, 1000, 90, null);
                    }
                    case 102 -> {
                        g.drawImage(play1l, 100, 90, null);
                        g.drawImage(play2l, 550, 90, null);
                        g.drawImage(play3o, 1000, 90, null);
                    }
                }
            }
        }
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

                if (commandCode == Global.NetEvent.EVENT_CHANGE_SCENE) {
                    ConnectController.getInstance().changeSceneReceive(strs, gameActorArr);
                }
                if (serialNum == gameActorArr.get(0).getConnectID()) {
                    return;
                }
                switch (commandCode) {
                    case Global.NetEvent.CONNECT: //自行定義所接收之指令代碼需要做什麼任務
                        if (playerCount >= 4) {
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
                            gameActorArr.add(new GameActor(Global.Actor.values()[playerCount], 0, 0));
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

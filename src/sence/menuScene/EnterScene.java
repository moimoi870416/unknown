package sence.menuScene;

import client.ClientClass;
import client.CommandReceiver;
import controller.ConnectController;
import controller.SenceController;
import menu.Style;
import object.actor.GameActor;
import sence.ConnectScene;
import sence.Scene;
import sence.gameScene.LimitMode;
import sence.gameScene.normalMode.NormalMode;
import server.Server;
import util.CommandSolver;
import util.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Global.isServer;
import static util.Global.isSingle;

public class EnterScene extends Scene {
    private Style playStyle1Light;//人物的
    private Style playStyle2Drank;//人物的
    private Style playStyle2Light;//人物的
    private Style playStyle3Light;//人物的
    private Style playStyle3Drank;//人物的

    private ArrayList<GameActor> gameActorArr;
    private boolean isSingle;
    private boolean isNormal;
    private boolean isAdd;
    private int playerCount;

    public EnterScene(boolean isSingle,boolean isNormal,boolean isAdd){
        this.isSingle = isSingle;
        this.isNormal = isNormal;
        this.isAdd = isAdd;
        gameActorArr = new ArrayList<>();
        playerCount = 0;
        gameActorArr.add(new GameActor(Global.Actor.FIRST,500,500));
        gameActorArr.get(playerCount++).setConnectID(ClientClass.getInstance().getID());
        ArrayList<String> strs = new ArrayList<>();
        ClientClass.getInstance().sent(Global.NetEvent.CONNECT,strs);

    }

    //設定圖片
    public Style seStyle(int width, int height, String path) {
        return new Style.StyleRect(width, height, path);
    }

    @Override
    public void sceneBegin() {


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
                if(commandCode == Global.Active.ENTER.getCommandCode()){
                    if(isServer) {
                        ConnectController.getInstance().changeSceneSend(isNormal);
                    }
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
        };
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {
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
                switch (commandCode){
                    case  Global.NetEvent.CONNECT: //自行定義所接收之指令代碼需要做什麼任務
                        if (playerCount >= 3){
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
                            gameActorArr.add(new GameActor(Global.Actor.values()[playerCount],0,
                                    0));
                            gameActorArr.get(playerCount++).setConnectID(serialNum);
                        }
                        break;

                }
            }
        });
    }

}

package sence;

import client.ClientClass;
import client.CommandReceiver;
import object.actor.GameActor;
import server.Server;
import controller.ConnectController;
import util.Global;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Global.*;

public abstract class ConnectScene extends GameScene{
    private int playerCount;


    @Override
    protected void sceneBeginComponent() {
        gameSceneBegin();
        connectLanArea();
        playerCount = 0;
        gameActorArr.add(new GameActor(Actor.values()[playerCount],500,500));
        gameActorArr.get(playerCount++).setConnectID(ClientClass.getInstance().getID());

    }

    protected abstract void gameSceneBegin();

    @Override
    protected void sceneEndComponent() {
        gameSceneEnd();
    }

    protected abstract void gameSceneEnd();

    @Override
    protected void connectUpdate() {
        if(isSingle){
            return;
        }
        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int commandCode, ArrayList<String> strs) {
                if(serialNum == gameActorArr.get(0).getConnectID()){
                    return;
                }
                switch (commandCode){
                    case  NetEvent.CONNECT: //自行定義所接收之指令代碼需要做什麼任務

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
                            gameActorArr.add(new GameActor(Actor.values()[playerCount], Integer.parseInt(strs.get(0)),
                                    Integer.parseInt(strs.get(1))));
                            gameActorArr.get(playerCount++).setConnectID(serialNum);
                        }
                        break;
                    case NetEvent.ACTOR:
                        ConnectController.getInstance().actorReceive(gameActorArr,serialNum,strs);
                            break;
                    case NetEvent.BULLET_NEW:
                        ConnectController.getInstance().newBulletReceive(testBullets,strs);
                        break;
                    case NetEvent.ACTOR_HEAL:
                        ConnectController.getInstance().healReceive(gameActorArr,serialNum,strs);
                        break;
                    case NetEvent.ACTOR_FLASH:
                        ConnectController.getInstance().flashReceive(gameActorArr,serialNum,strs);
                        break;
                    case NetEvent.ACTOR_CHANGE_GUN:
                        ConnectController.getInstance().changeGunReceive(gameActorArr,serialNum,strs);
                        break;
                    case NetEvent.MONSTER_NEW:
                        ConnectController.getInstance().newMonsterReceive(monster,strs);
                        break;
                    case NetEvent.MONSTER_BOSS_ATTACK_TYPE:
                        ConnectController.getInstance().bossAtkTypeReceive(monster.getLast(),strs);
                        break;
                }
            }
        });
    }

    public void connectLanArea(){
        Scanner sc = new Scanner(System.in);

        System.out.println("創建伺服器 => 1, 連接其他伺服器 => 2");
        int opt = sc.nextInt();
        switch (opt) {
            case 1:
                Global.isServer = true;
                Server.instance().create(12345);
                Server.instance().start();
                System.out.println(Server.instance().getLocalAddress()[0]);
                try {
                    ClientClass.getInstance().connect("127.0.0.1", 12345); // ("SERVER端IP", "SERVER端PORT")
                } catch (IOException ex) {
                    Logger.getLogger(ConnectScene.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                System.out.println("請輸入主伺服器IP:");
                try {
                    ClientClass.getInstance().connect(sc.next(), 12345); // ("SERVER端IP", "SERVER端PORT")
                } catch (IOException ex) {
                    Logger.getLogger(ConnectScene.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }

    }

}

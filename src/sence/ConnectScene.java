package sence;

import client.ClientClass;
import client.CommandReceiver;
import controller.ConnectController;
import java.util.ArrayList;


import static util.Global.*;

public abstract class ConnectScene extends GameScene {


    @Override
    protected void sceneBeginComponent() {

        gameSceneBegin();

    }

    protected abstract void gameSceneBegin();

    @Override
    protected void sceneEndComponent() {
        gameSceneEnd();
    }

    protected abstract void gameSceneEnd();

    @Override
    protected void connectUpdate() {
        if (isSingle) {
            return;
        }
        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int commandCode, ArrayList<String> strs) {
                if(commandCode == NetEvent.EVENT_CHANGE_BOSS_SCENE){
                    ConnectController.getInstance().changeBossSceneReceive(gameActorArr);
                }
                if (serialNum == gameActorArr.get(0).getConnectID()) {
                    return;
                }
                switch (commandCode) {
                    case NetEvent.ACTOR -> ConnectController.getInstance().actorReceive(gameActorArr, strs);
                    case NetEvent.BULLET_NEW -> ConnectController.getInstance().newBulletReceive(testBullets, strs);
                    case NetEvent.ACTOR_HEAL -> ConnectController.getInstance().healReceive(gameActorArr, serialNum, strs);
                    case NetEvent.ACTOR_FLASH -> ConnectController.getInstance().flashReceive(gameActorArr, serialNum, strs);
                    case NetEvent.MONSTER_NEW -> ConnectController.getInstance().newMonsterReceive(monster, strs);
                    case NetEvent.MONSTER_BOSS_ATTACK_TYPE -> ConnectController.getInstance().bossAtkTypeReceive(monster, strs);
                    case NetEvent.MONSTER -> ConnectController.getInstance().monsterReceive(monster, strs);
                    case NetEvent.MONSTER_DEAD -> ConnectController.getInstance().monsterDeadReceive(monster, strs);
                    case NetEvent.MONSTER_STATE -> ConnectController.getInstance().monsterStateReceive(monster,strs);
                    case NetEvent.ACTOR_LYSU -> ConnectController.getInstance().lysuReceive(gameActorArr,strs);

                }
            }
        });
    }
}

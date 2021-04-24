package sence;

import client.ClientClass;
import client.CommandReceiver;
import object.actor.GameActor;
import controller.ConnectController;
import java.util.ArrayList;


import static util.Global.*;

public abstract class ConnectScene extends GameScene{


    @Override
    protected void sceneBeginComponent() {
        gameSceneBegin();
        ArrayList<GameActor> test = new ArrayList<>();
        if(!isSingle){
            for(int i=0 ; i<gameActorArr.size() ; i++){
                switch (gameActorArr.get(i).getConnectID()){
                    case 100 -> test.add(new GameActor(Actor.FIRST,500,500));
                    case 101 -> test.add(new GameActor(Actor.SECOND,500,510));
                    case 102 -> test.add(new GameActor(Actor.THIRD,500,500));
                }
            }
            gameActorArr = test;
        }
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

}

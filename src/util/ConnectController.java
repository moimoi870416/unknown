package util;

import client.ClientClass;
import static util.Global.*;

import object.GameObjForAnimator;
import object.actor.GameActor;
import object.monster.Monster;
import weapon.Bullet;

import java.util.ArrayList;

public class ConnectController {
    private static ConnectController connectController;

    private ConnectController() {
    }

    public static ConnectController getInstance() {
        if (connectController == null) {
            connectController = new ConnectController();
        }
        return connectController;
    }

    public void actorSend(GameActor gameActor){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.collider().left() + "");//0
        strs.add(gameActor.collider().top() + "");//1
        strs.add(gameActor.getLife() + "");//2
        strs.add(gameActor.getState() + "");//3
        strs.add(gameActor.getDir() + "");//4
        ClientClass.getInstance().sent(NetEvent.CONNECT, strs);
        ClientClass.getInstance().sent(NetEvent.ACTOR_MOVE, strs);
    }

    public void actorReceive(ArrayList<GameActor> gameActorArr,int serialNum,ArrayList<String> strs){
        for(int i=0 ; i<gameActorArr.size() ; i++){
            if(gameActorArr.get(i).getConnectID() == serialNum){
                gameActorArr.get(i).offSetX(Integer.valueOf(strs.get(0)));
                gameActorArr.get(i).offSetY(Integer.valueOf(strs.get(1)));
                gameActorArr.get(i).offLife(Integer.valueOf(strs.get(2)));
                gameActorArr.get(i).setState(GameObjForAnimator.State.valueOf(strs.get(3)));
                gameActorArr.get(i).setDir(GameObjForAnimator.Dir.valueOf(strs.get(4)));
            }
        }
    }

    public void bulletSend(Bullet bullet){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(bullet.left() + "");
        strs.add(bullet.top() + "");
        strs.add(bullet.getAtk() + "");
        strs.add(bullet.getState() + "");
        strs.add(bullet.getPenetration() + "");
        ClientClass.getInstance().sent(NetEvent.BULLET, strs);
    }

    public void bulletReceive(){

    }

    public void monsterSend(Monster monster){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(monster.collider().left() + "");
        strs.add(monster.collider().top() + "");
        strs.add(monster.getLife() + "");
        strs.add(monster.getState() + "");
        strs.add(monster.getDir() + "");
        ClientClass.getInstance().sent(NetEvent.MONSTER, strs);
    }

    public void monsterReceive(){

    }


}

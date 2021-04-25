package controller;

import client.ClientClass;
import static util.Global.*;

import object.GameObjForAnimator;
import object.actor.GameActor;
import object.monster.*;
import sence.GameScene;
import sence.gameScene.LimitMode;
import sence.gameScene.normalMode.BossScene;
import sence.gameScene.normalMode.NormalMode;
import weapon.Bullet;
import weapon.Gun;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

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

    public void actorSend(GameActor gameActor,int mouseX,int mouseY){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.collider().left() + "");//0
        strs.add(gameActor.collider().top() + "");//1
        strs.add(gameActor.getLife() + "");//2
        strs.add(gameActor.getState() + "");//3
        strs.add(gameActor.getDir() + "");//4
        strs.addAll(gunSend(gameActor,mouseX,mouseY));
        ClientClass.getInstance().sent(NetEvent.CONNECT, strs);
        ClientClass.getInstance().sent(NetEvent.ACTOR, strs);
    }



    public void changeGunSend(GameActor gameActor, int commandCode){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.getConnectID() + "");//0
        strs.add(commandCode + "");//1
        ClientClass.getInstance().sent(NetEvent.ACTOR_CHANGE_GUN, strs);
    }

    public void healSend(GameActor gameActor){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.getConnectID() + "");
        ClientClass.getInstance().sent(NetEvent.ACTOR_HEAL, strs);
    }

    public void flashSend(GameActor gameActor,int mouseX,int mouseY){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.getConnectID() + "");
        strs.add(mouseX + "");
        strs.add(mouseY + "");
        ClientClass.getInstance().sent(NetEvent.ACTOR_FLASH, strs);
    }


    public void actorReceive(ArrayList<GameActor> gameActorArr,int serialNum,ArrayList<String> strs){
        for(int i=1 ; i<gameActorArr.size() ; i++){
            if(gameActorArr.get(i).getConnectID() == serialNum){
                gameActorArr.get(i).offSetX(Integer.valueOf(strs.get(0)));
                gameActorArr.get(i).offSetY(Integer.valueOf(strs.get(1)));
                gameActorArr.get(i).setLife(Integer.valueOf(strs.get(2)));
                gameActorArr.get(i).setState(GameObjForAnimator.State.valueOf(strs.get(3)));
                gameActorArr.get(i).setDir(GameObjForAnimator.Dir.valueOf(strs.get(4)));
                gameActorArr.get(i).getBlood().barUpdate(Integer.valueOf(strs.get(0)),
                                                         Integer.valueOf(strs.get(1)),
                                                         Integer.valueOf(strs.get(2)));
                gunReceive(gameActorArr.get(i),strs);

            }
        }
    }

    private ArrayList<String> gunSend(GameActor gameActor,int mouseX,int mouseY){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.collider().centerX() + "");//5
        strs.add(gameActor.collider().bottom() + "");//6
        strs.add(mouseX + "");//7
        strs.add(mouseY + "");//8
        return strs;
    }

    private void gunReceive(GameActor gameActor,ArrayList<String> strs){
        gameActor.getCurrentGun().painter().setCenter(Integer.valueOf(strs.get(5)),Integer.valueOf(strs.get(6))-28);
        gameActor.getCurrentGun().collider().setCenter(Integer.valueOf(strs.get(5)),Integer.valueOf(strs.get(6))-28);
        gameActor.getCurrentGun().setDir(GameObjForAnimator.Dir.valueOf(strs.get(4)));
        gameActor.getRotation().rotationUpdate(gameActor.collider().centerX(), gameActor.collider().centerY(),
                gameActor.collider().centerX(), gameActor.collider().centerY(), gameActor.getDir(),Integer.valueOf(strs.get(7)),Integer.valueOf(strs.get(8)));

    }

    public void healReceive(ArrayList<GameActor> gameActorArr,int serialNum,ArrayList<String> strs){
        for(int i=1 ; i<gameActorArr.size() ; i++) {
            if (gameActorArr.get(i).getConnectID() == serialNum) {
                if(gameActorArr.get(i).getConnectID() == Integer.valueOf(strs.get(0))){
                    gameActorArr.get(i).getSkill().heal();
                }
            }
        }
    }

    public void flashReceive(ArrayList<GameActor> gameActorArr,int serialNum,ArrayList<String> strs){
        for(int i=1 ; i<gameActorArr.size() ; i++) {
            if (gameActorArr.get(i).getConnectID() == serialNum) {
                if(gameActorArr.get(i).getConnectID() == Integer.valueOf(strs.get(0))){
                    gameActorArr.get(i).getSkill().flash(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2)),null);

                }
            }
        }
    }

    public void changeGunReceive(ArrayList<GameActor> gameActorArr,int serialNum,ArrayList<String> strs){
        for(int i=1 ; i<gameActorArr.size() ; i++) {
            if (gameActorArr.get(i).getConnectID() == serialNum) {
                gameActorArr.get(i).changeGun(Integer.valueOf(strs.get(1)));
            }
        }
    }

    public void newBulletSend(GameActor gameActor, int mouseX, int mouseY){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(gameActor.painter().centerX() + "");
        strs.add(gameActor.painter().centerY() + "");
        strs.add(mouseX + "");
        strs.add(mouseY + "");
        strs.add(gameActor.getCurrentGun().getGunType() + "");
        strs.add(gameActor.getConnectID() + "");
        ClientClass.getInstance().sent(NetEvent.BULLET_NEW, strs);
    }

    public void newBulletReceive(LinkedList<Bullet> bullets, ArrayList<String> strs){
        bullets.add(new Bullet(Integer.valueOf(strs.get(0)),Integer.valueOf(strs.get(1)),
                Integer.valueOf(strs.get(2)),Integer.valueOf(strs.get(3)),
                        Gun.GunType.valueOf(strs.get(4)),Integer.valueOf(strs.get(5))));
    }

    public void newMonsterSend(Monster monster,int commandCode){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(commandCode +"");//0
        strs.add(monster.painter().left() +"");//1
        strs.add(monster.painter().top() +"");//2
        ClientClass.getInstance().sent(NetEvent.MONSTER_NEW, strs);
    }

    public void newMonsterReceive(LinkedList<Monster> monsters,ArrayList<String> strs){
        switch (Integer.valueOf(strs.get(0))){
            case 0 -> monsters.add(new BullBoss(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2))));
            case 1 -> monsters.add(new Cockroach(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2))));
            case 2 -> monsters.add(new Rino(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2))));
            case 3 -> monsters.add(new Stone(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2))));
            case 4 -> monsters.add(new SmallMonster(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2)), SmallMonster.Type.values()[Integer.valueOf(strs.get(0))-4]));
            case 5 -> monsters.add(new SmallMonster(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2)), SmallMonster.Type.values()[Integer.valueOf(strs.get(0))-4]));
            case 6 -> monsters.add(new SmallMonster(Integer.valueOf(strs.get(1)),Integer.valueOf(strs.get(2)), SmallMonster.Type.values()[Integer.valueOf(strs.get(0))-4]));
        }
    }

    public void bossAtkTypeSend(int typeCode){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(typeCode +"");//0
        ClientClass.getInstance().sent(NetEvent.MONSTER_BOSS_ATTACK_TYPE, strs);
    }

    public void bossAtkTypeReceive(Monster monster,ArrayList<String> strs){
        monster.setAtkType(Integer.valueOf(strs.get(0)));
    }

    public void monsterSend(Monster monster){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(monster.getConnectID() +"");//0
        strs.add(monster.collider().left() + "");//1
        strs.add(monster.collider().top() + "");//2
        strs.add(monster.getState() + "");//3
        strs.add(monster.getDir() + "");//4
        ClientClass.getInstance().sent(NetEvent.MONSTER, strs);
    }

    public void monsterReceive(LinkedList<Monster> monster,ArrayList<String> strs){
        for(int i=0 ; i<monster.size() ; i++){
            if(monster.get(i).getConnectID() == Integer.valueOf(strs.get(0))){
                monster.get(i).offSetX(Integer.valueOf(strs.get(1)));
                monster.get(i).offSetY(Integer.valueOf(strs.get(2)));
                monster.get(i).setState(GameObjForAnimator.State.valueOf(strs.get(3)));
                monster.get(i).setDir(GameObjForAnimator.Dir.valueOf(strs.get(4)));
                monster.get(i).transHitArea();
            }
        }
    }

    public void changeSceneSend(boolean isNormal){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(isNormal + "");
        ClientClass.getInstance().sent(NetEvent.EVENT_CHANGE_SCENE, strs);
    }

    public void changeSceneReceive(ArrayList<String> strs, ArrayList<GameActor> gameActorArr){
        ArrayList<GameActor> test = new ArrayList<>();

        if(Boolean.valueOf(strs.get(0))){
            for(int i=0 ; i<gameActorArr.size() ; i++){
                switch (gameActorArr.get(i).getConnectID()){
                    case 100 -> test.add(new GameActor(Actor.FIRST,200,200));
                    case 101 -> test.add(new GameActor(Actor.SECOND,1000,2000));
                    case 102 -> test.add(new GameActor(Actor.THIRD,1500,200));
                }
            }
            for(int i=0 ; i<gameActorArr.size() ; i++){
                test.get(i).setConnectID(gameActorArr.get(i).getConnectID());
            }
            SenceController.getSenceController().change(new BossScene(test));
            return;
        }
        for(int i=0 ; i<gameActorArr.size() ; i++){
            switch (gameActorArr.get(i).getConnectID()){
                case 100 -> test.add(new GameActor(Actor.FIRST,500,500));
                case 101 -> test.add(new GameActor(Actor.SECOND,500,525));
                case 102 -> test.add(new GameActor(Actor.THIRD,500,550));
            }
        }
        for(int i=0 ; i<gameActorArr.size() ; i++){
            test.get(i).setConnectID(gameActorArr.get(i).getConnectID());
        }

        SenceController.getSenceController().change(new LimitMode(test));
    }

    public void monsterDeadSend(int key){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(key + "");
        ClientClass.getInstance().sent(NetEvent.MONSTER_DEAD, strs);
    }

    public void monsterDeadReceive(LinkedList<Monster> monster,ArrayList<String> strs){
        for(int i=0 ; i<monster.size() ; i++){
            if(monster.get(i).getConnectID() == Integer.valueOf(strs.get(0))){
                monster.remove(i);
                return;
            }
        }
    }

    public void monsterBooleanSend(boolean isTrue, int connectID, String type){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(type);
        strs.add(connectID + "");
        strs.add(isTrue + "");
        ClientClass.getInstance().sent(NetEvent.MONSTER_IS_CHASE, strs);
    }

    public void monsterBooleanReceive(LinkedList<Monster> monster, ArrayList<String> strs){
        for(int i=0 ; i<monster.size() ; i++){
            if(monster.get(i).getConnectID() == Integer.valueOf(strs.get(1))){
                switch (strs.get(0)){
                   case "isChase" ->monster.get(i).setIsChase(Boolean.valueOf(strs.get(2)));
                   case "forRino" ->monster.get(i).setForRino(Boolean.valueOf(strs.get(2)));
                   case "focus" ->monster.get(i).setFocus(Boolean.valueOf(strs.get(2)));
                   case "readyAtk" ->monster.get(i).setReadyAtk(Boolean.valueOf(strs.get(2)));
                }

            }
        }
    }

    public void monsterStateSend(GameObjForAnimator.State state,int connectID){
        ArrayList<String> strs = new ArrayList<>();
        strs.add(connectID + "");
        strs.add(state +"");
        ClientClass.getInstance().sent(NetEvent.MONSTER_STATE, strs);
    }

    public void monsterStateReceive(LinkedList<Monster> monster, ArrayList<String> strs){
        for(int i=0 ; i<monster.size() ; i++){
            if(monster.get(i).getConnectID() == Integer.valueOf(strs.get(0))){
                monster.get(i).setState(GameObjForAnimator.State.valueOf(strs.get(1)));
            }
        }
    }
}

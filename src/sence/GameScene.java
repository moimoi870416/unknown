package sence;

import camera.Camera;
import client.ClientClass;
import object.GameObjForAnimator;
import object.GameObject;
import object.actor.GameActor;
import object.monster.Monster;
import util.CommandSolver;
import util.Display;
import weapon.Bullet;
import weapon.Gun;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static util.Global.*;

public abstract class GameScene extends Scene {
    protected ArrayList<GameObject> mapObjArr;
    private LinkedList<Bullet> testBullets;
    protected LinkedList<Monster> monster;
    private int listenerMouseX;
    private int listenerMouseY;
    protected ArrayList<GameActor> gameActorArr;//主角
    private Display display;//畫面物件
    private boolean shooting;
    private Camera camera;
    protected MapInfo mapInfo;

    @Override
    public void sceneBegin() {
        mapObjArr = new ArrayList<>();
        monster = new LinkedList<>();
        testBullets = new LinkedList<>();
        gameActorArr = new ArrayList<>();
        sceneBeginComponent();
        display = new Display(gameActorArr.get(0));
        camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
                .setCameraMoveSpeed(2)
                .setChaseObj(gameActorArr.get(0), 20, 20)
                .setCameraStartLocation(-WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2)
                .gen();
    }

    protected abstract void sceneBeginComponent();

    @Override
    public void sceneEnd() {
        sceneEndComponent();
    }

    protected abstract void sceneEndComponent();

    @Override
    public void paint(Graphics g) {
        camera.start(g);//鏡頭一定要第一個PAINT
        mapInfo.mapPaint(g);
        monster.forEach(monster -> {
            if (camera.isCollision(monster)) {
                monster.paint(g);
            }
        });

        for(int i=0 ; i<gameActorArr.size() ; i++){
            if (camera.isCollision(gameActorArr.get(i))) {
                gameActorArr.get(i).paint(g);
            }
        }

        //mapObjArr.forEach(a -> a.paint(g));
        mapObjArr.forEach(object -> {
            if (camera.isCollision(object)) {
                object.paint(g);
            }
        });
        testBullets.forEach(testBullet -> testBullet.paint(g));
        camera.paint(g);
        camera.end(g);
        display.paint(g);
    }

    @Override
    public void update() {
        connectUpdate();
        mouseUpdate();
        monsterUpdate();
        actorUpdate();
        displayUpdate();
        shootUpdate();
        bulletsUpdate();
        mapInfo.mapUpdate();
        camera.update();
    }

    protected abstract void connectUpdate();

    private void mouseUpdate() {
        mouseX = listenerMouseX + camera.getCameraWindowX();//滑鼠的絕對座標 ((listenerMouse是滑鼠監聽的回傳值
        mouseY = listenerMouseY + camera.getCameraWindowY();
        gameActorArr.get(0).changeDir(mouseX);
    }

    private void displayUpdate() {
        display.displayUpdate(gameActorArr.get(0));
    }

    private void bulletsUpdate() {
        for (int i = 0; i < testBullets.size(); i++) {
            if (testBullets.get(i).getState() == Bullet.State.STOP) {
                testBullets.remove(i);
                i--;
                break;
            }
            testBullets.get(i).update();
            if (testBullets.get(i).isOut()) {
                testBullets.remove(i);
                i--;
                continue;
            }
            int x = 0;
            for (int k = 0; k < mapObjArr.size(); k++) {
                if (testBullets.get(i).isCollied(mapObjArr.get(k))) {
                    testBullets.remove(i);
                    i--;
                    x++;
                    break;
                }
            }
            for (int k = 1; k < gameActorArr.size(); k++) {
                if (testBullets.get(i).isShootingActor(gameActorArr.get(k))) {
                    gameActorArr.get(k).offLife(testBullets.get(i).getAtk());
                    i--;
                    break;
                }

            }
            if (x == 0) {
                for (int k = 0; k < monster.size(); k++) {
                    if (testBullets.get(i).isColliedInHit(monster.get(k))) {
                        if (monster.get(k).getState() != GameObjForAnimator.State.DEATH) {
                            int life = monster.get(k).getLife();
                            monster.get(k).offLife(testBullets.get(i).getAtk());
                            if (monster.get(k).getLife() <= 0) {
                                monster.get(k).setState(GameObjForAnimator.State.DEATH);
                            }
                            if (testBullets.get(i).isPenetrate(life)) {
//                                testBullets.remove(i);
//                                i--;
//                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void monsterUpdate() {
        for (int i = 0; i < monster.size(); i++) {
            if (monster.get(i).getState() == GameObjForAnimator.State.DEAD) {
                monster.remove(i);
                i--;
                break;
            }
            monster.get(i).update();
            if (monster.get(i).isCollisionWithActor(gameActorArr.get(0))) {
                monster.get(i).attack(gameActorArr.get(0));
            }
            for (int k = 0; k < mapObjArr.size(); k++) {
                monster.get(i).isCollider(mapObjArr.get(k));
            }
            if (monster.size() > 1 && i != monster.size() - 1) {
                if (!gameActorArr.get(0).isCollisionWithActor(monster.get(i))) {
                    monster.get(i).isCollisionWithMonster(monster.get(i + 1));
                }
            }
        }

    }

    private void shootUpdate() {
        if (shooting) {
            if (gameActorArr.get(0).getCurrentGun().shoot()) {
                this.testBullets.add(new Bullet
                        (gameActorArr.get(0).painter().centerX(), gameActorArr.get(0).painter().centerY(),
                                mouseX, mouseY,
                                gameActorArr.get(0).getCurrentGun().getGunType(),
                                gameActorArr.get(0)));
                shootCount++;
            }
        }
    }

    private void actorUpdate() {
        ArrayList<String> strs = new ArrayList<>();
        gameActorArr.get(0).update();
        strs.add(gameActorArr.get(0).collider().left() + "");
        strs.add(gameActorArr.get(0).collider().top() + "");
        ClientClass.getInstance().sent(NetEvent.CONNECT, strs);
        ClientClass.getInstance().sent(NetEvent.ACTOR_MOVE, strs);
        for (int i = 0; i < mapObjArr.size(); i++) {
            gameActorArr.get(0).isCollider(mapObjArr.get(i));
        }
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state == CommandSolver.MouseState.MOVED || state == CommandSolver.MouseState.DRAGGED) {
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
            }
            if (state == CommandSolver.MouseState.PRESSED) {
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
                gameActorArr.get(0).getCurrentGun().beginShoot();
                shooting = true;
                if (gameActorArr.get(0).getCurrentGun().getGunType() == Gun.GunType.MACHINE_GUN) {
                    gameActorArr.get(0).setMoveSpeed(gameActorArr.get(0).getCurrentGun().getGunType().getMoveSpeed() / 2);
                }
            }

            if (shooting && state == CommandSolver.MouseState.DRAGGED) {
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();

            }
            if (state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED) {
                shooting = false;
                if (shooting) {
                    if (gameActorArr.get(0).getCurrentGun().shoot()) {
                        this.testBullets.add(new Bullet
                                (this.gameActorArr.get(0).painter().centerX(), this.gameActorArr.get(0).painter().centerY(),
                                        mouseX, mouseY,
                                        gameActorArr.get(0).getCurrentGun().getGunType(),
                                        gameActorArr.get(0)));
                    }
                }
                if (gameActorArr.get(0).getCurrentGun().getGunType() == Gun.GunType.MACHINE_GUN) {
                    gameActorArr.get(0).setMoveSpeed(gameActorArr.get(0).getCurrentGun().getGunType().getMoveSpeed());
                }
                shootCount = 0;

                gameActorArr.get(0).getCurrentGun().resetBeginShoot();
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode >= 1 || commandCode <= 4) {
                    gameActorArr.get(0).move(commandCode);
                }
                if (commandCode == Active.RELOADING.getCommandCode()) {
                    gameActorArr.get(0).getCurrentGun().reloading();
                }
                if (commandCode == Active.NUMBER_ONE.getCommandCode() || commandCode == Active.NUMBER_TWO.getCommandCode()) {
                    gameActorArr.get(0).changeGun(commandCode);
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (commandCode >= 1 || commandCode <= 4) {
                    gameActorArr.get(0).setState(GameObjForAnimator.State.STAND);
                }
                if (commandCode == Active.SPACE.getCommandCode()) {
                    gameActorArr.get(0).getSkill().flash(mouseX, mouseY, mapObjArr);
                }
                if (commandCode == Active.SKILL.getCommandCode()) {
                    gameActorArr.get(0).getSkill().heal();
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    protected abstract class MapInfo {

        public abstract void mapPaint(Graphics g);

        public abstract void mapUpdate();
    }

}
package sence;

import camera.Camera;
import controller.ImageController;
import object.GameObjForAnimator;
import object.GameObject;
import object.actor.GameActor;
import object.monster.Monster;
import util.CommandSolver;
import controller.ConnectController;
import util.Display;
import weapon.Bullet;
import weapon.Gun;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static util.Global.*;

public abstract class GameScene extends Scene {
    protected ArrayList<GameObject> mapObjArr;
    protected LinkedList<Bullet> testBullets;
    protected LinkedList<Monster> monster;
    private int listenerMouseX;
    private int listenerMouseY;
    protected ArrayList<GameActor> gameActorArr;//主角
    private Display display;//畫面物件
    private boolean shooting;
    private Camera camera;
    protected MapInfo mapInfo;
    private int mouseX;
    private int mouseY;
    private EffectView effectView;


    @Override
    public void sceneBegin() {
        mapObjArr = new ArrayList<>();
        monster = new LinkedList<>();
        testBullets = new LinkedList<>();
        sceneBeginComponent();
        display = new Display(gameActorArr.get(0));
        camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
                .setCameraMoveSpeed(2)
                .setChaseObj(gameActorArr.get(0), 20, 20)
                .setCameraStartLocation(-WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2)
                .gen();
        effectView = new EffectView();

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
        for (int i = 0; i < gameActorArr.size(); i++) {
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
        effectView.effectPaint(g);
        camera.end(g);
        display.paint(g);
    }

    @Override
    public void update() {
        connectUpdate();
        effectView.effectUpdate();
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
        this.mouseX = listenerMouseX + camera.getCameraWindowX();//滑鼠的絕對座標 ((listenerMouse是滑鼠監聽的回傳值
        this.mouseY = listenerMouseY + camera.getCameraWindowY();
        gameActorArr.get(0).changeDir(this.mouseX);
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
            if (x == 0) {
                for (int k = 0; k < gameActorArr.size(); k++) {
                    if (testBullets.get(i).isShootingActor(gameActorArr.get(k))) {
                        if(testBullets.get(i).getAtk() >=100){
                            gameActorArr.get(k).setLife(1);
                            i--;
                            x++;
                            break;
                        }
                        gameActorArr.get(k).offLife(testBullets.get(i).getAtk() / 4);
                        i--;
                        x++;
                        break;
                    }

                }
            }
            if (x == 0) {
                for (int k = 0; k < monster.size(); k++) {
                    if (testBullets.get(i).isColliedInHit(monster.get(k))) {
                        if (monster.get(k).getState() != GameObjForAnimator.State.DEATH) {
                            int life = monster.get(k).getLife();
                            monster.get(k).offLife(testBullets.get(i).getAtk());
                            if (monster.get(k).getLife() <= 0) {
                                monster.get(k).setStateComponent(GameObjForAnimator.State.DEATH);
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
            if (isServer) {
                if (monster.get(i).getState() == GameObjForAnimator.State.DEAD) {
                    ConnectController.getInstance().monsterDeadSend(monster.get(i).getConnectID());
                    monster.remove(i);
                    i--;
                    break;
                }
            }
            if (monster.get(i).getState() == GameObjForAnimator.State.DEATH || monster.get(i).getState() == GameObjForAnimator.State.DEAD) {
                break;
            }
            monster.get(i).updateForConnect();
            if (isServer) {
                monster.get(i).update();
                for (int k = 0; k < gameActorArr.size(); k++) {
                    monster.get(i).whoIsNear(gameActorArr.get(k));
                }
                for (int k = 0; k < mapObjArr.size(); k++) {
                    monster.get(i).isCollider(mapObjArr.get(k));
                }
                if (monster.size() > 1 && i != monster.size() - 1) {
                    for (int k = 0; k < gameActorArr.size(); k++) {
                        if (!gameActorArr.get(k).isCollisionWithActor(monster.get(i))) {
                            monster.get(i).isCollisionWithMonster(monster.get(i + 1));
                        }
                    }
                }
                ConnectController.getInstance().monsterSend(monster.get(i));
            }
            if (monster.get(i).isCollisionWithActor(gameActorArr.get(0))) {
                monster.get(i).attack(gameActorArr.get(0));
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
                                gameActorArr.get(0).getConnectID()));
                ConnectController.getInstance().newBulletSend(gameActorArr.get(0), mouseX, mouseY);
                shootCount++;
            }
        }
    }

    private void actorUpdate() {
        for (int i = 0; i < gameActorArr.size(); i++) {
            gameActorArr.get(i).getSkill().skillUpdate();
        }
        gameActorArr.get(0).update();
        gameActorArr.get(0).rotationUpdate(mouseX, mouseY);
        ConnectController.getInstance().actorSend(gameActorArr.get(0), mouseX, mouseY);//傳送自己的資料
        for (int i = 0; i < mapObjArr.size(); i++) {//場景物件碰撞
            gameActorArr.get(0).isCollider(mapObjArr.get(i));
        }
        if (gameActorArr.get(0).getState() == GameObjForAnimator.State.DEAD) {//死亡畫面追蹤
            if (gameActorArr.size() == 2) {
                camera.setObj(gameActorArr.get(1));
                gameActorArr.get(0).offSetX(gameActorArr.get(1).collider().left());
                gameActorArr.get(0).offSetY(gameActorArr.get(1).collider().top());
                if (gameActorArr.get(1).getLife() <= 0 && gameActorArr.size() == 3) {
                    camera.setObj(gameActorArr.get(2));
                    gameActorArr.get(0).offSetX(gameActorArr.get(2).collider().left());
                    gameActorArr.get(0).offSetY(gameActorArr.get(2).collider().top());
                }
            }
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
                                        gameActorArr.get(0).getConnectID()));
                        ConnectController.getInstance().newBulletSend(gameActorArr.get(0), mouseX, mouseY);
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
                if (gameActorArr.get(0).getState() == GameObjForAnimator.State.DEATH || gameActorArr.get(0).getState() == GameObjForAnimator.State.DEAD) {
                    return;
                }
                if (commandCode >= 1 || commandCode <= 4) {
                    gameActorArr.get(0).setState(GameObjForAnimator.State.STAND);
                }
                if (commandCode == Active.SPACE.getCommandCode() && gameActorArr.get(0).getSkill().getDelayForFlash().isStop()) {
                    gameActorArr.get(0).getSkill().flash(mouseX, mouseY, mapObjArr);
                    ConnectController.getInstance().flashSend(gameActorArr.get(0), mouseX, mouseY);

                }
                if (commandCode == Active.SKILL.getCommandCode() && gameActorArr.get(0).getSkill().getHealCD().isStop()) {
                    gameActorArr.get(0).getSkill().heal();
                    ConnectController.getInstance().healSend(gameActorArr.get(0));
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

    private class EffectView {
        private Image victory;
        private Image defeat;
        private Image warning;
        private boolean isVictory;
        private boolean isNobodyAlive;

        private EffectView() {
            isVictory = false;
            isNobodyAlive = false;
            defeat = ImageController.getInstance().tryGet("/pictures/effect/fail.png");
        }

        private void effectPaint(Graphics g) {
            if (isNobodyAlive) {
                g.drawImage(defeat, camera.getCameraWindowX() + 220, camera.getCameraWindowY() + 200, null);
            }

        }

        private void effectUpdate() {
            int sum = 0;
            for (int i = 0; i < gameActorArr.size(); i++) {
                if (gameActorArr.get(i).getLife() <= 0) {
                    gameActorArr.get(i).setLife(0);
                }
                sum += gameActorArr.get(i).getLife();
            }
            if (sum <= 0) {
                isNobodyAlive = true;

            } else {

                isNobodyAlive = false;
            }
        }
    }
}

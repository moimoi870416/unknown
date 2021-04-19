package object.actor;

import util.*;
import object.GameObjForAnimator;
import weapon.Gun;

import java.awt.*;

public class GameActor extends GameObjForAnimator {
    private WhichGun currentGun;
    private WhichGun otherGun;
    private Global.Direction verticalDir;
    private Global.Direction horizontalDir;
    private final int FLASH_MAX_DISTANCE = 300;
    private Delay delayForFlash;
    private boolean canFlash;
    private Animator flashAnimator;
    private int XForFlash;
    private int YForFlash;
    private Rotation rotation;
    private Bar blood;


    public GameActor( String path,final int x, final int y) {
        super(x, y, 58, 58,100,10,3);
        animator = new Animator(path,30,58,58,2);
        animator.setArr(3);
        flashAnimator = new Animator("/actor/flash.png",8,48,32,2);
        flashAnimator.setArr(4);
        flashAnimator.setPlayOnce();
        currentGun = WhichGun.ONE;
        otherGun = WhichGun.TWO;
        currentGun.gun.translate(painter().centerX(), painter().centerY());

        verticalDir = horizontalDir = Global.Direction.NO;
        otherGun.gun.translate(painter().centerX(), painter().centerY());
        delayForFlash = new Delay(120);
        canFlash = true;
        rotation = new Rotation();
        blood = new Bar();

    }

    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
        rotation.paint(g, currentGun.gun,dir);
        flashAnimator.paintAnimator(g, XForFlash - 24, XForFlash + 24, YForFlash - 16, YForFlash + 16, dir);
        blood.paint(g);
    }

    public void changeGun(int commandCode) {
        if (commandCode == -1) {
            currentGun = WhichGun.ONE;
            otherGun = WhichGun.TWO;
            Display.isFirstGun = true;
        } else if (commandCode == -2) {
            currentGun = WhichGun.TWO;
            otherGun = WhichGun.ONE;
            Display.isFirstGun = false;
        }
    }

    private enum WhichGun {
        ONE(new Gun(Gun.GunType.MACHINE_GUN, Global.actorX, Global.actorY)),
        TWO(new Gun(Gun.GunType.PISTOL, Global.actorX, Global.actorY));
        private Gun gun;

        WhichGun(Gun gun) {
            this.gun = gun;
        }
    }

    @Override
    public void changeDir(int mouseX) {
        if (mouseX > painter().centerX()) {
            dir = Dir.LEFT;
        } else {
            dir = Dir.RIGHT;
        }
    }

    public void tradeGun(Gun gun) {
        currentGun.gun = gun;
    }

    public Gun gunOtherGun() {
        return otherGun.gun;
    }

    public Gun getCurrentGun() {
        return currentGun.gun;
    }

    public void move(int commandCode) {
        if(state != State.RUN){
            setState(State.RUN);
        }
        switch (commandCode) {
            case 2:
                translateY(-moveSpeed);
                verticalDir = Global.Direction.UP;
                break;
            case 3:
                translateY(moveSpeed);
                verticalDir = Global.Direction.DOWN;
                break;
            case 1:
                translateX(moveSpeed);
                horizontalDir = Global.Direction.RIGHT;
                break;
            case 0:
                translateX(-moveSpeed);
                horizontalDir = Global.Direction.LEFT;
                break;
            default:
                verticalDir = horizontalDir = Global.Direction.NO;
                break;
        }
    }

    @Override
    public void setState(State state) {
        this.state = state;
        switch (state){
            case STAND -> {
                animator.setImg("/actor/actorStand.png",2);
                animator.setArr(3);
                animator.setDelayCount(30);
                animator.setPlayLoop();
            }
            case RUN -> {
                animator.setImg("/actor/run.png",2);
                animator.setArr(4);
                animator.setDelayCount(10);
                animator.setPlayLoop();
            }
            case DEATH -> {
                animator.setImg("/actor/actorDead.png",2);
                animator.setArr(16);
                animator.setDelayCount(20);
                animator.setPlayOnce();
            }
        }
    }

    @Override
    public void update() {
        switch (verticalDir) {
            case UP:
                if (painter().top() < 0) {
                    translateY(moveSpeed);
                }
                break;
            case DOWN:
                if (painter().bottom() > Global.MAP_HEIGHT - 70) {
                    translateY(-moveSpeed);
                }
                break;
        }
        switch (horizontalDir){
            case RIGHT:
                if (painter().right() > Global.MAP_WIDTH) {
                    translateX(-moveSpeed);
                }
                break;
            case LEFT:
                if (painter().left() < 0) {
                    translateX(moveSpeed);
                }
                break;

            case UP:
                if (painter().top() < 0) {
                    translateY(moveSpeed);
                }
                break;
            case DOWN:
                if (painter().bottom() > Global.MAP_HEIGHT - 15) {
                    translateY(-moveSpeed);
                }
                break;
            case NO:
        }
        if (delayForFlash.count()) {
            canFlash = true;
        }
        currentGun.gun.update();
        currentGun.gun.translateForActor();
        updatePosition();
        rotation.rotationUpdate(this.collider().centerX() + currentGun.gun.collider().width() / 3f, this.collider().centerY() - currentGun.gun.collider().height() / 2,
                this.collider().centerX() - currentGun.gun.collider().width() / 3f, this.collider().centerY() - currentGun.gun.collider().height() / 2);
        System.out.println(life);
        blood.barUpdate(collider().left(),collider().top(),this.life);
    }

    private void updatePosition() {
        Global.actorX = collider().centerX();
        Global.actorY = collider().bottom();
    }

    public void flash(int mouseX, int mouseY) {
        if (canFlash) {
            delayForFlash.play();
            flashAnimator.setPlayOnce();
            XForFlash = painter().centerX();
            YForFlash = painter().centerY();
            int x = Math.abs(mouseX - painter().centerX());
            int y = Math.abs(mouseY - painter().centerY());
            if (x == 0 && y == 0) {
                return;
            }
            float d = (float) Math.sqrt(x * x + y * y);
            float distance = d;//計算斜邊,怪物與人物的距離
            if (distance > FLASH_MAX_DISTANCE) {
                distance = FLASH_MAX_DISTANCE;
            }
            float moveOnX = (float) Math.cos(Math.toRadians((Math.acos(x / d) / Math.PI * 180))) * distance;
            float moveOnY = (float) Math.sin(Math.toRadians((Math.asin(y / d) / Math.PI * 180))) * distance;
            if (mouseY < painter().centerY()) {
                moveOnY = -moveOnY;
            }
            if (mouseX < painter().centerX()) {
                moveOnX = -moveOnX;
            }
            translate((int) moveOnX, (int) moveOnY);
            canFlash = false;
        }
    }
}

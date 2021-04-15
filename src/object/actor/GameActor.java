package object.actor;

import util.Animator;
import util.Delay;
import util.Display;
import util.Global;
import object.GameObjForAnimator;
import weapon.Gun;

import java.awt.*;

public class GameActor extends GameObjForAnimator {
    private WhichGun currentGun;
    private WhichGun otherGun;
    private Global.Direction dirMove;
    private final int FLASH_MAX_DISTANCE = 300;
    private Delay delayForFlash;
    private boolean canFlash;

    public GameActor( String path,final int x, final int y) {
        super(x, y, 58, 58,100,10,3);
        animator = new Animator(path,15,58,58,2);
        animator.setArr(4);
        currentGun = WhichGun.ONE;
        otherGun = WhichGun.TWO;
        currentGun.gun.translate(painter().centerX(), painter().centerY());
        dirMove = Global.Direction.NO;
        delayForFlash = new Delay(600);
        canFlash = true;

    }
    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
        currentGun.gun.paintComponent(g,Global.actorX,Global.actorY-50);
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
        ONE(new Gun(Gun.GunType.PISTOL, Global.actorX, Global.actorY)),
        TWO(new Gun(Gun.GunType.UZI, Global.actorX, Global.actorY));

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

    public void tradeGun(Gun gun){
        currentGun.gun = gun;
    }

    public Gun gunOtherGun(){
        return otherGun.gun;
    }

    public Gun getCurrentGun() {
        return currentGun.gun;
    }

    public void move(int commandCode) {
        switch (commandCode) {
            case 2:
                translateY(-moveSpeed);
                dirMove = Global.Direction.UP;
                break;
            case 3:
                translateY(moveSpeed);
                dirMove = Global.Direction.DOWN;
                break;
            case 1:
                translateX(moveSpeed);
                dirMove = Global.Direction.RIGHT;
                break;
            case 0:
                translateX(-moveSpeed);
                dirMove = Global.Direction.LEFT;
                break;
            default:
                dirMove = Global.Direction.NO;
                break;
        }
    }

    @Override
    public void setState(State state) {

    }

    @Override
    public void update() {
        switch (dirMove) {
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
        updatePosition();
    }

    private void updatePosition() {
        Global.actorX = collider().centerX();
        Global.actorY = collider().bottom();
    }

    public void flash(int mouseX, int mouseY) {
        if (canFlash) {
            delayForFlash.play();
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

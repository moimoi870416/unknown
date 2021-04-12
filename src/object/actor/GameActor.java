package object.actor;

import unit.Delay;
import unit.Global;
import object.GameObjForAnimator;
import weapon.Gun;

import java.awt.*;

public class GameActor extends GameObjForAnimator {
    private WhichGun whichGun;
    private Global.Direction dirMove;
    private final int FLASHDISTANCE = 200;
    private Delay delayForFlash;
    private boolean canFlash;

    public GameActor( String path,final int x, final int y) {
        super(path,15,x, y, 58, 58,100,10,3);
        animator.setAnimatorSize(58);
        animator.setACTOR_WALK(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13});
        whichGun = WhichGun.ONE;
        dirMove = Global.Direction.NO;
        delayForFlash = new Delay(600);
        canFlash = true;
    }

    public void changeGun(int commandCode){
        if(commandCode == -1){
            whichGun = WhichGun.ONE;
        }else if(commandCode == -2){
            whichGun = WhichGun.TWO;
        }
    }

    private enum WhichGun{
        ONE(new Gun(Gun.GunType.MACHINE_GUN,0,0)),
        TWO(new Gun(Gun.GunType.SNIPER,0,0));

        private Gun gun;

        WhichGun(Gun gun){
            this.gun = gun;
        }
    }

    @Override
    public void changeDir(int mouseX){
        if(mouseX>painter().centerX()){
            dir = Dir.LEFT;
        }else {
            dir = Dir.RIGHT;
        }
    }

    public Gun getGun(){
        return whichGun.gun;
    }

    public void move(int commandCode){
        switch (commandCode){
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
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
//        whichGun.gun.paint(g,painter().centerX(),painter().centerY(),null);
    }

    @Override
    public void update() {
        switch (dirMove) {
            case RIGHT:
                if (painter().right() > Global.WINDOW_WIDTH) {
                    translateX(-moveSpeed);
                }
                break;
            case LEFT :
                if (painter().left() < 0) {
                    translateX(moveSpeed);
                }
                break;
            case UP :
                if (painter().top() < 0) {
                    translateY(moveSpeed);
                }
                break;
            case DOWN :
                if (painter().bottom() > Global.WINDOW_HEIGHT) {
                    translateY(-moveSpeed);
                }
                break;
            case NO:

        }
        if(delayForFlash.count()){
            canFlash = true;
        }
        whichGun.gun.update();
    }

    public void flash(int mouseX,int mouseY){
        delayForFlash.play();
        if(canFlash) {
            float x = Math.abs(mouseX - painter().centerX());
            float y = Math.abs(mouseY - painter().centerY());
            if (x == 0 && y == 0) {
                return;
            }
            float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
            if (distance > FLASHDISTANCE) {
                distance = FLASHDISTANCE;
            }
            float moveOnX = (float) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * distance);
            float moveOnY = (float) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * distance);
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

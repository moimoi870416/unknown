package object.actor;

import unit.Global;
import object.GameObjForAnimator;
import weapon.GunTest;
import java.awt.*;

public class GameActor extends GameObjForAnimator {
    private WhichGun whichGun;
    private Global.Direction dirMove;

    public GameActor( String path,final int x, final int y) {
        super(path,15,x, y, 58, 58,100,10,3);
        animator.setAnimatorSize(58);
        animator.setACTOR_WALK(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13});
        whichGun = WhichGun.ONE;
        dirMove = Global.Direction.NO;
    }

    public void changeGun(int commandCode){
        if(commandCode == -1){
            whichGun = WhichGun.ONE;
        }else if(commandCode == -2){
            whichGun = WhichGun.TWO;
        }
    }

    private enum WhichGun{
        ONE(new GunTest(GunTest.GunTypeTest.MACHINE_GUN)),
        TWO(new GunTest(GunTest.GunTypeTest.SNIPER_GUN));

        private GunTest gunTest;
        private GunTest getGun(){
            return gunTest;
        }
        WhichGun(GunTest gunTest){
            this.gunTest = gunTest;
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

    public GunTest getGun(){
        return whichGun.getGun();
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
        whichGun.gunTest.paint(g,painter().centerX(),painter().centerY());
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
    }

}

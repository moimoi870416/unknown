package object.actor;

import object.GameObjForAnimator;
import weapon.Gun;

import java.awt.*;

public class GameActor extends GameObjForAnimator {
    private WhichGun whichGun;

    public GameActor( final int x, final int y) {
        super("/actorrun.png",15,x, y, 58, 58,100,10,3);
        animator.setAnimatorSize(58);
        animator.setACTOR_WALK(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13});
        whichGun = WhichGun.GUN1;
    }

    public void changeGun(int commandCode){
        if(commandCode == -1){
            whichGun = WhichGun.GUN1;
        }else if(commandCode == -2){
            whichGun = WhichGun.GUN2;
        }
    }

    private enum WhichGun{
        GUN1(new Gun(Gun.GunType.PISTOL)),
        GUN2(new Gun(Gun.GunType.AK));

        private Gun gun;
        private Gun getGun(){
            return gun;
        }
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
        return whichGun.getGun();
    }

    public void move(int commandCode){
        switch (commandCode){
            case 2:
                translateY(-moveSpeed);
                break;
            case 3:
                translateY(moveSpeed);
                break;
            case 1:
                translateX(moveSpeed);
                break;
            case 0:
                translateX(-moveSpeed);
                break;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
        whichGun.gun.paint(g,painter().centerX(),painter().centerY());
    }

    @Override
    public void update() {

    }



}

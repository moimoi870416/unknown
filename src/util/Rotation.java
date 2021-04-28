package util;

import controller.ImageController;
import object.GameObjForAnimator;
import object.actor.GameActor;
import weapon.Gun;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static util.Global.*;

public class Rotation {

    private float startPointX;
    private float startPointY;
    private float fixedX; //定住的位置
    private float fixedY;
    private GameObjForAnimator.Dir direction;
    private int mouseX;
    private int mouseY;

    public float angleBetweenTwoPointWithFixedPoint() {
        float angle1 = (float) Math.atan2((startPointY - fixedY), (startPointX - fixedX));
        float angle2 = (float) Math.atan2((this.mouseY - fixedY), (this.mouseX - fixedX));
        return angle2 - angle1;
    }

    public boolean isRight() {
        if (direction == GameObjForAnimator.Dir.RIGHT) {
            return true;
        }
        return false;
    }

    public float toDegrees() {
        float ans = (float) Math.toDegrees(angleBetweenTwoPointWithFixedPoint());
        if(isRight()) {
            return ans;
        }
        if(ans <= 0){
            return (ans + 180) % 180;
        }
        return ans - 180;

    }

    public void paint(Graphics g, Gun gun, GameActor gameActor) {

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform t = g2d.getTransform();
        g2d.rotate(Math.toRadians(toDegrees()), gameActor.painter().centerX(), gameActor.painter().bottom() - 20);
        if (isRight()) {
            g2d.drawImage(ImageController.getInstance().tryGet(gameActor.getCurrentGun().getGunType().forActorPath),
                    gameActor.painter().centerX(),
                    gameActor.painter().bottom() -34,
                    gameActor.painter().centerX() + gun.getGunType().getWidthForActor(),
                    gameActor.painter().bottom() + gun.getGunType().getHeightForActor() / 2 - 34,
                    0,
                    0,
                    gun.getGunType().getWidthForActor(),
                    gun.getGunType().getHeightForActor() / 2-2, null);
        } else {
            g2d.drawImage(ImageController.getInstance().tryGet(gameActor.getCurrentGun().getGunType().forActorPath),
                    gameActor.painter().centerX() - gun.getGunType().getWidthForActor(),
                    gameActor.painter().bottom()-34,
                    gameActor.painter().centerX(),
                    gameActor.painter().bottom()-34 + gun.getGunType().getHeightForActor() / 2,
                    0,
                    gun.getGunType().getHeightForActor() / 2,
                    gun.getGunType().getWidthForActor(),
                    gun.getGunType().getHeightForActor(), null);
        }
        g2d.setTransform(t);
    }

    public void rotationUpdate(float startPointX, float startPointY, float fixedX, float fixedY, GameObjForAnimator.Dir dir,int mouseX,int mouseY) {
        direction = dir;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if(dir == GameObjForAnimator.Dir.RIGHT){
            this.startPointX = startPointX;
            this.startPointY = startPointY;
            this.fixedX = fixedX;
            this.fixedY = fixedY;
        }else{
            this.startPointX = fixedX;
            this.startPointY = fixedY;
            this.fixedX = startPointX;
            this.fixedY = startPointY;
        }
    }

}

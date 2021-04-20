package util;

import controller.ImageController;
import object.GameObjForAnimator;
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

    public float angleBetweenTwoPointWithFixedPoint() {
        float angle1 = (float) Math.atan2((startPointY - fixedY), (startPointX - fixedX));
        float angle2 = (float) Math.atan2((mouseY - fixedY), (mouseX - fixedX));
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
//        if (isRight()) {
//            if (ans > 90) {
//                return 90f;
//            } else if (ans < -90) {
//                return -90f;
//            }
//            return ans;
//        }
//        if (ans <= 90) {
//            return 90f;
//        } else if (ans >= -90) {
//            return -90f;
//        }
//        return ans ;
    }

    public void paint(Graphics g, Gun gun) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform t = g2d.getTransform();
        g2d.rotate(Math.toRadians(toDegrees()), actorX, actorY - 20);
        System.out.println(toDegrees());
        if (isRight()) {
            g2d.drawImage(ImageController.getInstance().tryGet(gun.getGunType().forActorPath),
                    actorX,
                    actorY -35,
                    actorX + gun.getGunType().getWidthForActor(),
                    actorY + gun.getGunType().getHeightForActor() / 2 - 35,
                    0,
                    0,
                    gun.getGunType().getWidthForActor(),
                    gun.getGunType().getHeightForActor() / 2-2, null);
        } else {
            g2d.drawImage(ImageController.getInstance().tryGet(gun.getGunType().forActorPath),
                    actorX - gun.getGunType().getWidthForActor(),
                    actorY-35,
                    actorX,
                    actorY-35 + gun.getGunType().getHeightForActor() / 2,
                    0,
                    gun.getGunType().getHeightForActor() / 2,
                    gun.getGunType().getWidthForActor(),
                    gun.getGunType().getHeightForActor(), null);
        }
        g2d.setTransform(t);
    }

    public void rotationUpdate(float startPointX, float startPointY, float fixedX, float fixedY, GameObjForAnimator.Dir dir) {
        direction = dir;

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

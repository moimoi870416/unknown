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

    public float angleBetweenTwoPointWithFixedPoint() {
        float angle1 = (float) Math.atan2((startPointY - fixedY), (startPointX - fixedX));
        float angle2 = (float) Math.atan2((mouseY - fixedY), (mouseX - fixedX));
        return angle2 - angle1;
    }

    public float toDegrees() {
        float ans = (float) Math.toDegrees(angleBetweenTwoPointWithFixedPoint());
        if (ans > 90) {
            return 90f;
        } else if (ans < -90) {
            return -90f;
        } else {
            return ans ;
        }
    }

    public void paint(Graphics g, Gun gun, GameObjForAnimator.Dir dir) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform t = g2d.getTransform();
        g2d.rotate(Math.toRadians(toDegrees()), actorX, actorY-20);
        if(dir == GameObjForAnimator.Dir.LEFT) {
            g2d.drawImage(ImageController.getInstance().tryGet(gun.getGunType().forActorPath), actorX, actorY-35,actorX+gun.getGunType().getWidthForActor(), actorY+gun.getGunType().getHeightForActor()/2-35,0,0,gun.getGunType().getWidthForActor(),gun.getGunType().getHeightForActor()/2-2, null);
        }else {
            g2d.drawImage(ImageController.getInstance().tryGet(gun.getGunType().forActorPath), actorX-gun.getGunType().getWidthForActor(), actorY,actorX, actorY+gun.getGunType().getHeightForActor()/2,0,gun.getGunType().getHeightForActor()/2,gun.getGunType().getWidthForActor(),gun.getGunType().getHeightForActor(), null);
        }
        g2d.setTransform(t);
    }

    public void rotationUpdate(float startPointX, float startPointY, float fixedX, float fixedY) {
        this.startPointX = startPointX;
        this.startPointY = startPointY;
        this.fixedX = fixedX;
        this.fixedY = fixedY;
    }

}

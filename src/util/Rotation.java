package util;

import controller.ImageController;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static util.Global.*;

public class Rotation {

    private float startPointX;
    private float startPointY;

    private float fixedX;
    private float fixedY;

    public float angleBetweenTwoPointWithFixedPoint() {
        float angle1 = (float) Math.atan2((startPointY - fixedY), (startPointX - fixedX));
        float angle2 = (float) Math.atan2((mouseY - fixedY), ( mouseX - fixedX));
        return angle2 - angle1;
    }

    public float toDegrees() {
        return (float) Math.toDegrees(angleBetweenTwoPointWithFixedPoint());
    }

    public void paint(Graphics g,String path){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform t = g2d.getTransform();
        g2d.rotate(Math.toRadians(toDegrees()), actorX, actorY);
        g2d.drawImage(ImageController.getInstance().tryGet(path), actorX, actorY-25, null);
        g2d.setTransform(t);
    }

    public void rotationUpdate(float startPointX, float startPointY, float fixedX, float fixedY){
        this.startPointX = startPointX;
        this.startPointY = startPointY;
        this.fixedX = fixedX;
        this.fixedY= fixedY;
    }


}

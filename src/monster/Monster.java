package monster;

import controller.ImageController;
import object.GameObject;
import unit.Delay;

import java.awt.*;

public abstract class Monster extends GameObject {
    private int MOVE_SPEED;
    private Image image;
    private Delay delay;
    private int atk;

    public Monster(int x, int y, int width, int height) {
        super(x, y, width, height);
        image = ImageController.getInstance().tryGet("/grass1.png");
        //delay = new Delay(10);
        //delay.loop();
    }

    public void chase(int actorX,int actorY){
        double x = Math.abs(actorX - getCenterX());
        double y = Math.abs(actorY - getCenterY());
        double distance = Math.sqrt(x * x + y * y);//計算斜邊
        double moveOnX = Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * MOVE_SPEED;
        double moveOnY = Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * MOVE_SPEED;
        if (actorY < getCenterY()) {
            moveOnY = -moveOnY;
        }
        if (actorX < getCenterX()) {
            moveOnX = -moveOnX;
        }
        move(moveOnX, moveOnY);

    }

    private void move(double x,double y){
        offSet(x,y);
    }

    @Override
    public boolean isOut() {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image,left(),top(),null);
    }

    @Override
    public void update() {
    }
}

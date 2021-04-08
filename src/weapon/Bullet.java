package weapon;

import controller.ImageController;
import object.GameObject;
import unit.GameKernel;

import java.awt.*;

public class Bullet implements GameKernel.PaintInterface, GameKernel.UpdateInterface{

    private float x;
    private float y;
    private float width;
    private float height;
    protected float mouseX;//滑鼠X位置
    protected float mouseY;//滑鼠Y位置
    private float moveOnX;//X方向位移
    private float moveOnY;//Y方向位移
    private int MOVE_SPEED;
    protected float shootDeviation;//射擊偏差(預設為1=無偏差)
    private float distance;
    private int atk;
    private GunType gunType;
    private Image img;

    public Bullet(final int x, final int y, final int width, final int height,int mouseX,int mouseY,GunType gunType) {
        img = ImageController.getInstance().tryGet("/bullet.png");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.gunType = gunType;
        setGun();
        setAngle();
        setDistanceDeviation();
    }

    private void setAngle(){
        float x = (float) Math.abs(mouseX-getCenterX());
        float y = (float)Math.abs(mouseY-getCenterY());
        distance = (float)Math.sqrt(x*x+y*y);//計算斜邊
        moveOnX = (float)Math.cos(Math.toRadians((Math.acos(x/distance)/Math.PI*180)+this.shootDeviation))*MOVE_SPEED;
        moveOnY = (float)Math.sin(Math.toRadians((Math.asin(y/distance)/Math.PI*180)+this.shootDeviation))*MOVE_SPEED;
        if(mouseY < getCenterY()){
            moveOnY = -moveOnY;
        }
        if(mouseX < getCenterX()){
            moveOnX = -moveOnX;
        }
    }

    private void setDistanceDeviation(){
        if(distance>500){
            distance += (Math.random()*30-15);
        }else if(distance >200){
            distance += (Math.random()*16-8);
        }
    }

    protected double setRange(){
        return distance -= MOVE_SPEED;
    }

    protected void move(){
        offSet(moveOnX,moveOnY);
    }

    public void offSet(final double x, final double y) {
        this.x += x;
        this.y += y;
    }

    public double getCenterX() {
        return (this.x  + this.x+this.width) / 2;
    }

    public double getCenterY() {
        return (this.y  + this.y+this.height) / 2;
    }

    public double left() {
        return this.x;
    }

    public double right() {
        return this.x + this.width;
    }

    public double top() {
        return this.y;
    }

    public double bottom() {
        return this.y + this.height;
    }

    public boolean isCollied(final GameObject object) {
        float x = (float)Math.abs(object.collider().centerX()-getCenterX());
        float y = (float)Math.abs(object.collider().centerY()-getCenterY());
        float d = (float)Math.sqrt(x*x+y*y);//計算斜邊
        if(d < (object.collider().width()+width)/2){
            return true;
        }
        return false;
    }

    public boolean isOut() {
        if(setRange()<0){
            return true;
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,(int)left(),(int)top(),null);
    }

    @Override
    public void update() {
        move();
    }

    private float setShootDeviation(int max,int min){
        float temp = (float) (Math.random() * (max - min + 1) + min);
        if(temp == 0){
            return 1;
        }
        return temp;
    }

}

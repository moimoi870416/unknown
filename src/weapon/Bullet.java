package weapon;

import controller.ImageController;
import object.GameObject;
import object.monster.Monster;
import unit.GameKernel;
import java.awt.*;

public class Bullet implements GameKernel.PaintInterface, GameKernel.UpdateInterface{

    private float x;
    private float y;
    private float width = 5;
    private float height = 5;
    private float moveOnX;//X方向位移
    private float moveOnY;//Y方向位移
    private int MOVE_SPEED;
    protected float shootDeviation;//射擊偏差(預設為0=無偏差)
    private float distance;
    private int atk;
    private Image img;
    private int flyingDistance;

    public Bullet(final int x, final int y,int mouseX,int mouseY,int moveSpeed,int atk,int flyingDistance,float shootDeviation) {
        img = ImageController.getInstance().tryGet("/bullet.png");
        this.x = x;
        this.y = y;
        this.MOVE_SPEED = moveSpeed;
        this.atk = atk;
        this.flyingDistance = flyingDistance;
        this.shootDeviation = shootDeviation;
        setAngle(mouseX,mouseY);
        setDistanceDeviation();
    }

    private void setAngle(int mouseX,int mouseY){
        int x = (int)Math.abs(mouseX-getCenterX());
        int y = (int)Math.abs(mouseY-getCenterY());
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
            flyingDistance += (Math.random()*30-15);
        }else if(distance >200){
            flyingDistance += (Math.random()*16-8);
        }
    }

    protected double setRange(){
        return flyingDistance -= MOVE_SPEED;
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

    public boolean isCollied(final Monster monster) {
        float x = (float)Math.abs(monster.collider().centerX()-getCenterX());
        float y = (float)Math.abs(monster.collider().centerY()-getCenterY());
        float d = (float)Math.sqrt(x*x+y*y);//計算斜邊
        if(d < (monster.collider().width()+width)/2){
            monster.setLife(monster.getLife()-atk);
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

}
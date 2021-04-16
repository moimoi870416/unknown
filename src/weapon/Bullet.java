package weapon;

import controller.ImageController;
import object.GameObjForAnimator;
import object.GameObject;
import object.monster.Monster;
import util.Animator;
import util.GameKernel;
import java.awt.*;
import static util.Global.*;

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
    private int penetration;
    private Image img;
    private int flyingDistance;
    private BulletType bulletType;
    private State state;
    private Animator hitAnimator;
    private int hitX;
    private int hitY;
    private boolean isHit;

    public Bullet(final int x, final int y, int mouseX,int mouseY, Gun.GunType gunType) {
        img = ImageController.getInstance().tryGet("/weapon/bullet.png");
        this.x = x;
        this.y = y;
        setGunBullet(gunType);
        this.atk = bulletType.atk;
        this.MOVE_SPEED = bulletType.speedMove;
        this.flyingDistance = bulletType.flyingDistance;
        if(shootCount <=3){
            shootDeviation = 0;
        }else if(shootCount > 3){
            this.shootDeviation = setShootDeviation(bulletType.shootDeviationMax-2, bulletType.shootDeviationMix+2);
        }else if(shootCount > 7){
            this.shootDeviation = setShootDeviation(bulletType.shootDeviationMax, bulletType.shootDeviationMix);
        }
        if(bulletType == BulletType.SNIPER){
            shootDeviation = 0;
        }
        this.penetration = bulletType.penetration;
        state = State.FLYING;
        setAngle(mouseX,mouseY);
        setDistanceDeviation();
        hitAnimator = new Animator("/weapon/blood(100-100).png",0,100,100,2);
        hitAnimator.setArr(14);
        isHit = false;
    }

    public enum State{
        FLYING,
        HIT,
        STOP
    }

    public State getState(){
        return state;
    }

    private enum BulletType{
        PISTOL(10,10,200,2,-2,50),
        UZI(19,15,400,7,-7,50),
        AK(31,12,550,3,-3,50),
        SNIPER(120,30,1000,0,0,300),
        MACHINE_GUN(25,15,550,5,-5,100);

        private int atk;//攻擊力
        private int speedMove;//子彈的速度
        private int flyingDistance;//射程
        private int shootDeviationMax;
        private int shootDeviationMix;
        private int penetration;//穿透力

        BulletType(int atk,int speedMove,int flyingDistance,int shootDeviationMax,int shootDeviationMin,int penetration){
            this.atk = atk;
            this.speedMove = speedMove;
            this.flyingDistance = flyingDistance;
            this.penetration = penetration;
            this.shootDeviationMax = shootDeviationMax;
            this.shootDeviationMix = shootDeviationMin;

        }

    }
    private void setGunBullet(Gun.GunType gunType){
        bulletType = BulletType.values()[gunType.ordinal()];
    }

    private float setShootDeviation(int max,int min){
        return (float)(Math.random() * (max - min + 1) + min);
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
        if(state != State.FLYING){
            return;
        }
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
            return true;
        }
        return false;
    }

    public boolean isOut() {
        if(state != State.FLYING){
            return false;
        }
        if(setRange()<0){
            return true;
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        if(state == State.FLYING) {
            g.drawImage(img, (int) left(), (int) top(), null);
            return;
        }
        hitAnimator.paintAnimator(g,hitX-30,hitX+70,hitY-50,hitY+50, GameObjForAnimator.Dir.LEFT);

    }

    @Override
    public void update() {
        move();
        if(hitAnimator.isFinish()){
            state = State.STOP;
        }
    }

    public int getAtk(){
        return atk;
    }

    public boolean isPenetrate(int monsterLife){
        penetration -= monsterLife;
        if(penetration <= 0){
            if(!isHit){
                state = State.HIT;
                setHitXAndY();
                hitAnimator.setPlayOnce();
            }
            return true;
        }
        return false;
    }

    private void setHitXAndY(){
        hitX = (int)left();
        hitY = (int)top();
        isHit = true;
    }
}

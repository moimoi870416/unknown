package bullet;

import object.GameObject;
import unit.GameKernel;

public abstract class Bullet implements GameKernel.PaintInterface, GameKernel.UpdateInterface{

    private double x;
    private double y;
    private double width;
    private double height;
    protected double mouseX;//滑鼠X位置
    protected double mouseY;//滑鼠Y位置
    private double moveOnX;//X方向位移
    private double moveOnY;//Y方向位移
    //protected double originalX;//初始X位置
    //protected double originalY;//初始Y位置
    private int MOVE_SPEED;
    protected double shootDeviation;//射擊偏差(預設為1=無偏差)
    private double distance;

    public Bullet(final int x, final int y, final int width, final int height,int mouseX,int mouseY,int moveSpeed,double shootDeviation) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.shootDeviation = shootDeviation;
        if(shootDeviation == 0){
            this.shootDeviation =1;
        }
        //originalX = getCenterX();
        //originalY = getCenterY();
        MOVE_SPEED = moveSpeed;
        if(moveSpeed == 0){
            MOVE_SPEED = 10;
        }
        setAngle();
        setDistanceDeviation();
    }

    private void setAngle(){
        double x = Math.abs(mouseX-getCenterX());
        double y = Math.abs(mouseY-getCenterY());
        distance = Math.sqrt(x*x+y*y);//計算斜邊
        moveOnX = Math.cos(Math.toRadians((Math.acos(x/distance)/Math.PI*180)+this.shootDeviation))*MOVE_SPEED;
        moveOnY = Math.sin(Math.toRadians((Math.asin(y/distance)/Math.PI*180)+this.shootDeviation))*MOVE_SPEED;
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
        /*
        if(clickOnY || clickOnX) {
            if(clickOnY) {
                if (mouseX > originalX) {
                    offSetX(MOVE_SPEED);
                } else if (mouseX < originalX) {
                    offSetX(-MOVE_SPEED);
                }
            }
            if(clickOnX) {
                if (mouseY < originalY) {
                    offSetY(-MOVE_SPEED);
                } else if (mouseY > originalY) {
                    offSetY(MOVE_SPEED);
                }
            }
            return;
        }

         */
        offSet(moveOnX,moveOnY);
    }

    public void offSetX(final double x) {
        this.x += x;
    }

    public void offSetY(final double y) {
        this.y += y;
    }

    public void offSet(final double x, final double y) {
        this.x += x;
        this.y += y;
    }

    public double getCenterX() {
        return (this.x + this.width) / 2;
    }

    public double getCenterY() {
        return (this.y + this.height) / 2;
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
        if (left() > object.right()) {
            return false;
        }
        if (right() < object.left()) {
            return false;
        }
        if (top() > object.bottom()) {
            return false;
        }
        if (bottom() < object.top()) {
            return false;
        }
        return true;
    }

    public abstract boolean isOut();

}

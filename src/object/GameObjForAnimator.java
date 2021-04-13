package object;

import java.awt.*;

public class GameObjForAnimator extends GameObject {
    protected Animator animator;
    protected Dir dir;
    protected State state;
    protected int life;
    protected int atk;
    protected int moveSpeed;

    public GameObjForAnimator(String path,int countLimit, int x, int y, int width, int height,int life,int atk,int moveSpeed) {
        this(path,countLimit,x, y, width, height,x,y,width,height,life,atk,moveSpeed);
    }

    public GameObjForAnimator(String path,int countLimit, int x, int y, int width, int height, int x2, int y2, int width2, int height2,int life,int atk,int moveSpeed) {
        super(x, y, width, height,x2,y2,width2,height2);
        animator = new Animator(path,countLimit);
        this.life = life;
        this.atk = atk;
        this.moveSpeed = moveSpeed;
        state = State.ALIVE;
        dir = Dir.LEFT;
    }



    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
    }

    @Override
    public void update() {

    }

    public enum State{
        ALIVE,
        ATTACK,
        DEAD,
    }

    public enum Dir {
        LEFT,
        RIGHT,
    }

    protected void changeDir(double moveOnX) {
        if (moveOnX > 0) {
            dir = Dir.LEFT;
        } else {
            dir = Dir.RIGHT;
        }
    }

    protected void changeDir(int moveOnX) {
        if (moveOnX > 0) {
            dir = Dir.LEFT;
        } else {
            dir = Dir.RIGHT;
        }
    }

    public Animator getAnimator() {
        return animator;
    }

    public void offLife(int atk){
        this.life -= atk;
        if(life <=0){
            state = State.DEAD;
        }
    }

    public int getLife(){
        return life;
    }

    public int getAtk() {
        return atk;
    }

    public Dir getDir(){
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    //    public boolean isCollisionWithActor(GameObjForAnimator other) {
//            int distanceX = this.collider().centerX() - other.collider().centerX();
//            int distanceY = this.collider().centerY() - other.collider().centerY();
//            double distance = Math.sqrt(distanceX ^ 2 + distanceY ^ 2);
//            if (distance < (this.collider().width()/2 + other.collider().width()/2 )) {
//                return true;
//            }
//        return false;
//    }

    public boolean isCollisionWithActor(GameObjForAnimator other) {
        if (Math.abs(this.collider().centerX() - other.collider().centerX()) < 50 && Math.abs(this.collider().centerY() - other.collider().centerY()) < 50) {
            return true;
        }
        return false;
    }
}

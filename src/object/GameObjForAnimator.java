package object;

import java.awt.*;

public class GameObjForAnimator extends GameObject {
    protected Animator animator;
    protected Dir dir;
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
        dir = Dir.LEFT;
    }



    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(),dir);
    }

    @Override
    public void update() {

    }

    public enum Dir{
        LEFT,
        RIGHT,
        DEAD
    }

    protected void changeDir(double moveOnX){
        if(moveOnX>0){
            dir = Dir.LEFT;
        }else {
            dir = Dir.RIGHT;
        }
    }

    protected void changeDir(int moveOnX){
        if(moveOnX>0){
            dir = Dir.LEFT;
        }else {
            dir = Dir.RIGHT;
        }
    }

    public Animator getAnimator(){
        return animator;
    }

    public void setLife(int life){
        if(life < 0){
            animator.setDelay().stop();
            animator.setDelay().play();
            dir = Dir.DEAD;
        }
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Dir getDir(){
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }
}

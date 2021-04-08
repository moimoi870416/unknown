package object;

import java.awt.*;

public class GameObjForAnimator extends GameObject {
    protected Animator animator;
    protected Dir dir;
    protected int life;
    protected int atk;
    protected int moveSpeed;

    public GameObjForAnimator(String path,int countLimit, int x, int y, int width, int height,int life,int atk,int moveSpeed) {
        super(x, y, width, height);
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

    protected enum Dir{
        LEFT,
        RIGHT,
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
}

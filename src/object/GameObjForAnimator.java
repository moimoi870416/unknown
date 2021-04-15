package object;

import util.Animator;

import java.awt.*;

public abstract class GameObjForAnimator extends GameObject {
    protected Animator animator;
    protected Dir dir;
    protected int life;
    protected int atk;
    protected int moveSpeed;
    protected State state;
    protected boolean isDie;

    public GameObjForAnimator(int x, int y, int width, int height,int life,int atk,int moveSpeed) {
        this(x, y, width, height,x,y,width,height,life,atk,moveSpeed);
    }

    public GameObjForAnimator(int x, int y, int width, int height, int x2, int y2, int width2, int height2,int life,int atk,int moveSpeed) {
        super(x, y, width, height,x2,y2,width2,height2);
        this.life = life;
        this.atk = atk;
        this.moveSpeed = moveSpeed;
        dir = Dir.LEFT;
        state = State.ALIVE;
        isDie = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
    }

    public enum State{
        ALIVE,
        DEATH,
        DEAD
    }

    @Override
    public void update() {

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

    public void offLife(int atk){
        this.life -= atk;
    }

    public int getLife(){
        return life;
    }

    public int getAtk() {
        return atk;
    }

    public boolean isCollisionWithActor(GameObjForAnimator other) {
        if (Math.abs(this.collider().centerX() - other.collider().centerX()) < 50 && Math.abs(this.collider().centerY() - other.collider().centerY()) < 50) {
            return true;
        }
        return false;
    }
}

package object;

import controller.ConnectController;
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
    private GameObject nearestObj;
    protected int connectID;

    public GameObjForAnimator(int x, int y, int width, int height, int life, int atk, int moveSpeed) {
        this(x, y, width, height, x, y, width, height, life, atk, moveSpeed);
    }

    public GameObjForAnimator(int x, int y, int width, int height, int x2, int y2, int width2, int height2, int life, int atk, int moveSpeed) {
        super(x, y, width, height, x2, y2, width2, height2);
        this.life = life;
        this.atk = atk;
        this.moveSpeed = moveSpeed;
        dir = Dir.RIGHT;
        setState(State.STAND);
        isDie = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (state == State.DEAD) {
            return;
        }
        if (state == State.DEATH) {
            if (animator.isFinish()) {
                state = State.DEAD;
            }
        }
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
    }

    public enum State {
        STAND,
        WALK,
        RUN,
        ATTACK,
        CRITICAL,
        DEATH,
        DEAD
    }

    public void setState(State state){
        if(this.state == state){
            if(!animator.isFinish()){
                return;
            }

        }
        this.state = state;
        if(animator == null){
            return;
        }
        setStateComponent();
    }

    protected abstract void setStateComponent();

    public State getState() {
        return state;
    }

    @Override
    public abstract void update();

    public enum Dir {
        RIGHT,
        LEFT,
    }

    protected void changeDir(double moveOnX) {
        if (moveOnX > 0) {
            dir = Dir.RIGHT;
        } else {
            dir = Dir.LEFT;
        }
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    protected void changeDir(int moveOnX) {
        if (moveOnX > 0) {
            dir = Dir.RIGHT;
        } else {
            dir = Dir.LEFT;
        }
    }

    public Dir getDir() {
        return dir;
    }

    public void offLife(int atk) {
        this.life -= atk;
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

    public boolean isCollisionWithActor(GameObjForAnimator other) {
        if (Math.abs(this.collider().centerX() - other.collider().centerX()) < 50 && Math.abs(this.collider().centerY() - other.collider().centerY()) < 50) {
            return true;
        }
        return false;
    }

    //判斷四面碰撞
    private boolean topIsCollision(GameObject obj) {
        return obj.collider().bottom() >= collider().top() &&
                obj.collider().left() < collider().right() &&
                obj.collider().right() > collider().left() &&
                obj.collider().bottom() < collider().bottom() &&
                collider().right() - obj.collider().left() > obj.collider().bottom() - collider().top() &&
                obj.collider().right() - collider().left() > obj.collider().bottom() - collider().top();
    }

    private boolean leftIsCollision(GameObject obj) {
        return obj.collider().right() >= collider().left() &&
                obj.collider().bottom() > collider().top() &&
                obj.collider().top() < collider().bottom() &&
                obj.collider().right() < collider().right();
    }

    private boolean rightIsCollision(GameObject obj) {
        return obj.collider().left() <= collider().right() &&
                obj.collider().bottom() > collider().top() &&
                obj.collider().top() < collider().bottom() &&
                obj.collider().left() > collider().left();
    }

    private boolean bottomIsCollision(GameObject obj) {
        return obj.collider().top() <= collider().bottom() &&
                obj.collider().left() < collider().right() &&
                obj.collider().right() > collider().left() &&
                obj.collider().top() > collider().top() &&
                collider().right() - obj.collider().left() > collider().bottom() - obj.collider().top() &&
                obj.collider().right() - collider().left() > collider().bottom() - obj.collider().top();
    }


    @Override
    public CollisionDir isCollider(GameObject obj) {
        if (isCollision(obj)) {
            nearestObj = obj;
            if (topIsCollision(obj)) {
                offSetY(obj.collider().bottom());
                return CollisionDir.UP;
            }
            if (bottomIsCollision(obj)) {
                offSetY(obj.collider().top() - collider().height());
                return CollisionDir.DOWN;
            }
            if (leftIsCollision(obj)) {
                offSetX(obj.collider().right());
                return CollisionDir.LEFT;
            }
            if (rightIsCollision(obj)) {
                offSetX(obj.collider().left() - collider().width());
                return CollisionDir.RIGHT;
            }
        }
        return CollisionDir.NO;
    }


}

package object;

import java.awt.*;

public class GameObjForAnimator extends GameObject {
    protected Animator animator;
    protected Dir dir;

    public GameObjForAnimator(String path,int countLimit, int x, int y, int width, int height) {
        super(x, y, width, height);
        animator = new Animator(path,countLimit);
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
}

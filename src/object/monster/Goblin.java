package object.monster;

import object.Animator;

import java.awt.*;

public class Goblin extends Monster {


    public Goblin(int x, int y) {
        super("/monster/goblin.png",15,x, y, 32, 32,100,10,2);
        animator.setAnimatorSize(16);
        animator.setACTOR_WALK(new int[]{0,1,2,3,4,5});
    }


    @Override
    protected void setAnimator(String path, int countLimit) {
        animator = new Animator(path,countLimit) {
            @Override
            public void paintAnimator(Graphics g, int left, int right, int top, int bottom) {
                if (delay.count()) {
                    this.count = ++this.count % ACTOR_WALK.length;
                }

                g.drawImage(this.img, left, top, right , bottom
                        , animatorSize *ACTOR_WALK[this.count]
                        , animatorSize * dir.ordinal()
                        , animatorSize+ animatorSize * ACTOR_WALK[this.count]
                        , animatorSize + animatorSize * dir.ordinal()
                        , null);
            }
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        if (state == State.ALIVE) {
            animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom());
            return;
        }

    }
}

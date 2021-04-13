package object.monster;

import object.Animator;

import java.awt.*;

public class BullBoss extends Monster {

    public BullBoss(int x, int y) {
        super("/monster/bullboss.png",15,x+30,y+30,132,132,x, y, 192, 192,1000,51,2);
        animator.setAnimatorSize(96);
        animator.setACTOR_WALK(new int[]{0,1,2,3,4});
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
        animator.paintAnimator(g,painter().left(), painter().right(), painter().top(), painter().bottom());
    }



}

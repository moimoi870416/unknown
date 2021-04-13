package object.animator;

import object.GameObjForAnimator;

import java.awt.*;

public class GoblinAnimator extends Animator{
    private final int[] ACTOR_WALK = {0,1,2,3,4,5} ;
    public GoblinAnimator(String path, int countLimit, int size) {
        super(path, countLimit, size);
    }

    @Override
    public void paintAnimator(Graphics g, int left, int right, int top, int bottom, GameObjForAnimator.Dir dir) {
        if (this.delay.count()) {
            this.count = ++this.count % ACTOR_WALK.length;
        }

        g.drawImage(this.img, left, top, right , bottom
                , animatorSize *ACTOR_WALK[this.count]
                , animatorSize * dir.ordinal()
                , animatorSize+ animatorSize * ACTOR_WALK[this.count]
                , animatorSize + animatorSize * dir.ordinal()
                , null);
    }
}

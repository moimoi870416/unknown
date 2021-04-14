package object.animator;

import object.GameObjForAnimator;
import java.awt.*;

public class BullBossAnimator extends Animator{
    private State state;

    public BullBossAnimator(String path, int countLimit, int size) {
        super(path, countLimit, size);
        state = State.INTIMIDATE;
    }

    @Override
    public void paintAnimator(Graphics g, int left, int right, int top, int bottom, GameObjForAnimator.Dir dir) {
        if (this.delay.count()) {
            this.count = ++this.count % state.getActiveArr().length;
        }
        g.drawImage(this.img, left, top, right , bottom
                , animatorSize * state.getActiveArr()[this.count]
                , animatorSize * dir.ordinal()* 10 + animatorSize * state.ordinal()
                , animatorSize+ animatorSize * state.getActiveArr()[this.count]
                , animatorSize + animatorSize * dir.ordinal() *10 + animatorSize * state.ordinal()
                , null);
    }

    public void setState(State state){
        this.state = state;
        count = 0;
    }

    private enum State{
        STAND(new int[] {0,1,2,3,4}),
        WALK(new int[] {0,1,2,3,4,5,6,7}),
        RUN(new int[] {0,1,2,3,4}),
        ATTACK1(new int[] {0,1,2,3,4,5,6,7,8}),
        BREATH(new int[] {0,1,2,3,4}),
        INTIMIDATE(new int[] {0,1,2,3,4,5}),
        ATTACK2(new int[] {0,1,2,3,4,5,6,7,8}),
        SLEEP(new int[] {0,1,2}),
        DEATH(new int[] {0,1,2}),
        DEAD(new int[] {0,1,2,3,4,5});

        int[] active;

        private int[] getActiveArr(){
            return active;
        }

        State(int[] arr){
            active = arr;
        }
    }
}

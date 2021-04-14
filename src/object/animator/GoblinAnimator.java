package object.animator;

import controller.ImageController;
import object.GameObjForAnimator;

import java.awt.*;

public class GoblinAnimator extends Animator{
    private State state;
    public GoblinAnimator(String path, int countLimit, int size) {
        super(path, countLimit, size);
        state = State.RUN;
        deadCount = State.DEATH.active.length;
    }

    @Override
    public void paintAnimator(Graphics g, int left, int right, int top, int bottom, GameObjForAnimator.Dir dir) {
        if (this.delay.count()) {
            this.count = ++this.count % state.getActiveArr().length;
        }

        g.drawImage(this.img, left, top, right , bottom
                , animatorSize * state.getActiveArr()[this.count]
                , animatorSize * dir.ordinal()
                , animatorSize+ animatorSize * state.getActiveArr()[this.count]
                , animatorSize + animatorSize * dir.ordinal()
                , null);
        if(deadCount-- == 0){
            state = State.DEAD;
        }
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
        switch (this.state){
            case RUN:
                img = ImageController.getInstance().tryGet("/monster/goblin/goblin.png");
                break;
            case DEATH:
                img = ImageController.getInstance().tryGet("/monster/goblin/goblindead.png");
                break;
        }
        count = 0;
    }

    private enum State {
        RUN(new int[]{0,1,2,3,4,5}),
        DEATH(new int[]{0, 1, 2}),
        DEAD(new int[]{});

        int[] active;
        private int[] getActiveArr(){
            return active;
        }

        State(int[] arr){
            active = arr;
        }
    }
}

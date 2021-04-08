package object;

import controller.ImageController;
import unit.Delay;
import unit.Global;

import java.awt.*;

public class Animator {
    private Image img;
    private int count;
    private Delay delay;
    private int[] ACTOR_WALK;
    private int animatorSize;

    public Animator(String path,int countLimit) {
        this.img = ImageController.getInstance().tryGet(path);
        delay = new Delay(countLimit);
        count = 0;
        animatorSize = Global.UNIT_Y;
    }

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

    public void setACTOR_WALK(int[] arr){
        ACTOR_WALK = arr;
    }

    public void setAnimatorSize(int size){
        animatorSize = size;
    }

    public Delay setDelay(){
        return delay;
    }
}

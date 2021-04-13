package object;

import controller.ImageController;
import util.Delay;
import util.Global;

import java.awt.*;

public abstract class Animator {
    protected Image img;
    protected int count;
    protected Delay delay;
    protected int[] ACTOR_WALK;
    protected int animatorSize;

    public Animator(String path,int countLimit) {
        this.img = ImageController.getInstance().tryGet(path);
        delay = new Delay(countLimit);
        delay.loop();
        count = 0;
        animatorSize = Global.UNIT_Y;
    }

    public abstract void paintAnimator(Graphics g, int left, int right, int top, int bottom);

    public void setAnimatorSize(int size){
        this.animatorSize = size;
    }

    public void setACTOR_WALK(int[] arr){
        ACTOR_WALK = arr;
    }

    public Delay setDelay(){
        return delay;
    }



}

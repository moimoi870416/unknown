package object.animator;

import controller.ImageController;
import object.GameObjForAnimator;
import util.Delay;
import java.awt.*;

public abstract class Animator {
    protected Image img;
    protected int count;
    protected Delay delay;
    protected int animatorSize;

    public Animator(String path,int countLimit,int size) {
        this.img = ImageController.getInstance().tryGet(path);
        delay = new Delay(countLimit);
        delay.loop();
        count = 0;
        animatorSize = size;
    }

    public abstract void paintAnimator(Graphics g, int left, int right, int top, int bottom, GameObjForAnimator.Dir dir);



    public void setImg(String img,boolean isLoop){
        delay.stop();
        this.img = ImageController.getInstance().tryGet(img);
        if(isLoop){
            delay.loop();
            return;
        }
        delay.play();
    }

}

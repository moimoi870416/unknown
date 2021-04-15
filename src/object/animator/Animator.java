package object.animator;

import controller.ImageController;
import object.GameObjForAnimator;
import util.Delay;
import java.awt.*;

public class Animator {
    private Image img;
    private int count;
    private Delay delay;
    private int animatorSize;
    private int[] arr;
    private int picSize;
    private int height;

    public Animator(String path,int countLimit,int size,int picSize) {
        this.img = ImageController.getInstance().tryGet(path);
        delay = new Delay(countLimit);
        delay.loop();
        count = 0;
        animatorSize = size;
        this.picSize = picSize /2;//圖片高度的動作/2
    }

    public void paintAnimator(Graphics g, int left, int right, int top, int bottom, GameObjForAnimator.Dir dir){
        if (this.delay.count()) {
            this.count = ++this.count % arr.length;
        }
        g.drawImage(this.img, left, top, right , bottom
                , animatorSize * arr[this.count]
                , animatorSize * dir.ordinal() * this.picSize
                , animatorSize+ animatorSize * arr[this.count]
                , animatorSize + animatorSize * dir.ordinal() * this.picSize
                , null);
    }

    public void setArr(int number){
        setArr(number,0);
    }
    public void setArr(int number,int height){
        int[] arr = new int[number];
        for(int i=0 ; i<number ; i++){
            arr[i] = i;
        }
        this.arr = arr;
        this.height = height-1;
        this.count = 0;
    }

    public void setImg(String path,int picSize){
        this.img = ImageController.getInstance().tryGet(path);
        this.picSize = picSize/2;
        this.count = 0;
    }


}

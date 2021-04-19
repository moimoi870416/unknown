package util;

import controller.ImageController;
import object.GameObjForAnimator;
import util.Delay;
import java.awt.*;

public class Animator {
    private Image img;
    private int count;
    private Delay delay;
    private int widthSize;
    private int heightSize;
    private int[] arr;
    private int picSize;
    private int height;
    private boolean playOnce;
    private int playOnceCount;
    private boolean isFinish;

    public Animator(String path,int countLimit,int widthSize,int heightSize,int picSize) {
        this.img = ImageController.getInstance().tryGet(path);
        delay = new Delay(countLimit);
        delay.loop();
        count = 0;
        this.widthSize = widthSize;
        this.heightSize = heightSize;
        this.picSize = picSize /2;//圖片高度的動作/2
        playOnceCount = 0;
        playOnce = false;
        isFinish = false;
    }

    public void paintAnimator(Graphics g, int left, int right, int top, int bottom, GameObjForAnimator.Dir dir){
        if(playOnceCount >= 0) {
            if (this.delay.count()) {
                this.count = ++this.count % arr.length;
                if(playOnce){
                    playOnceCount--;
                    if(playOnceCount < 0){
                        isFinish = true;
                    }
                }
            }
            g.drawImage(this.img, left, top, right, bottom
                    , widthSize * arr[this.count]
                    , heightSize * dir.ordinal() * this.picSize + heightSize * height
                    , widthSize + widthSize * arr[this.count]
                    , heightSize + heightSize * dir.ordinal() * this.picSize + heightSize * height
                    , null);
        }
    }

    public void setArr(int number){
        setArr(number,1);
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

    public void setDelayCount(int delayCount){
        delay = new Delay(delayCount);
        delay.loop();
        count = 0;
    }

    public void setPlayOnce(){
        playOnceCount = arr.length-2;
        playOnce = true;
        count = 0;
    }

    public void setPlayLoop(){
        playOnceCount = arr.length;
        playOnce = false;
        count = 0;
    }

    public boolean isFinish(){
        return playOnce && isFinish;
    }

    public void setWidthAndHeightSize(int widthSize,int heightSize){
        this.widthSize = widthSize;
        this.heightSize = heightSize;
    }


}

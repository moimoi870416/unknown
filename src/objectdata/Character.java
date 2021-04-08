package objectdata;

import controller.ImageController;
import unit.Delay;

import java.awt.*;

public abstract class Character extends GameObject {
    protected int[] ACTOR_WALK;
    protected Image img;
    protected int count;
    protected Delay delay;

    public Character(int x, int y, int width, int height,String path) {
        super(x, y, width, height);
        this.img = ImageController.getInstance().tryGet(path);
        this.count = 0;
    }
    public abstract void paintAnimator(final Graphics g, final int num, final int left, final int right, final int top, final int bottom);

}

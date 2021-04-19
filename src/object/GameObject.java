package object;

import camera.MapInformation;
import util.Global;
import util.GameKernel;
import java.awt.*;

public abstract class GameObject implements GameKernel.PaintInterface, GameKernel.UpdateInterface {

    //判斷碰撞的匡
    private Rect collider;
    //畫圖的匡
    private Rect painter;

    public GameObject(int x, int y, int width, int height) {
        this(x, y, width, height, x, y, width, height);
    }

    public GameObject(Rect rect) {
        collider = new Rect(rect);
        painter = new Rect(rect);
    }

    public GameObject(int x, int y, int width, int height,
                      int x2, int y2, int width2, int height2) {
        collider = Rect.genWithCenter(x, y, width, height);
        painter = Rect.genWithCenter(x2, y2, width2, height2);
    }

    public GameObject(Rect rect, Rect rect2) {
        collider = new Rect(rect);
        painter = new Rect(rect2);
    }

    public final Rect collider() {
        return collider;
    }

    public final Rect painter() {
        return painter;
    }

    //超過境頭視野就不用畫
    public boolean outOfScreen() {
        if (painter.bottom() <= 0) {
            return true;
        }
        if (painter.right() <= 0) {
            return true;
        }
        if (painter.left() >= MapInformation.mapInfo().right()) {
            return true;
        }
        return painter.top() >= MapInformation.mapInfo().bottom();
    }

    protected boolean isOut(){
        if (collider().bottom() > Global.MAP_HEIGHT - 70) {
            offSetY(Global.MAP_HEIGHT - 70-collider().height());
            return true;
        }
        if (collider().right() > Global.MAP_WIDTH) {
            offSetX(Global.MAP_WIDTH-collider().width());
            return true;
        }
        if (collider().left() < 0) {
            offSetX(0);
            return true;
        }
        return false;


    }

    public boolean touchTop() {
        return collider.top() <= 0;
    }

    public boolean touchLeft() {
        return collider.left() <= 0;
    }

    public boolean touchRight() {
        return collider.right() >= MapInformation.mapInfo().right();
    }

    public boolean touchBottom() {
        return collider.bottom() >= MapInformation.mapInfo().bottom();
    }

    //是否在境頭視野內
    public boolean isCollision(GameObject obj) {
        return collider.overlap(obj.collider);
    }

    public enum CollisionDir{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NO
    }

    public abstract CollisionDir isCollider(GameObject obj);

    //移動 x及y的位置
    public void translate(int x, int y) {
        collider.translate(x, y);
        painter.translate(x, y);
    }

    public final void translateX(int x) {
        collider.translateX(x);
        painter.translateX(x);
    }

    public final void translateY(int y) {
        collider.translateY(y);
        painter.translateY(y);
    }

    public final void offSetX(int x){
        int value = collider.left() - painter.left();
        int cW = collider.width();
        int pW = painter.width();
        collider.setLeft(x);
        collider.setRight(x+cW);
        painter.setLeft(x-value);
        painter.setRight(x-value+pW);
    }

    public final void offSetY(int y){
        int value = collider.top() - painter.top();
        int cH = collider.height();
        int pH = painter.height();
        collider.setTop(y);
        collider.setBottom(y+cH);
        painter.setTop(y-value);
        painter.setBottom(y-value+pH);
    }

    public final void offSet(int x,int y){
        int valueX = collider.left() - painter.left();
        collider.setLeft(x);
        painter.setLeft(x-valueX);
        int valueY = collider.top() - painter.top();
        collider.setTop(y);
        painter.setTop(y-valueY);
    }

    @Override
    public final void paint(Graphics g) {
        paintComponent(g);
        if (Global.IS_DEBUG) {
<<<<<<< HEAD
            g.drawString(this.painter.left() + "," + this.painter.top(), this.painter.left() + 5, this.painter.top() + 12);
            g.drawString(this.painter.right() + "," + this.painter.bottom(), this.painter.left() + 5, this.painter.top() + 27);
            g.setColor(Color.RED);
=======
//            g.drawString(this.painter.centerX()+ "," + this.painter.centerY(), this.painter.left() + 5, this.painter.top() + 12);
//            g.drawString(this.painter.width() + "," + this.painter.height(), this.painter.left() + 5, this.painter.top() + 27);
//            g.setColor(Color.RED);
>>>>>>> 旋轉
            g.drawRect(this.painter.left(), this.painter.top(), this.painter.width(), this.painter.height());
            g.setColor(Color.BLUE);
            g.drawRect(this.collider.left(), this.collider.top(), this.collider.width(), this.collider.height());
            g.setColor(Color.BLACK);
        }
    }

    public abstract void paintComponent(Graphics g);

}

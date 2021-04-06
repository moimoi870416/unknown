package object;

import bullet.Bullet;
import unit.GameKernel;

public abstract class GameObject implements GameKernel.PaintInterface, GameKernel.UpdateInterface {

    private int x;
    private int y;
    private int width;
    private int height;

    public GameObject(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public void offSetX(final int x) {
        this.x += x;
    }

    public void offSetY(final int y) {
        this.y += y;
    }

    public void offSet(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    public void offSet(final double x, final double y) {
        this.x += x;
        this.y += y;
    }

    public int getCenterX() {
        return (this.x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.y + this.height) / 2;
    }

    public int left() {
        return this.x;
    }

    public int right() {
        return this.x + this.width;
    }

    public int top() {
        return this.y;
    }

    public int bottom() {
        return this.y + this.height;
    }

    public boolean isCollied(final GameObject object) {
        if (left() > object.right()) {
            return false;
        }
        if (right() < object.left()) {
            return false;
        }
        if (top() > object.bottom()) {
            return false;
        }
        if (bottom() < object.top()) {
            return false;
        }
        return true;
    }

    public boolean isCollied(final Bullet bullet) {
        if (left() > bullet.right()) {
            return false;
        }
        if (right() < bullet.left()) {
            return false;
        }
        if (top() > bullet.bottom()) {
            return false;
        }
        if (bottom() < bullet.top()) {
            return false;
        }
        return true;
    }

    public abstract boolean isOut();
}

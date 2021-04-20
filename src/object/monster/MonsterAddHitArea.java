package object.monster;

import object.Rect;

public abstract class MonsterAddHitArea extends Monster {
    protected Rect hitCollied;

    public MonsterAddHitArea(int x, int y, int width, int height, int life, int atk, int moveSpeed, boolean isOnceAttack) {
        this(x, y, width, height, x, y, width, height, life, atk, moveSpeed, isOnceAttack);

    }

    public MonsterAddHitArea(int x, int y, int width, int height, int x2, int y2, int width2, int height2, int life, int atk, int moveSpeed, boolean isOnceAttack) {
        super(x, y, width, height, x2, y2, width2, height2, life, atk, moveSpeed, isOnceAttack);
        setHitCollied(x,y);
    }

    protected abstract void setHitCollied(int x,int y);

    @Override
    public void translate(int x, int y) {
        collider().translate(x, y);
        painter().translate(x, y);
        hitCollied.translate(x,y);
    }

    @Override
    public void translateX(int x) {
        collider().translateX(x);
        painter().translateX(x);
        hitCollied.translateX(x);
    }

    @Override
    public void translateY(int y) {
        collider().translateY(y);
        painter().translateY(y);
        hitCollied.translateY(y);
    }
}

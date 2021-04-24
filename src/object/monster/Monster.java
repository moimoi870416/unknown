package object.monster;

import controller.ConnectController;
import object.Rect;
import object.actor.GameActor;
import util.Delay;
import util.Global;
import object.GameObjForAnimator;

import java.awt.*;

public abstract class Monster extends GameObjForAnimator {
    private Delay delayForCollision;
    protected Delay attackDelay;
    private boolean collision;
    protected boolean canAttack;
    protected Delay delayForAttack;
    protected boolean isOnceAttack;
    protected boolean isChase;
    protected boolean clickAtk;
    protected boolean forRino;
    protected Rect hitCollied;
    protected int hitX;
    protected int hitY;
    protected GameActor gameActor;
    private float nearest;
    protected int atkType;

    public Monster(int x, int y, int width, int height, int life, int atk, int moveSpeed, boolean isOnceAttack,int typeCode) {
        this(x, y, width, height, x, y, width, height,x,y,width,height, life, atk, moveSpeed, isOnceAttack,typeCode);

    }

    public Monster(int x, int y, int width, int height, int x2, int y2, int width2, int height2,int hitX,int hitY,int hitWidth,int hitHeight, int life, int atk, int moveSpeed, boolean isOnceAttack,int typeCode) {
        super(x, y, width, height, x2, y2, width2, height2, life, atk, moveSpeed);
        attackDelay = new Delay(60);
        isChase = false;
        canAttack = true;
        delayForAttack = new Delay(90);
        this.delayForCollision = new Delay(10);
        collision = true;
        this.isOnceAttack = isOnceAttack;
        forRino = false;
        hitCollied = Rect.genWithCenter(hitX,hitY,hitWidth,hitHeight);
        this.hitX = hitCollied.left() - collider().left();
        this.hitY = hitCollied.top() -collider().top();
        nearest = 50000f;
        if(Global.isServer) {
            ConnectController.getInstance().newMonsterSend(this, typeCode);
        }
    }

    public void chase() {
        float x = Math.abs(gameActor.collider().centerX() - painter().centerX());
        float y = Math.abs(gameActor.collider().bottom() - painter().centerY()-10);
        if (x <= 20 && y <= 20) {
            return;
        }
        float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
        float moveOnX = (float) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * this.moveSpeed); //  正負向量
        float moveOnY = (float) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * this.moveSpeed);
        if (gameActor.collider().bottom() < painter().centerY()) {
            moveOnY = -moveOnY;
        }
        if (gameActor.collider().centerX() < painter().centerX()) {
            moveOnX = -moveOnX;
        }
        translate((int) moveOnX, (int) moveOnY);
        changeDir(moveOnX);
    }

    @Override
    public void update() {
        transHitArea();
        if (isOut()) {
            return;
        }
        if (state == State.DEATH) {
            isChase = false;
            return;
        }
        updateComponent();
        if (delayForCollision.count()) {
            collision = true;
        }
        if (isOnceAttack) {
            if (delayForAttack.count()) {
                canAttack = true;
            }
        }
        if(forRino){
            return;
        }
        if (isChase) {
            chase();
            return;
        }
        isSeeingActor();
    }

    public void whoIsNear(GameActor gameActor){
            float dx = Math.abs(gameActor.collider().centerX() - painter().centerX());
            float dy = Math.abs(gameActor.collider().bottom() - painter().centerY()-10);
            float dc = (float) Math.sqrt(dx * dx + dy * dy);//計算斜邊,怪物與人物的距離
            if(dc < nearest){
                nearest = dc;
                this.gameActor = gameActor;
            }

    }

    public void attack(GameObjForAnimator gameObjForAnimator) {
        if (isOnceAttack) {
            if (canAttack) {
                delayForAttack.play();
                gameObjForAnimator.offLife(this.atk);
                canAttack = false;
            }
            return;
        }
        if (this.isCollisionWithActor(gameObjForAnimator)) {
            delayForAttack.play();
            if (delayForAttack.count()) {
                gameObjForAnimator.offLife(this.atk);
            }
            return;
        }
        delayForAttack.stop();

    }

    protected abstract void updateComponent();

    protected void isSeeingActor() {
        if (Math.abs(Global.actorX - painter().centerX()) < Global.WINDOW_WIDTH / 2) {
            setState(State.RUN);
            isChase = true;
        }
    }


    public void isCollisionWithMonster(Monster other) {
        int r;
        if (collision) {
            delayForCollision.play();
            r = Global.random(0, 3);
            if (this.isCollision(other)) {
                switch (r) {
                    case 0:
                        translateX(moveSpeed * 2);
                        other.translateX(-moveSpeed * 2);
                        break;
                    case 1:
                        translateX(-moveSpeed * 2);
                        other.translateX(moveSpeed * 2);
                        break;
                    case 2:
                        translateY(moveSpeed * 2);
                        other.translateY(-moveSpeed * 2);
                        break;
                    case 3:
                        translateY(-moveSpeed * 2);
                        other.translateY(-moveSpeed * 2);
                        break;
                }
            }
            collision = false;
        }

    }

    public abstract void setState(State state);
    //public abstract String getType();

    public Rect getHitCollied(){
        return hitCollied;
    }

    @Override
    protected void paintDebug(Graphics g){
        g.setColor(Color.ORANGE);
        g.drawRect(this.hitCollied.left(), this.hitCollied.top(), this.hitCollied.width(), this.hitCollied.height());

    }

    private void transHitArea(){
        int width = hitCollied.width();
        int height = hitCollied.height();
        hitCollied.setLeft(collider().left()+this.hitX);
        hitCollied.setTop(collider().top()+this.hitY);
        hitCollied.setRight(hitCollied.left()+width);
        hitCollied.setBottom(hitCollied.top()+height);
    }

    public void setAtkType(int typeCode){
        atkType = typeCode;
    }

}

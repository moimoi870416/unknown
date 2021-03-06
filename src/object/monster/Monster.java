package object.monster;

import controller.ConnectController;
import object.Rect;
import object.actor.GameActor;
import util.Delay;
import util.Global;
import object.GameObjForAnimator;

import java.awt.*;

import static util.Global.isServer;
import static util.Global.playerNum;

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
    protected boolean focus;
    protected boolean readyAtk;
    private int typeCode;

    public Monster(int x, int y, int width, int height, int life, int atk, int moveSpeed, boolean isOnceAttack, int typeCode) {
        this(x, y, width, height, x, y, width, height, x, y, width, height, life, atk, moveSpeed, isOnceAttack, typeCode);

    }

    public Monster(int x, int y, int width, int height, int x2, int y2, int width2, int height2, int hitX, int hitY, int hitWidth, int hitHeight, int life, int atk, int moveSpeed, boolean isOnceAttack, int typeCode) {
        super(x, y, width, height, x2, y2, width2, height2, life*(playerNum+1), atk, moveSpeed);
        attackDelay = new Delay(60);
        isChase = false;
        canAttack = true;
        delayForAttack = new Delay(90);
        this.delayForCollision = new Delay(10);
        collision = true;
        this.isOnceAttack = isOnceAttack;
        forRino = false;
        hitCollied = Rect.genWithCenter(hitX, hitY, hitWidth, hitHeight);
        this.hitX = hitCollied.left() - collider().left();
        this.hitY = hitCollied.top() - collider().top();
        nearest = 50000f;
        this.typeCode = typeCode;
        setMonsterState(State.STAND);
        if (isServer) {
            this.connectID = Global.NetEvent.MONSTER_CONNECT_ID++;
            ConnectController.getInstance().newMonsterSend(this);
        }

    }

    public void chase() {

        float x = Math.abs(gameActor.collider().centerX() - painter().centerX());
        float y = Math.abs(gameActor.collider().bottom() - painter().centerY() - 10);
        if (x <= 20 && y <= 20) {
            return;
        }
        float distance = (float) Math.sqrt(x * x + y * y);//????????????,????????????????????????
        float moveOnX = (float) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * this.moveSpeed); //  ????????????
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

        if (state == State.DEATH || state == State.DEAD) {
            isChase = false;
            return;
        }
        transHitArea();
        if (delayForCollision.count()) {
            collision = true;
        }
        if (isOnceAttack) {
            if (delayForAttack.count()) {
                canAttack = true;
            }
        }
        updateComponent();

        if (forRino) {
            return;
        }
        if (isChase) {
            chase();
        }
    }

    public void setMonsterState(State state) {
        setState(state);

        if (isServer) {
            ConnectController.getInstance().monsterStateSend(state, connectID);
        }
    }

    protected abstract void updateForDelay();

    public void whoIsNear(GameActor gameActor) {
        float dx = Math.abs(gameActor.collider().centerX() - painter().centerX());
        float dy = Math.abs(gameActor.collider().bottom() - painter().centerY() - 10);
        float dc = (float) Math.sqrt(dx * dx + dy * dy);//????????????,????????????????????????
        if (dc < nearest) {
            nearest = dc;
            this.gameActor = gameActor;
        }
        if (nearest < Global.WINDOW_WIDTH / 2) {
            isChase = true;
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

//    protected void isSeeingActor() {
//        if (Math.abs(Global.actorX - painter().centerX()) < Global.WINDOW_WIDTH / 2) {
//            setState(State.RUN);
//            isChase = true;
//        }
//    }


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


    public Rect getHitCollied() {
        return hitCollied;
    }

    @Override
    protected void paintDebug(Graphics g) {
        g.setColor(Color.ORANGE);
        g.drawRect(this.hitCollied.left(), this.hitCollied.top(), this.hitCollied.width(), this.hitCollied.height());
    }

    public void transHitArea() {
        int width = hitCollied.width();
        int height = hitCollied.height();
        hitCollied.setLeft(collider().left() + this.hitX);
        hitCollied.setTop(collider().top() + this.hitY);
        hitCollied.setRight(hitCollied.left() + width);
        hitCollied.setBottom(hitCollied.top() + height);
    }

    public void setAtkType(int typeCode) {
        atkType = typeCode;
    }

    public int getConnectID() {
        return connectID;
    }

    public void setIsChase(boolean isChase) {
        this.isChase = isChase;
    }

    public void setForRino(boolean isTrue) {
        this.forRino = isTrue;
    }

    public void setFocus(boolean isTure) {
        this.focus = isTure;
    }

    public void setReadyAtk(boolean isTure) {
        this.readyAtk = isTure;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setConnectID(int connectID) {
        this.connectID = connectID;
    }

    public boolean getIsChase(){
        return isChase;
    }
}
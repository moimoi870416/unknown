package object.monster;

import controller.ConnectController;
import util.Animator;
import util.Delay;
import util.Global;

import java.awt.*;

public class BullBoss extends Monster {
    private boolean attacking;
    private final int atkDistance = 600;
    private boolean focus;
    private int moveOnX;
    private int moveOnY;
    private int moveDistance;
    private int totalDistance;
    private boolean readyAtk;
    private int chaseCount;
    private Delay normalAtkDelay;

    public BullBoss(int x, int y) {
        super(x + 60, y + 40, 280, 230, x, y, 384, 384, x + 150, y + 90, 100, 90, 30000, 51, 2, false, 0);
        animator = new Animator("/pictures/monster/bullboss.png", 30, 96, 96, 20);
        animator.setArr(5, 5);
        attacking = false;
        delayForAttack = new Delay(45);
        animator.setDelayCount(30);
        focus = false;
        readyAtk = true;
        totalDistance = 0;
        attackDelay.play();
        chaseCount = 0;
        normalAtkDelay = new Delay(45);
        normalAtkDelay.loop();
    }

    @Override
    protected void updateForDelay() {

    }

    @Override
    protected void updateComponent() {
        if (attacking) {
            if (atkType == 3) {
                if (!isChase) {
                    if (animator.isFinish()) {
                        setMonsterState(State.STAND);
                    }
                    return;
                }
                forRino = true;
                if (readyAtk) {
                    if (criticalAttack()) {
                        return;
                    }
                    chase();
                    return;
                }
                criticalAtkMove();
                return;

            } else {
                normalAtk();
            }
            return;
        }
        attacking = true;
        if (Global.isServer) {
            atkType = Global.random(0, 3);
            ConnectController.getInstance().bossAtkTypeSend(atkType);
        }

    }


    @Override
    protected void setStateComponent() {
        switch (this.state) {
            case STAND -> {
//                int r = Global.random(0,1);
//                if(r == 0){
//                    animator.setArr(5,5);
//                }else {
                animator.setArr(6, 6);
//                }
                animator.setDelayCount(15);
                animator.setPlayOnce();
            }
            case DEATH -> {
                animator.setArr(6, 10);
                animator.setDelayCount(30);
                animator.setPlayOnce();
                moveSpeed = 0;
            }
            case ATTACK -> {
                animator.setArr(9, 4);
                animator.setDelayCount(5);
                animator.setPlayOnce();
                moveSpeed = 3;
                isOnceAttack = false;
                canAttack = true;
                attackDelay = new Delay(30);

            }
            case CRITICAL -> {
                animator.setArr(9, 7);
                animator.setDelayCount(5);
                animator.setPlayOnce();
                moveSpeed = 4;
                isOnceAttack = true;
                canAttack = true;
                attackDelay = new Delay(60);
            }
            case RUN -> {
                animator.setArr(8, 2);
                animator.setDelayCount(10);
                animator.setPlayLoop();
                moveSpeed = 3;
            }
        }
    }

    private void normalAtk() {
        if (Math.abs(painter().centerX() - gameActor.collider().centerX()) > 50) {
            setMonsterState(State.RUN);
            chase();
            chaseCount++;
            if (chaseCount > 180) {
                attacking = false;
                chaseCount = 0;
            }
            return;
        }
        if (state != State.ATTACK) {
            if (normalAtkDelay.count()) {
                setMonsterState(State.ATTACK);
            }
        }
            chase();
        if ( animator.isFinish()) {
            setMonsterState(State.RUN);
            attacking = false;
        }
    }

    private boolean criticalAttack() {
        if (Math.abs(painter().centerX() - gameActor.collider().centerX()) < 500 || focus) {
            focus = true;
            if (attackDelay.count()) {
                setMonsterState(State.CRITICAL);
                int x = Math.abs(gameActor.collider().centerX() - painter().centerX());
                int y = Math.abs(gameActor.collider().bottom() - painter().centerY());
                float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
                this.moveOnX = (int) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * moveSpeed * 3); //  正負向量
                this.moveOnY = (int) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * moveSpeed * 3);
                if (gameActor.collider().bottom() < painter().centerY()) {
                    this.moveOnY = -moveOnY;
                }
                if (gameActor.collider().centerX() < painter().centerX()) {
                    this.moveOnX = -moveOnX;
                }
                moveDistance = (int) Math.sqrt(moveOnX * moveOnX + moveOnY * moveOnY);
                readyAtk = false;
                changeDir(moveOnX);
            }
            return true;
        }
        return false;
    }

    private void criticalAtkMove() {
        if (totalDistance < atkDistance) {
            translate(moveOnX, moveOnY);
            totalDistance += moveDistance;
            return;
        }
        focus = false;
        readyAtk = true;
        attackDelay.play();
        setMonsterState(State.STAND);
        totalDistance = 0;
        changeDir(gameActor.collider().centerX() - painter().centerX());
        attacking = false;
    }

}

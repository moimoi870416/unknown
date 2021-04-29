package object.monster;


import controller.ConnectController;
import util.Animator;
import util.Delay;

public class Rino extends Monster {

    private final int atkDistance = 500;
    private int moveOnX;
    private int moveOnY;
    private int moveDistance;
    private int totalDistance;
    private int originalAtk;

    public Rino(int x, int y) {
        super(x + 5, y + 6, 94, 58, x, y, 104, 68, x + 5, y + 6, 94, 58, 1500, 45, 2, true, 2);
        animator = new Animator("/pictures/monster/rino/Idle2(52x34).png", 30, 104, 68d, 2);
        animator.setArr(11);
        readyAtk = true;
        attackDelay = new Delay(180);
        attackDelay.play();
        this.totalDistance = 0;
        focus = false;
        originalAtk = atk;
        clickAtk = true;
    }

    @Override
    protected void setStateComponent() {

        switch (state) {
            case STAND -> {
                animator.setImg("/pictures/monster/rino/Idle2(52x34).png", 2);
                animator.setArr(11);
                animator.setDelayCount(0);
                moveSpeed = 0;
            }
            case WALK -> {
                animator.setImg("/pictures/monster/rino/Idle2(52x34).png", 2);
                animator.setArr(11);
                animator.setDelayCount(10);
                atk += atk * 0.5;
                moveSpeed = 2;
            }
            case ATTACK -> {
                animator.setImg("/pictures/monster/rino/Idle2(52x34).png", 2);
                animator.setArr(11);
                animator.setDelayCount(10);
                atk = originalAtk;
            }
            case RUN -> {
                animator.setImg("/pictures/monster/rino/Run(52x34).png", 2);
                animator.setArr(6);
                animator.setDelayCount(10);
                moveSpeed = 2;
            }
            case DEATH -> {
                animator.setImg("/pictures/monster/rino/dead(224-64).png", 2);
                animator.setWidthAndHeightSize(32, 32);
                animator.setArr(7);
                animator.setDelayCount(10);
                animator.setPlayOnce();
                moveSpeed = 0;
            }
        }
    }

    @Override
    protected void updateForDelay() {

    }


    protected void updateComponent() {
        if (isChase) {
            forRino = true;
            if (readyAtk) {
                if (attack()) {
                    return;
                }
                chase();
                return;
            }
            atkMove();
            return;
        }
    }

    private boolean attack() {
        if (Math.abs(painter().centerX() - gameActor.collider().centerX()) < 500 || focus) {
            focus = true;
            ConnectController.getInstance().monsterBooleanSend(focus, connectID, "focus");
            setMonsterState(State.STAND);

            if (attackDelay.count()) {
                setMonsterState(State.RUN);
                int x = Math.abs(gameActor.collider().centerX() - painter().centerX());
                int y = Math.abs(gameActor.collider().bottom() - painter().centerY());
                float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
                this.moveOnX = (int) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * moveSpeed * 5); //  正負向量
                this.moveOnY = (int) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * moveSpeed * 5);
                if (gameActor.collider().bottom() < painter().centerY()) {
                    this.moveOnY = -moveOnY;
                }
                if (gameActor.collider().centerX() < painter().centerX()) {
                    this.moveOnX = -moveOnX;
                }
                moveDistance = (int) Math.sqrt(moveOnX * moveOnX + moveOnY * moveOnY);
                readyAtk = false;
                ConnectController.getInstance().monsterBooleanSend(readyAtk, connectID, "readyAtk");
                changeDir(moveOnX);
            }
            return true;
        }
        return false;
    }

    private void atkMove() {
        if (totalDistance < atkDistance) {
            translate(moveOnX, moveOnY);
            totalDistance += moveDistance;
            return;
        }
        focus = false;
        ConnectController.getInstance().monsterBooleanSend(focus, connectID, "focus");
        readyAtk = true;
        ConnectController.getInstance().monsterBooleanSend(readyAtk, connectID, "readyAtk");
        attackDelay.play();
        totalDistance = 0;
        changeDir(gameActor.collider().centerX() - painter().centerX());
        animator.setDelayCount(30);
    }
}

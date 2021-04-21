package object.monster;

import object.Rect;
import util.Animator;
import util.Delay;
import util.Global;

public class BullBoss extends MonsterAddHitArea {
    private boolean attacking;
    private final int atkDistance = 600;
    private boolean focus;
    private int moveOnX;
    private int moveOnY;
    private int moveDistance;
    private int totalDistance;
    private boolean readyAtk;
    private int atkType;
    private int count;

    public BullBoss(int x, int y) {
        super(x+60,y+40,280,230,x, y, 384, 384,30000,51,2,false);
        animator = new Animator("/monster/bullboss.png",30,96,96,20);
        delayForAttack = new Delay(75);
        attacking = false;
        setState(State.STAND);
        delayForAttack = new Delay(45);
        animator.setDelayCount(30);
        focus = false;
        readyAtk = true;
        totalDistance = 0;

        attackDelay.play();
        count = 0;
    }

    @Override
    protected void updateComponent() {
        if(attacking) {
            if(atkType == 3) {
                if (!isChase) {
                    if (animator.isFinish()) {
                        setState(State.STAND);
                    }
                    return;
                }
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
            }else {
                normalAtk();
            }
            return;
        }
        atkType = Global.random(0,3);
        attacking = true;
    }

    @Override
    public void setState(State state) {
        this.state = state;
        switch (state){
            case STAND -> {
                int r = Global.random(0,1);
                if(r == 0){
                    animator.setArr(5,5);
                }else {
                    animator.setArr(6,6);
                }
                animator.setDelayCount(15);
                animator.setPlayOnce();
            }
            case DEATH -> {
                animator.setArr(6,10);
                animator.setPlayOnce();
                moveSpeed = 0;
            }
            case ATTACK -> {
                animator.setArr(9,4);
                animator.setDelayCount(5);
                animator.setPlayOnce();
                moveSpeed = 3;
                attackDelay = new Delay(30);

            }
            case CRITICAL ->{
                animator.setArr(9,7);
                animator.setDelayCount(5);
                animator.setPlayOnce();
                moveSpeed = 4;
                isOnceAttack = true;
                attackDelay = new Delay(60);
            }
            case RUN -> {
                animator.setArr(8,2);
                animator.setDelayCount(10);
                animator.setPlayLoop();
                moveSpeed = 3;
            }
        }
    }

    private void normalAtk(){
        if(Math.abs(painter().centerX() - Global.actorX) > 100){
            if(state != State.RUN){
                setState(State.RUN);
            }
            chase();
            count++;
            if(count >300){
                attacking = false;
                count = 0;
            }
            return;
        }
        if(state != State.ATTACK){
            setState(State.ATTACK);
            isOnceAttack = false;
        }
        if(state == State.ATTACK){
            chase();
        }
        if(state == State.ATTACK && animator.isFinish()){
            setState(State.RUN);
            attacking = false;
        }
    }

    private boolean attack(){
        if(Math.abs(painter().centerX() - Global.actorX) < 500 || focus) {
            focus = true;
            if (attackDelay.count()) {
                setState(State.CRITICAL);
                int x = Math.abs(Global.actorX - painter().centerX());
                int y = Math.abs(Global.actorY - painter().centerY());
                float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
                this.moveOnX = (int) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * moveSpeed *3); //  正負向量
                this.moveOnY = (int) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * moveSpeed *3);
                if (Global.actorY < painter().centerY()) {
                    this.moveOnY = -moveOnY;
                }
                if (Global.actorX < painter().centerX()) {
                    this.moveOnX = -moveOnX;
                }
                moveDistance = (int)Math.sqrt(moveOnX * moveOnX+ moveOnY * moveOnY);
                readyAtk = false;
                changeDir(moveOnX);
            }
            return true;
        }
        return false;
    }

    private void atkMove(){
        if(totalDistance < atkDistance){
            translate(moveOnX,moveOnY);
            totalDistance += moveDistance;
            return;
        }
        focus = false;
        readyAtk = true;
        attackDelay.play();
        setState(State.STAND);
        totalDistance = 0;
        changeDir(Global.actorX-painter().centerX());
        attacking = false;
    }

    @Override
    protected void setHitCollied(int x,int y) {
        hitCollied = Rect.genWithCenter(x+75,y+30,30,30);
    }

}

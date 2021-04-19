package object.monster;


import util.Animator;
import util.Delay;
import util.Global;

public class Rino extends Monster{
    private boolean readyAtk;
    private final int atkDistance = 500;
    private int moveOnX;
    private int moveOnY;
    private int moveDistance;
    private int totalDistance;
    private boolean focus;
    private int originalAtk;

    public Rino(int x, int y) {
<<<<<<< HEAD
        super(x+5,y+6,94,58,x, y, 104,68, 500, 80, 2);
=======
        super(x+5,y+6,94,58,x, y, 104,68, 1000, 80, 2,true);
>>>>>>> 碰撞
        animator = new Animator("/monster/rino/Idle2(52x34).png",30,52,34,2);
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
    public void setState(State state) {
        this.state = state;
        switch (state) {
            case STAND -> {
                animator.setImg("/monster/rino/Idle2(52x34).png", 2);
                animator.setArr(11);
                animator.setDelayCount(180);
            }
            case WALK -> {
                animator.setImg("/monster/rino/Idle2(52x34).png", 2);
                animator.setArr(11);
                animator.setDelayCount(180);
                atk += atk;
            }
            case ATTACK -> {
                animator.setImg("/monster/rino/Idle2(52x34).png", 2);
                animator.setArr(11);
                animator.setDelayCount(180);
                atk = originalAtk;
            }
            case RUN -> {
                animator.setImg("/monster/rino/Run(52x34).png", 2);
                animator.setArr(6);
                animator.setDelayCount(10);
            }
            case DEATH -> {
                animator.setImg("/monster/rino/dead(224-64).png", 2);
                animator.setWidthAndHeightSize(32,32);
                animator.setArr(7);
                animator.setDelayCount(10);
                animator.setPlayOnce();
                moveSpeed = 0;
            }
        }
    }

    @Override
<<<<<<< HEAD
    public void update(){
        if (state == State.DEATH) {
            isChase = false;
            return;
        }
        if(isOut()){
            return;
        }
=======
    protected void updateComponent() {
>>>>>>> 碰撞
        if(isChase){
            if(readyAtk){
                if(attack()){
                    return;
                }
                chase();
                return;
            }
            atkMove();
            return;
        }
        isSeeingActor();
        //setState(State.RUN);
    }

    private boolean attack(){
        if(Math.abs(painter().centerX() - Global.actorX) < 500 || focus) {
            focus = true;
            setState(State.STAND);
            if (attackDelay.count()) {
                setState(State.RUN);
                int x = Math.abs(Global.actorX - painter().centerX());
                int y = Math.abs(Global.actorY - painter().centerY());
                float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
                this.moveOnX = (int) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * moveSpeed * 5); //  正負向量
                this.moveOnY = (int) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * moveSpeed * 5);
                if (Global.actorY < painter().centerY()) {
                    this.moveOnY = -moveOnY;
                }
                if (Global.actorX < painter().centerX()) {
                    this.moveOnX = -moveOnX;
                }
                moveDistance = (int)Math.sqrt(moveOnX * moveOnX+ moveOnY * moveOnY);
                changeDir(moveOnX);
                readyAtk = false;
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
        totalDistance = 0;
        changeDir(Global.actorX-painter().centerX());
        animator.setDelayCount(30);

    }
}

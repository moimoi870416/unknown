package object.monster;


import util.Animator;
import util.Delay;
import util.Global;

public class Rino extends Monster{
    private boolean readyAtk;
    private final int atkDistance = 900;
    private int moveOnX;
    private int moveOnY;
    private int moveDistance;
    private int totalDistance;
    private boolean focus;

    public Rino(int x, int y) {
        super(x, y, 104,68, 250, 80, 2);
        animator = new Animator("/monster/rino/Idle(52x34).png",30,52,34,2);
        animator.setArr(11);
        readyAtk = true;
        attackDelay = new Delay(180);
        attackDelay.play();
        this.totalDistance = 0;
        focus = false;
    }

    @Override
    public void setState(State state) {
        this.state = state;
        switch (state){
            case STAND -> animator.setImg("/monster/rino/Idle(52x34).png",2);
            case RUN -> {
                animator.setImg("/monster/rino/Run(52x34).png", 2);
                animator.setArr(6);
                animator.setDelayCount(10);}
        }
    }

    @Override
    public void update(){
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
        setState(State.RUN);
    }

    private boolean attack(){
        if(Math.abs(painter().centerX() - Global.actorX) < 500 || focus) {
            focus = true;
            setState(State.STAND);
            if (attackDelay.count()) {
                setState(State.RUN);
                animator.setDelayCount(10);
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

package object.monster;
import util.Animator;
import util.Global;

public class Cockroach extends Monster {
    private boolean attacking;
    private boolean move;
    private int moveOnX;
    private int moveOnY;

    public Cockroach(int x, int y) {
        super(x+30,y+30,100,92,x, y, 150, 150,x+75,y+30,30,30, 5000, 30, 5,false,1);
        animator = new Animator("/pictures/monster/cockroach/run.png",0,150,150,2);
        animator.setArr(40);
        move = false;
        attacking = false;
    }

    @Override
    protected void updateComponent() {
        if(attacking) {
            if (delayForAttack.isPlaying()) {
                setState(State.ATTACK);
                attacking = false;
                move = false;
            }
        }
        if(state == State.RUN){
            attackMove();
        }
        if(delayForAttack.isStop()){
            if(attacking) {
                setState(State.RUN);
            }
            attacking = true;
        }
        if(isChase){
            forRino = true;
        }
    }

    @Override
    public void setState(State state) {
        this.state = state;
        switch (state){
            case RUN -> {
                animator.setImg("/pictures/monster/cockroach/run.png",2);
                animator.setArr(40);
                animator.setDelayCount(0);
                animator.setPlayLoop();
                moveSpeed = 5;
            }
            case ATTACK -> {
                animator.setImg("/pictures/monster/cockroach/eat.png",2);
                animator.setArr(29);
                animator.setDelayCount(6);
                animator.setPlayOnce();
                moveSpeed = 0;
            }
            case DEATH -> animator.setImg("/pictures/monster/cockroach/run.png",2);
        }
    }

    private void attackMove(){
        int x = 0;
        if(!move) {
            int r = Global.random(0, 1);
            if (r == 0) {
                x = Math.abs(Global.actorX - painter().centerX()) + 50;
            } else {
                x = Math.abs(Global.actorX - painter().centerX()) - 50;
            }
        }
        int y = Math.abs(Global.actorY - painter().centerY());
        float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
        int moveOnX = (int) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * moveSpeed); //  正負向量
        int moveOnY = (int) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * moveSpeed);
        if (Global.actorY < painter().centerY()) {
            this.moveOnY = -moveOnY;
        }
        if (Global.actorX < painter().centerX()) {
            this.moveOnX = -moveOnX;
        }
        translate(moveOnX,moveOnY);
        changeDir(moveOnX);
        move = true;

    }

//    private void move(){
//        if(totalDistance < atkDistance){
//            translate(moveOnX,moveOnY);
//            totalDistance += moveDistance;
//            return;
//        }
//        focus = false;
//        readyAtk = true;
//        attackDelay.play();
//        totalDistance = 0;
//        changeDir(Global.actorX-painter().centerX());
//        animator.setDelayCount(30);
//
//    }


}

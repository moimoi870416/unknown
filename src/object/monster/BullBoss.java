package object.monster;

import object.Rect;
import util.Animator;
import util.Delay;
import util.Global;

public class BullBoss extends MonsterAddHitArea {
    private boolean attacking;

    public BullBoss(int x, int y) {
        super(x+150,y+80,100,120,x, y, 384, 384,1000,51,2,false);
        animator = new Animator("/monster/bullboss.png",30,96,96,20);
        delayForAttack = new Delay(60);
        attacking = false;
        setState(State.STAND);
        animator.setDelayCount(30);
    }

    @Override
    protected void updateComponent() {
        if(!isChase){
            if(animator.isFinish()){
                setState(State.STAND);
            }
            return;
        }
        if(attacking == true) {
            if (delayForAttack.isPlaying()) {
                int r = Global.random(0, 2);
                if (r == 2) {
                    setState(State.ATTACK);
                } else {
                    setState(State.CRITICAL);
                }
                attacking = false;
            }
        }
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
                animator.setDelayCount(30);
                animator.setPlayOnce();
            }
            case DEATH -> {
                animator.setArr(6,10);
                animator.setPlayOnce();
                moveSpeed = 0;
            }
            case ATTACK -> {
                animator.setArr(9,4);
                animator.setDelayCount(20);
                animator.setPlayOnce();
                moveSpeed = 1;
            }
            case CRITICAL ->{
                animator.setArr(9,7);
                animator.setDelayCount(20);
                animator.setPlayOnce();
                moveSpeed = 1;
            }
            case RUN -> {
                animator.setArr(8,2);
                animator.setDelayCount(10);
                animator.setPlayLoop();
                moveSpeed = 3;
            }
        }
    }

    @Override
    protected void setHitCollied(int x,int y) {
        hitCollied = Rect.genWithCenter(x+75,y+30,30,30);
    }

}

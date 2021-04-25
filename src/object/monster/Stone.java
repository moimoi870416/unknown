package object.monster;

import util.Animator;

public class Stone extends Monster{
    private boolean dead;

    public Stone(int x, int y) {
        super(x, y, 32, 27,800,10,2,false,3);
        animator = new Animator("/pictures/monster/rock/rockRun(32x28).png",15,32,27,2);
        animator.setArr(14);
        dead = false;

    }

    @Override
    protected void updateForDelay() {

    }

    @Override
    protected void updateComponent() {
        if(animator.isFinish()){
            animator.setImg("/pictures/monster/rock/rockDead(22x18).png",2);
            animator.setArr(1);
            animator.setDelayCount(6000);
            animator.setPlayLoop();
        }
    }

    @Override
    public void setState(State state) {
        if(life <= 0){
            this.state = State.CRITICAL;
            moveSpeed = 0;
            atk = 0;
            life = Integer.MAX_VALUE;
        }

        switch (this.state) {
            case CRITICAL -> {
                if(!dead) {
                    animator.setImg("/pictures/monster/rock/RockDeath(22x18).png", 2);
                    animator.setArr(4);
                    animator.setDelayCount(10);
                    animator.setWidthAndHeightSize(22, 18);
                    animator.setPlayOnce();
                    dead = true;
                }
            }
        }

    }


}

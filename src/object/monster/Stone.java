package object.monster;

import util.Animator;

public class Stone extends Monster{
    private boolean dead;

    public Stone(int x, int y) {
        super(x, y, 32, 27,200,10,2);
        animator = new Animator("/monster/rock/rockRun(32x28).png",15,32,27,2);
        animator.setArr(14);
        deadCount = 80;
        dead = false;

    }

    @Override
    protected void updateComponent() {
        if(animator.isFinish()){
            animator.setImg("/monster/rock/rockDead(22x18).png",2);
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
        }
        switch (this.state) {
            case CRITICAL -> {
                if(!dead) {
                    animator.setImg("/monster/rock/RockDeath(22x18).png", 2);
                    animator.setArr(5);
                    animator.setDelayCount(10);
                    animator.setWidthAndHeightSize(22, 18);
                    animator.setPlayOnce();
                    dead = true;
                }
            }
        }

    }


}

package object.monster;

import object.animator.BullBossAnimator;

public class BullBoss extends Monster {

    public BullBoss(int x, int y) {
        super(x+30,y+30,132,132,x, y, 192, 192,1000,51,5);
        animator = new BullBossAnimator("/monster/bullboss.png",15,96);
    }

    @Override
    protected void setStatePath() {
        statePath.add("/monster/bullboss.png");
        statePath.add("/monster/bullboss.png");
        statePath.add("/monster/bullboss.png");
        statePath.add("/monster/bullboss.png");
        statePath.add("/monster/bullboss.png");
        statePath.add("/monster/bullboss.png");
    }


}

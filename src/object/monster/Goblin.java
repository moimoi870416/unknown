package object.monster;

import object.animator.GoblinAnimator;

public class Goblin extends Monster {

    public Goblin(int x, int y) {
        super(x, y, 32, 32,100,10,2);
        animator = new GoblinAnimator(statePath.get(0),15,16);
    }

    @Override
    protected void setStatePath() {
        statePath.add("/monster/goblin/goblin.png");
    }
}

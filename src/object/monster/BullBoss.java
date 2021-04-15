package object.monster;

import util.Animator;

public class BullBoss extends Monster {

    public BullBoss(int x, int y) {
        super(x+30,y+30,132,132,x, y, 192, 192,1000,51,5);
        animator = new Animator("/monster/bullboss.png",15,92,10);
        animator.setArr(5);
    }


}

package object.monster;

import object.animator.Animator;

public class Goblin extends Monster {

    public Goblin(int x, int y) {
        super(x, y, 32, 32,100,10,2);
        animator = new Animator("/monster/goblin/goblin88.png",15,16,2);
        animator.setArr(6);
    }

}

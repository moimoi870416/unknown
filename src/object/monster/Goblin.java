package object.monster;

import util.Animator;

public class Goblin extends Monster {

    public Goblin(int x, int y) {
        super(x, y, 32, 32,100,10,2);
        animator = new Animator("/monster/goblin/goblin88.png",15,16,2);
        animator.setArr(6);
    }

    public void setState(State state){
        switch (state){
            case DEATH -> animator.setImg("/monster/goblin/goblindead.png",2);
        }
    }
}

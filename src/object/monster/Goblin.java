package object.monster;

import object.animator.Animator;

public class Goblin extends Monster {

    public Goblin(int x, int y) {
        super(x, y, 32, 32,100,10,2);
        animator = new Animator("/monster/goblin/goblin88.png",15,16,16,2);
        animator.setArr(6);
        deadCount = 80;
    }

    @Override
    public void setState(State state){
        this.state = state;
        switch (state){
            case RUN -> animator.setImg("/monster/goblin/goblin88.png",2);
            case DEATH -> animator.setImg("/monster/goblin/goblindead.png",2);
        }

    }
}

package object.monster;

import util.Animator;

public class SmallMonster extends Monster {
    private Type type;

    public SmallMonster(int x, int y,Type type) {
        super(x, y, 32, 32,500,10,2,false);
        animator = new Animator(type.path,15,16,16,2);
        animator.setArr(6);
        this.type = type;
        state = State.RUN;
        setState(state);
    }

    @Override
    protected void updateComponent() {

    }

    @Override
    public void setState(State state){
        this.state = state;
        switch (this.state){
            case RUN -> {
                animator.setImg(type.path, 2);
                switch (type){
                    case MUSHROOM -> {
                        animator.setArr(16);
                        animator.setWidthAndHeightSize(32,32);
                    }
                }
            }
            case DEATH -> {
                animator.setImg("/monster/goblin/goblindead.png",2);
                animator.setArr(4);
                animator.setWidthAndHeightSize(16,16);
                animator.setPlayOnce();
            }
        }

    }

    public enum Type{
        GOBLIN("/monster/goblin/goblin88.png"),
        SLIME("/monster/goblin/goblin88.png"),
        MUSHROOM("/monster/goblin/mushroomRun.png");

        private String path;

        Type(String path){
            this.path = path;
        }
    }
}

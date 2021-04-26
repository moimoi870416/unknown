package object.monster;

import controller.ConnectController;
import util.Animator;

public class SmallMonster extends Monster {
    private Type type;

    public SmallMonster(int x, int y,Type type) {
        super(x, y, 32, 32,500,10,type.speedMove,false,type.typeCode);
        this.type = type;
        if(type == Type.GOBLIN) {
            animator = new Animator(type.path, 15, 16, 16, 2);
            animator.setArr(6);
        }else if(type == Type.MUSHROOM){
            animator = new Animator(type.path, 15, 32, 32, 2);
            animator.setArr(16);
        }

    }

    @Override
    protected void updateForDelay() {

    }

    @Override
    protected void updateComponent() {

    }

    @Override
    protected void setStateComponent() {
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
                animator.setImg("/pictures/monster/goblin/goblindead.png",2);
                animator.setArr(4);
                animator.setWidthAndHeightSize(16,16);
                animator.setPlayOnce();
            }
        }
    }

    public enum Type{
        GOBLIN("/pictures/monster/goblin/goblin88.png",5,4),
        SLIME("/pictures/monster/goblin/goblin88.png",3,5),
        MUSHROOM("/pictures/monster/goblin/mushroomRun.png",3,6);

        private String path;
        private int speedMove;
        private int typeCode;

        Type(String path,int speedMove,int typeCode){
            this.path = path;
            this.speedMove = speedMove;
            this.typeCode = typeCode;
        }
    }
}

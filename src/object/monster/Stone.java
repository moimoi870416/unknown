package object.monster;

import controller.ConnectController;
import util.Animator;
import util.Global;

import javax.crypto.spec.PSource;
import java.awt.*;

public class Stone extends Monster{
    private boolean dead;

    public Stone(int x, int y) {
        super(x, y, 32, 27,800,10,2,false,3);
        animator = new Animator("/pictures/monster/rock/rockRun(32x28).png",15,32,27,2);
        animator.setArr(14);
        dead = false;
        Global.stoneCount++;
    }

    @Override
    protected void updateForDelay() {

    }

    @Override
    protected void updateComponent() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(animator.isFinish()){
            animator.setImg("/pictures/monster/rock/rockDead(22x18).png",2);
            animator.setArr(1);
            animator.setDelayCount(6000);
            animator.setPlayLoop();
        }
    }

    @Override
    protected void setStateComponent() {
        switch (this.state) {
            case OTHER -> {
                if(!dead) {
                    animator.setImg("/pictures/monster/rock/RockDeath(22x18).png", 2);
                    animator.setArr(4);
                    animator.setDelayCount(10);
                    animator.setWidthAndHeightSize(22, 18);
                    animator.setPlayOnce();
                    dead = true;
                    moveSpeed = 0;
                    atk = 0;
                    life = Integer.MAX_VALUE;
                }
            }
        }
    }
}

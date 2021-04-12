package object.monster;

import java.awt.*;

public class BullBoss extends Monster {

    public BullBoss(int x, int y) {
        super("/bullboss.png",15,x+30,y+30,132,132,x, y, 192, 192,1000,2,51);
        animator.setAnimatorSize(96);
        animator.setACTOR_WALK(new int[]{0,1,2,3,4});
        animator.setDelay().loop();
    }

    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g,painter().left(), painter().right(), painter().top(), painter().bottom(),dir);
    }

}

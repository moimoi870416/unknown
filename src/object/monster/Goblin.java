package object.monster;

import java.awt.*;

public class Goblin extends Monster {

    public Goblin(int x, int y) {
        super("/goblinrun.png",15,x, y, 80, 80,2,10);
        animator.setAnimatorSize(16);
        animator.setACTOR_WALK(new int[]{0,1,2,3,4,5});
    }



    @Override
    public void update() {
    }

    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g,painter().left(), painter().right(), painter().top(), painter().bottom(),dir);
    }
}

package object.actor;

import object.GameObjForAnimator;
import java.awt.*;

public class GameActor extends GameObjForAnimator {


    public GameActor( final int x, final int y) {
        super("/actorrun.png",15,x, y, 58, 58,100,10,5);
        animator.setAnimatorSize(58);
        animator.setACTOR_WALK(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13});

    }

    public void move(int commandCode){
        switch (commandCode){
            case 6:
                translateY(-moveSpeed);
                break;
            case -6:
                translateY(moveSpeed);
                break;
            case 7:
                translateX(moveSpeed);
                break;
            case -7:
                translateX(-moveSpeed);
                break;
        }
        changeDir(commandCode);
    }

    @Override
    public void paintComponent(Graphics g) {
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
    }

    @Override
    public void update() {

    }

}

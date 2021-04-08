package actor;

import unit.Global;
import java.awt.*;
import objectdata.Character;

public class GameActor extends Character {
    private final int MOVE_SPEED = 5;
    private int num;

    public GameActor(final int num, final int x, final int y) {
        super(x, y, 58, 58,"/actor.png");
        this.num = num;
    }

    private void changeDir(int commandCode) {
        if (commandCode == 6) {
            translateY(-this.MOVE_SPEED);
        } else if (commandCode == -6) {
            translateY(this.MOVE_SPEED);
        } else if (commandCode == 7) {
            translateX(this.MOVE_SPEED);
        } else if (commandCode == -7) {
            translateX(-this.MOVE_SPEED);
        }
    }

    public void move(int commandCode){
        changeDir(commandCode);
        if (commandCode == 6) {
            translateY(-this.MOVE_SPEED);
        } else if (commandCode == -6) {
            translateY(this.MOVE_SPEED);
        } else if (commandCode == 7) {
            translateX(this.MOVE_SPEED);
        } else if (commandCode == -7) {
            translateX(-this.MOVE_SPEED);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }

    @Override
    public void paintAnimator(Graphics g, int num, int left, int right, int top, int bottom) {

    }
}

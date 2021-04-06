package actor;

import controller.ImageController;
import controller.Global;
import object.GameObject;
import unit.CommandSolver;
import unit.Delay;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameActor extends GameObject implements CommandSolver.KeyListener, CommandSolver.MouseListener {
    private final int MOVE_SPEED = 5;
    ActorAnimator actorAnimator;
    private int num;
    private Dir dir;

    public GameActor(final int num, final int x, final int y) {
        super(x, y, Global.UNIT_X, Global.UNIT_Y);
        this.num = num;
        this.actorAnimator = new ActorAnimator();
        this.dir = Dir.DOWN;
    }

    private void changeDir(final int x, final int y) {
        final int tx = x - getCenterX();
        final int ty = y - getCenterY();
        if (ty > 0 && y > bottom()) {
            this.dir = Dir.DOWN;
        } else if (tx > 0 && x > right()) {
            this.dir = Dir.RIGHT;
        } else if (ty < 0 && y < top()) {
            this.dir = Dir.UP;
        } else if (tx < 0 && x < left()) {
            this.dir = Dir.LEFT;
        }
    }

    private void move(int commandCode){
        if (commandCode == 6) {
            offSetY(-this.MOVE_SPEED);
        } else if (commandCode == -6) {
            offSetY(this.MOVE_SPEED);
        } else if (commandCode == 7) {
            offSetX(this.MOVE_SPEED);
        } else if (commandCode == -7) {
            offSetX(-this.MOVE_SPEED);
        }
    }

    @Override
    public boolean isOut() {
        return false;
    }

    @Override
    public void paint(final Graphics g) {
        this.actorAnimator.paint(g, this.num, left(), right(), top(), bottom(), this.dir);
    }

    @Override
    public void update() {

    }

    @Override
    public void keyPressed(final int commandCode, final long trigTime) {
        move(commandCode);
    }

    @Override
    public void keyReleased(final int commandCode, final long trigTime) {

    }

    @Override
    public void keyTyped(final char c, final long trigTime) {

    }

    @Override
    public void mouseTrig(final MouseEvent e, final CommandSolver.MouseState state, final long trigTime) {
        changeDir(e.getX(), e.getY());
    }

    private enum Dir {
        DOWN,
        LEFT,
        RIGHT,
        UP,
    }

    private static class ActorAnimator {
        private static final int[] ACTOR_WALK = {0, 1, 2, 1};
        private Image img;
        private int count;
        private Delay delay;

        public ActorAnimator() {
            this.img = ImageController.getInstance().tryGet("/Actor1.png");
            this.delay = new Delay(20);
            this.delay.loop();
            this.count = 0;
        }

        public void paint(final Graphics g, final int num, final int left, final int right, final int top, final int bottom, final Dir dir) {
            if (this.delay.count()) {
                this.count = ++this.count % 4;
            }
            final int tx = (num % 4) * Global.UNIT_X * 3;
            final int ty = (num / 4) * Global.UNIT_Y * 4;
            g.drawImage(this.img, left, top, right , bottom
                    , tx + ACTOR_WALK[this.count] * Global.UNIT_X
                    , ty + Global.UNIT_Y * dir.ordinal()
                    , tx + Global.UNIT_X + Global.UNIT_X * ACTOR_WALK[this.count]
                    , Global.UNIT_Y + Global.UNIT_Y * dir.ordinal()
                    , null);
        }
    }
}

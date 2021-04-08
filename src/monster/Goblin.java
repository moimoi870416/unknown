package monster;

import unit.Global;
import unit.Delay;

import java.awt.*;

public class Goblin extends Monster{

    public Goblin(int x, int y) {
        super("/goblinrun.png",x, y, 32, 32,2,10);
        ACTOR_WALK = new int[]{0,1,2,3,4,5};
        delay = new Delay(15);
        delay.loop();
    }

    @Override
    public void paintAnimator(Graphics g, int num, int left, int right, int top, int bottom) {
        if (this.delay.count()) {
            this.count = ++this.count % ACTOR_WALK.length;
        }
        g.drawImage(this.img, left, top, right , bottom
                , Global.UNIT_X/2 *ACTOR_WALK[this.count]
                , Global.UNIT_Y/2 * dir.ordinal()
                , Global.UNIT_X/2 + Global.UNIT_X/2 * ACTOR_WALK[this.count]
                , Global.UNIT_Y/2 + Global.UNIT_Y/2 * dir.ordinal()
                , null);
    }

    @Override
    public void update() {
    }

    @Override
    public void paintComponent(Graphics g) {
        paintAnimator(g,0, painter().left(), painter().top(), painter().right(), painter().bottom());
    }
}

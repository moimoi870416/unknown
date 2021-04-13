package object.monster;

import java.awt.*;
import java.util.ArrayList;

public class BullBoss extends Monster {

    public BullBoss(int x, int y) {
        super(x+30,y+30,132,132,x, y, 192, 192,1000,51,2);
    }

    @Override
    protected void setStatePath() {
        //"/monster/bullboss.png",15,
    }


}

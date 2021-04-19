package util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bar {

    private int x;
    private int y;
    private int width;
    private int height;
    private int life;

    public Bar() {
        this.width = 60;
        this.height = 7;
    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //畫外圈
        Rectangle2D r2 = new Rectangle2D.Double(x, y - height, width, height);
        g2.setColor(Color.BLACK);
        g2.draw(r2);
        //根據血量比例畫血條
        Rectangle2D r = new Rectangle2D.Double(x + 1, y - height, width * ((double) life / 100) - 1, height - 1);
        g2.setColor(Color.RED);
        g2.fill(r);
    }

    public void barUpdate(int x, int y,int life) {
        this.x = x;
        this.y = y;
        this.life = life;
    }

}

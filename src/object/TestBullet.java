package object;

import bullet.Bullet;
import controller.ImageController;
import java.awt.*;

public class TestBullet extends Bullet {
    private Image img;

    public TestBullet(int x, int y, int width, int height, int mouseX, int mouseY) {
        super(x, y, width, height,mouseX,mouseY,10,Math.random()*8-4);
        img = ImageController.getInstance().tryGet("/bullet.png");

    }


    @Override
    public boolean isOut() {
        if(setRange()<0){
            return true;
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,(int)left(),(int)top(),null);
    }

    @Override
    public void update() {
        move();
    }

}

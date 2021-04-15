package util;

import controller.ImageController;
import object.actor.GameActor;

import java.awt.*;

public class Display {

    private Image gun1Frame;
    private Image gun2Frame;
    private Font font;
    private Image currentGun;
    private Image anotherGun;
    private GameActor gameActor;

    public Display(GameActor gameActor) {
        this.gameActor = gameActor;
        gun1Frame = ImageController.getInstance().tryGet("/map/frame1.png");
        gun2Frame = ImageController.getInstance().tryGet("/map/frame2.png");
        currentGun = ImageController.getInstance().tryGet(gameActor.getGun().getGunType().path);
        font = new Font("Curlz TM", Font.PLAIN, 20);
    }


    public void paint(Graphics g) {
        magazinePaint(g);
        framePaint(g);
        gunPaint(g);
    }


    private void framePaint(Graphics g) {
        g.drawImage(gun1Frame, 1200, 750, null);
        g.drawImage(gun2Frame, 1280, 750, null);

    }

    private void magazinePaint(Graphics g) {
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(gameActor.getGun().toString(), 1100, 800);
    }

    private void gunPaint(Graphics g) {
        g.drawImage(currentGun, 1200, 750, null);
    }


}

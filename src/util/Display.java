package util;

import controller.ImageController;
import object.actor.GameActor;
import weapon.Gun;

import java.awt.*;

public class Display {

    private Image gun1Frame;
    private Image gun2Frame;
    private Image skillFrame1;
    private Image skillFrame2;
    private Font font1;
    private Font font2;
    private Image currentGun;
    private Image otherGun;
    private GameActor gameActor;
    public static boolean isFirstGun;


    public Display(GameActor gameActor) {
        this.gameActor = gameActor;
        isFirstGun = true;
        gun1Frame = ImageController.getInstance().tryGet("/display/frame1.png");
        gun2Frame = ImageController.getInstance().tryGet("/display/frame2.png");
        skillFrame1 = ImageController.getInstance().tryGet("/display/heal.png");
        skillFrame2 = ImageController.getInstance().tryGet("/display/frame2.png");
        currentGun = ImageController.getInstance().tryGet(gameActor.getCurrentGun().getGunType().forMapPath);
        otherGun = ImageController.getInstance().tryGet(gameActor.gunOtherGun().getGunType().forMapPath);
        font1 = new Font("Curlz TM", Font.PLAIN, 20);
        font2 = new Font("Curlz TM", Font.PLAIN, 14);
    }


    public void paint(Graphics g) {
        magazinePaint(g);
        skillPaint(g);
        framePaint(g);
        gunPaint(g);

    }

    private void skillPaint(Graphics g) {

        g.drawImage(skillFrame1, 1260, 750, null);
        g.drawImage(skillFrame2, 1340, 750, null);
    }

    private void framePaint(Graphics g) {
        if (isFirstGun) {
            g.drawImage(gun1Frame, 1100, 750, null);
            g.drawImage(gun2Frame, 1180, 750, null);
            return;
        }
        g.drawImage(gun2Frame, 1100, 750, null);
        g.drawImage(gun1Frame, 1180, 750, null);

    }

    private void magazinePaint(Graphics g) {
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString(gameActor.getCurrentGun().toString(), 1020, 790);
    }

    private void gunPaint(Graphics g) {
        g.drawImage(currentGun, 1100, 760, null);
        g.drawImage(otherGun, 1180, 760, null);
        surplusBullet(g);
    }

    private void surplusBullet(Graphics g) {
        g.setFont(font2);
        g.setColor(Color.BLACK);
        if (gameActor.getCurrentGun().getGunType() == Gun.GunType.PISTOL) {
            return;
        }
        if (isFirstGun) {
            g.drawString(String.valueOf(gameActor.getCurrentGun().getSurplusBullet()), 1140, 815);
            return;
        }
        g.drawString(String.valueOf(gameActor.getCurrentGun().getSurplusBullet()), 1220, 815);
    }


}

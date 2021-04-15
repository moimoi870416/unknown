package util;

import controller.ImageController;
import object.actor.GameActor;
import weapon.Gun;

import java.awt.*;

public class Display {

    private Image gun1Frame;
    private Image gun2Frame;
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
        currentGun = ImageController.getInstance().tryGet(gameActor.getCurrentGun().getGunType().path);
        otherGun=ImageController.getInstance().tryGet(gameActor.gunOtherGun().getGunType().path);
        font1 = new Font("Curlz TM", Font.PLAIN, 20);
        font2=new  Font("Curlz TM", Font.PLAIN, 14);
    }


    public void paint(Graphics g) {
        magazinePaint(g);
        framePaint(g);
        gunPaint(g);

    }


    private void framePaint(Graphics g) {
        if(isFirstGun){
            g.drawImage(gun1Frame, 1200, 750, null);
            g.drawImage(gun2Frame, 1280, 750, null);
            return;
        }
        g.drawImage(gun2Frame, 1200, 750, null);
        g.drawImage(gun1Frame, 1280, 750, null);


    }

    private void magazinePaint(Graphics g) {
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString(gameActor.getCurrentGun().toString(), 1110, 790);
    }

    private void gunPaint(Graphics g) {
        g.drawImage(currentGun, 1200, 760, null);
        g.drawImage(otherGun,1280,760,null);
        surplusBullet(g);
    }

    private void surplusBullet(Graphics g){
        g.setFont(font2);
        g.setColor(Color.BLACK);
        if(isFirstGun) {
            if(gameActor.getCurrentGun().getGunType() == Gun.GunType.PISTOL){
                return;
            }
            g.drawString(String.valueOf(gameActor.getCurrentGun().getSurplusBullet()), 1240, 815);
            return;
        }
        g.drawString(String.valueOf(gameActor.getCurrentGun().getSurplusBullet()),1320,815);
    }


}

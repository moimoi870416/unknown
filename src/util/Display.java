package util;

import controller.ImageController;
import object.actor.GameActor;
import weapon.Gun;

import java.awt.*;

public class Display {

    private Image gun1Frame;
    private Image gun2Frame;
    private Image skillHeal;
    private Image skillFlash;
    private Image skillNull;
    private Image num1;
    private Image num2;
    private Image num3;
    private Image num4;
    private Font font1;
    private Font font2;
    private Font font3;
    private Image currentGun;
    private Image otherGun;
    private GameActor gameActor;
    private boolean canHeal;
    private boolean canFlash;
    public static boolean isFirstGun;


    public Display(GameActor gameActor) {
        this.gameActor = gameActor;
        isFirstGun = true;
        gun1Frame = ImageController.getInstance().tryGet("/display/frame1.png");
        gun2Frame = ImageController.getInstance().tryGet("/display/frame2.png");
        skillHeal = ImageController.getInstance().tryGet("/display/heal.png");
        skillFlash = ImageController.getInstance().tryGet("/display/flash.png");
        skillNull = ImageController.getInstance().tryGet("/display/frame1.png");
        num1 = ImageController.getInstance().tryGet("/display/FrameNum1.png");
        num2 = ImageController.getInstance().tryGet("/display/FrameNum2.png");
        num3 = ImageController.getInstance().tryGet("/display/frameNum3.png");
        num4 = ImageController.getInstance().tryGet("/display/frameNum4.png");
        currentGun = ImageController.getInstance().tryGet(gameActor.getCurrentGun().getGunType().forMapPath);
        otherGun = ImageController.getInstance().tryGet(gameActor.gunOtherGun().getGunType().forMapPath);
        font1 = new Font("Curlz TM", Font.PLAIN, 20);
        font2 = new Font("Curlz TM", Font.PLAIN, 14);
        font3 = new Font("Curlz TM", Font.PLAIN, 40);

    }


    public void paint(Graphics g) {
        magazinePaint(g);
        skillPaint(g);
        framePaint(g);
        gunPaint(g);
        buttonPaint(g);

    }

    public void displayUpdate(GameActor gameActor) {
        this.gameActor = gameActor;
    }

    private void buttonPaint(Graphics g) {
        g.drawImage(num1, 1125, 818, null);
        g.drawImage(num2, 1205, 818, null);
        g.drawImage(num3, 1285, 818, null);
        g.drawImage(num4, 1365, 818, null);

    }

    private void skillPaint(Graphics g) {
        if (gameActor.getSkill().getHealCD().isStop()) {
            g.drawImage(skillHeal, 1260, 740, null);
        } else {
            g.drawImage(skillNull, 1260, 740, null);
            g.setFont(font3);
            g.setColor(Color.BLACK);

            if (gameActor.getSkill().getHealCD().getCount()/ 60 < 21) {
                g.drawString(String.valueOf(30 - gameActor.getSkill().getHealCD().getCount() / 60), 1272, 793);
            }else{
                g.drawString(String.valueOf(30 - gameActor.getSkill().getHealCD().getCount() / 60), 1284, 793);
            }
        }

        if (gameActor.getSkill().getDelayForFlash().isStop()) {
            g.drawImage(skillFlash, 1340, 740, null);
        } else {
            g.drawImage(skillNull, 1340, 740, null);
            g.setFont(font3);
            g.setColor(Color.BLACK);

            if (gameActor.getSkill().getDelayForFlash().getCount() / 60 < 10) {
                g.drawString(String.valueOf(9 - gameActor.getSkill().getDelayForFlash().getCount()/ 60), 1364, 793);

            }
        }
    }

    private void framePaint(Graphics g) {
        if (isFirstGun) {
            g.drawImage(gun1Frame, 1100, 740, null);
            g.drawImage(gun2Frame, 1180, 740, null);
            return;
        }
        g.drawImage(gun2Frame, 1100, 740, null);
        g.drawImage(gun1Frame, 1180, 740, null);

    }

    private void magazinePaint(Graphics g) {
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString(gameActor.getCurrentGun().toString(), 1010, 785);
    }

    private void gunPaint(Graphics g) {
        g.drawImage(currentGun, 1100, 750, null);
        g.drawImage(otherGun, 1180, 750, null);
        surplusBullet(g);
    }

    private void surplusBullet(Graphics g) {
        g.setFont(font2);
        g.setColor(Color.BLACK);
        if (gameActor.getCurrentGun().getGunType() == Gun.GunType.PISTOL) {
            return;
        }
        if (isFirstGun) {
            g.drawString(String.valueOf(gameActor.getCurrentGun().getSurplusBullet()), 1140, 810);
            return;
        }
        g.drawString(String.valueOf(gameActor.getCurrentGun().getSurplusBullet()), 1220, 810);
    }


}

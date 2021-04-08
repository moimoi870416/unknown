package weapon;

import unit.Delay;
import java.awt.*;

public class Gun{
    private int atk;
    private int speedMove;
    private float shootDeviation;
    private int firingSpeed;
    private GunType gunType;
    private int magazine;
    private Image img;
    private int count;
    private Delay delay;
    private boolean isReloading;
    private int maxMagazine;
    private int flyingDistance;

    public Gun(GunType type){
        gunType = type;
        setGun();
        count = magazine;
        delay.loop();
        isReloading = false;
        //img = ImageController.getInstance().tryGet("/pistol.png"); //待新增圖片
        //補不同槍的射擊音檔
    }

    public int getCount() {
        return count;
    }

    public int getMagazine() {
        return magazine;
    }

    public boolean shoot(){
        count--;
        if(count <0){
            //補缺彈音檔
            return false;
        }
        return true;
    }

    public boolean getIsReloading(){
        return isReloading;
    }

    public void reloading(){
        isReloading = true;
        if(delay.count()) {
            count = magazine;
            isReloading = false;
        }
    }

    public int getFlyingDistance() {
        return flyingDistance;
    }

    public int getAtk() {
        return atk;
    }

    public int getSpeedMove() {
        return speedMove;
    }

    public float getShootDeviation() {
        return shootDeviation;
    }

    public int getFiringSpeed() {
        return firingSpeed;
    }

    public void paint(Graphics g,int actorX,int actorY) {
        g.drawImage(img,actorX,actorY,null);
    }

    public enum GunType{
        PISTOL,
        UZI,
        AK,
        SNIPER_GUN,
        MACHINE_GUN

    }

    public void changeGun(GunType type){
        gunType = type;
        setGun();
    }


    private void setGun(){
        switch (gunType){
            case PISTOL:
                atk = 11;
                speedMove = 10;
                firingSpeed = 30;
                magazine = 15;
                maxMagazine = 9999999;
                flyingDistance = 250;
                shootDeviation = setShootDeviation(-2,2);
                delay = new Delay(60);
                break;
            case UZI:
                atk = 19;
                speedMove = 15;
                firingSpeed = 7;
                magazine = 40;
                maxMagazine = 90;
                flyingDistance = 400;
                shootDeviation = setShootDeviation(-5,5);
                break;
            case AK:
                atk = 31;
                speedMove = 12;
                firingSpeed = 10;
                magazine = 30;
                maxMagazine = 90;
                flyingDistance = 550;
                delay = new Delay(90);
                shootDeviation = setShootDeviation(-3,3);
                break;
            case SNIPER_GUN:
                atk = 120;
                speedMove = 20;
                firingSpeed = 60;
                magazine = 10;
                maxMagazine = 30;
                flyingDistance = 750;
                shootDeviation = 1;
                break;
            case MACHINE_GUN:
                atk = 41;
                speedMove = 15;
                firingSpeed = 5;
                magazine = 100;
                maxMagazine = 300;
                flyingDistance = 550;
                shootDeviation = setShootDeviation(-7,7);
                break;
        }
    }

    private float setShootDeviation(int max,int min){
        return (float)(Math.random() * (max - min + 1) + min);
    }

}

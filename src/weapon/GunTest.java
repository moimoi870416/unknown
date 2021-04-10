package weapon;

import unit.Delay;
import java.awt.*;

public class GunTest {
    private int atk;//攻擊力
    private int speedMove;//子彈的速度
    private int shootDeviationMax;//槍的誤差最大值
    private int shootDeviationMin;//槍的誤差最小值
    private GunTypeTest gunTypeTest;//槍的種類
    private int magazine;//彈匣 滿夾是子彈數的上限
    private Image img;
    private int count;//彈匣內的子彈數量
    private Delay reloadingDelay;//裝彈延遲
    private Delay shootingDelay;//射擊延遲(射速)
    private boolean isReloading;//是否在裝彈(裝彈中無法射擊)
    private int maxMagazine;//剩餘子彈
    private int flyingDistance;//射程

    public GunTest(GunTypeTest type){
        this.gunTypeTest = type;
        setGun();
        this.count = magazine;
        this.reloadingDelay.loop();
        this.shootingDelay.loop();
        this.isReloading = false;
        //img = ImageController.getInstance().tryGet("/pistol.png"); //待新增圖片
        //補不同槍的射擊音檔
    }

    public int getCount() {
        if(count <0){
            return 0;
        }
        return count;
    }

    public int getMagazine() {
        return magazine;
    }

    public boolean shoot(){
        if(count <=0){
            //補缺彈音檔
            return false;
        }
        count--;
        return true;
    }

    public boolean shootingDelay(){
        if(shootingDelay.isStop()){
            return true;
        }
        return shootingDelay.count();
    }

    public Delay getShootingDelay(){
        return shootingDelay;
    }

    public boolean getIsReloading(){
        return isReloading;
    }

    public void reloading(){
        isReloading = true;
        if(reloadingDelay.count()) {
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
        return setShootDeviation(shootDeviationMax,shootDeviationMin);
    }

    public void paint(Graphics g,int actorX,int actorY) {
        g.drawImage(img,actorX,actorY,null);
    }

    public enum GunTypeTest {
        PISTOL,
        UZI,
        AK,
        SNIPER_GUN,
        MACHINE_GUN

    }

    public void changeGun(GunTypeTest type){
        gunTypeTest = type;
        setGun();
    }


    private void setGun(){
        switch (gunTypeTest){
            case PISTOL:
                atk = 11;
                speedMove = 10;
                magazine = 15;
                maxMagazine = Integer.MAX_VALUE;
                flyingDistance = 250;
                shootingDelay = new Delay(30);
                reloadingDelay = new Delay(60);
                shootDeviationMax = 2;
                shootDeviationMin = -2;
                break;
            case UZI:
                atk = 19;
                speedMove = 15;
                magazine = 40;
                maxMagazine = 90;
                flyingDistance = 400;
                shootingDelay = new Delay(7);
                reloadingDelay = new Delay(45);
                shootDeviationMax = 5;
                shootDeviationMin = -5;
                break;
            case AK:
                atk = 31;
                speedMove = 12;
                magazine = 30;
                maxMagazine = 90;
                flyingDistance = 550;
                shootingDelay = new Delay(10);
                reloadingDelay = new Delay(90);
                shootDeviationMax = 4;
                shootDeviationMin = -4;
                break;
            case SNIPER_GUN:
                atk = 120;
                speedMove = 20;
                magazine = 10;
                maxMagazine = 30;
                flyingDistance = 750;
                shootingDelay = new Delay(60);
                reloadingDelay = new Delay(120);
                shootDeviationMax = 1;
                shootDeviationMin = 1;
                break;
            case MACHINE_GUN:
                atk = 41;
                speedMove = 15;
                magazine = 100;
                magazine = Integer.MAX_VALUE;
                maxMagazine = 300;
                flyingDistance = 550;
                shootingDelay = new Delay(3);
                reloadingDelay = new Delay(180);
                shootDeviationMax = 3;
                shootDeviationMin = -3;
                break;
        }
    }

    private float setShootDeviation(int max,int min){
        return (float)(Math.random() * (max - min + 1) + min);
    }

}

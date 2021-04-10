package weapon;

import object.GameObjForAnimator;
import unit.Delay;

public class Gun extends GameObjForAnimator {
    public Gun(GunType gunType, int x, int y) {
        super(gunType.path, 0, x, y, gunType.width, gunType.height,0,0,0);
    }

    public enum GunType{
        PISTOL("/gun.png",76,32,11,Integer.MAX_VALUE,15,30,60,10,200,1,-1),
        UZI("/gun.png",76,32,19,160,40,7,45,15,400,5,-5),
        AK("/gun.png",76,32,31,120,30,10,90,12,550,2,-2),
        SNIPER("/gun.png",76,32,120,30,10,60,120,30,1000,0,0),
        MACHINE_GUN("/gun.png",76,32,25,300,100,3,180,15,550,3,-3);

        private String path;
        private int width;
        private int height;
        private int atk;//攻擊力
        private int speedMove;//子彈的速度
        private int shootDeviationMax;//槍的誤差最大值
        private int shootDeviationMin;//槍的誤差最小值
        private int magazine;//彈匣
        private int count;//彈匣內的子彈數量
        private int currentCount;
        private Delay reloadingDelay;//裝彈延遲
        private Delay shootingDelay;//射擊延遲(射速)
        private boolean isReloading;//是否在裝彈(裝彈中無法射擊)
        private int maxMagazine;//最大彈匣數量
        private int flyingDistance;//射程

        GunType(String path,int width, int height,int atk,int maxMagazine,int magazine,int reloadingDelay,int shootingDelay,int speedMove,int flyingDistance,int shootDeviationMax ,int shootDeviationMin){
            this.path = path;
            this.width = width;
            this.height = height;
            this.atk = atk;
            this.maxMagazine = maxMagazine;
            this.magazine = magazine;
            this.speedMove = speedMove;
            this.flyingDistance = flyingDistance;
            this.shootDeviationMax = shootDeviationMax;
            this.shootDeviationMin = shootDeviationMin;
            this.isReloading = false;
            this.count = magazine;
            this.currentCount = maxMagazine;
            this.reloadingDelay = new Delay(reloadingDelay);
            this.shootingDelay = new Delay(shootingDelay);
        }
    }
}

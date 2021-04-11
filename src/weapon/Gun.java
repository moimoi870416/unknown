package weapon;

import object.GameObjForAnimator;
import unit.Delay;


public class Gun extends GameObjForAnimator {
    private int magazine;//彈匣
    private int count;//彈匣內的子彈數量
    private int currentCount;
    private Delay reloadingDelay;//裝彈延遲
    private Delay shootingDelay;//射擊延遲(射速)
    private boolean isReloading;//是否在裝彈(裝彈中無法射擊)
    private int maxMagazine;//最大彈匣數量
    private GunType gunType;

    public Gun(GunType gunType, int x, int y) {
        super(gunType.path, 0, x, y, gunType.width, gunType.height,0,0,0);
        this.gunType = gunType;
        this.magazine = gunType.magazine;
        this.count = this.magazine;
        this.maxMagazine = gunType.maxMagazine;
        this.currentCount = this.magazine;
        this.reloadingDelay = new Delay(gunType.reloadingDelay);
        this.shootingDelay = new Delay(gunType.shootingDelay);
        isReloading = false;
        reloadingDelay.loop();
    }

    public enum GunType{
        PISTOL("/gun.png",76,32,Integer.MAX_VALUE,15,30,60),
        UZI("/gun.png",76,32,160,40,7,45),
        AK("/gun.png",76,32,120,30,10,90),
        SNIPER("/gun.png",76,32,30,10,60,120),
        MACHINE_GUN("/gun.png",76,32,300,100,3,180);

        private String path;
        private int width;
        private int height;
        private int magazine;//彈匣
        private int reloadingDelay;//裝彈延遲
        private int shootingDelay;//射擊延遲(射速)
        private int maxMagazine;//最大彈匣數量

        GunType(String path,int width, int height,int maxMagazine,int magazine,int shootingDelay,int reloadingDelay){
            this.path = path;
            this.width = width;
            this.height = height;
            this.maxMagazine = maxMagazine;
            this.magazine = magazine;
            this.reloadingDelay = reloadingDelay;
            this.shootingDelay = shootingDelay;
        }
    }

    public GunType getGunType(){
        return gunType;
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


}

package weapon;

import object.GameObjForAnimator;
import util.Delay;

public class Gun extends GameObjForAnimator {
    private int magazine;//彈匣內的剩餘子彈數量
    private final int magazineMax;//一個彈匣的子彈數量
    private int surplusBullet;//剩餘的總子彈數量
    private final int magazineMaxQuantity;//最大總彈量
    private Delay reloadingDelay;//裝彈延遲
    private Delay shootingDelay;//射擊延遲(射速)
    private boolean canReloading;//是否在裝彈(裝彈中無法射擊)
    private boolean canShoot;
    
    private GunType gunType;

    public Gun(GunType gunType, int x, int y) {
        super(x, y, gunType.width, gunType.height,0,0,0);
        this.gunType = gunType;
        this.magazine = gunType.magazine;
        this.magazineMax = this.magazine;
        this.magazineMaxQuantity = gunType.maxMagazine;
        this.surplusBullet = this.magazineMaxQuantity;
        this.reloadingDelay = new Delay(gunType.reloadingDelay);
        this.shootingDelay = new Delay(gunType.shootingDelay);
        canReloading = false;
        canShoot = true;
    }

    public enum GunType{
        PISTOL("/weapon/gun.png",76,32,Integer.MAX_VALUE,15,30,60),
        UZI("/weapon/gun.png",76,32,160,40,7,45),
        AK("/weapon/gun.png",76,32,120,30,10,90),
        SNIPER("/weapon/gun.png",76,32,30,10,60,120),
        MACHINE_GUN("/weapon/gun.png",76,32,300,100,5,180);

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
    
    public void shoot(){
        if(canShoot){
            shootingDelay.play();
            if(magazineMax <=0){
                //補缺彈音檔
                return;
            }
            magazine--;
            canShoot = false;
            System.out.println(magazine+"/"+magazineMax);
        }
    }

    public void getBullet(int key){
        surplusBullet += key;
        if(surplusBullet > magazineMaxQuantity){
            surplusBullet = magazineMaxQuantity;
        }
    }

    public void reloading(){
        if(canReloading) {
            reloadingDelay.play();
            if(surplusBullet < magazineMax){
                magazine = surplusBullet;
                surplusBullet = 0;
            }else {
                magazine = magazineMax;
                surplusBullet -= magazine;
            }
            canReloading = false;
            canShoot = false;
        }
    }

    public boolean isCanShoot(){
        return canShoot;
    }

    @Override
    public void update() {
        if(reloadingDelay.count()){
            canReloading = true;
            canShoot = true;
        }
        if(shootingDelay.count()){
            canShoot = true;
        }
    }

    @Override
    protected void setStatePath() {
    }

}

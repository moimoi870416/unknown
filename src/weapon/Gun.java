package weapon;

import controller.ImageController;
import object.GameObjForAnimator;
import util.Delay;
import java.awt.*;

public class Gun extends GameObjForAnimator {
    private int magazine;//彈匣內的剩餘子彈數量
    private final int magazineMax;//一個彈匣的子彈數量
    private int surplusBullet;//剩餘的總子彈數量
    private final int magazineMaxQuantity;//最大總彈量
    private Delay reloadingDelay;//裝彈延遲
    private Delay shootingDelay;//射擊延遲(射速)
    private Delay beginShoot;
    private boolean canReloading;//是否在裝彈(裝彈中無法射擊)
    private boolean canShoot;
    private GunType gunType;
    private Image imgForDisplay;
    private Image imgForActor;
    private int positionX;
    private int positionY;
    private int widthForActor;
    private int heightForActor;



    public Gun(GunType gunType, int x, int y) {
        super(x, y, gunType.width, gunType.height, 0, 0, 0);
        this.imgForDisplay = ImageController.getInstance().tryGet(gunType.forMapPath);
        this.imgForActor = ImageController.getInstance().tryGet(gunType.forActorPath);
        this.positionX = x;
        this.positionX = y;
        this.gunType = gunType;
        this.magazine = gunType.magazine;
        this.magazineMax = this.magazine;
        this.magazineMaxQuantity = gunType.maxMagazine;
        this.surplusBullet = this.magazineMaxQuantity;
        this.reloadingDelay = new Delay(gunType.reloadingDelay);
        this.shootingDelay = new Delay(gunType.shootingDelay);
        this.beginShoot = new Delay(gunType.beginShoot);
        canReloading = true;
        canShoot = true;
        shootingDelay.loop();
        this.widthForActor = gunType.widthForActor;
        this.heightForActor = gunType.heightForActor;

    }

    public enum GunType {
        PISTOL("/weapon/pistol.png", "/actor/pistol3.png",76, 32,28,42, Integer.MAX_VALUE, 15, 15, 60, 1),
        UZI("/weapon/uzi.png", "/actor/uzi3.png",70, 54,29,44, 160, 40, 7, 60, 5),
        AK("/weapon/ak.png", "/actor/ak3.png",70, 54,53,56, 120, 30, 10, 90, 10),
        SNIPER("/weapon/sniper.png", "/actor/sniper3.png",70, 54,66,48, 30, 10, 60, 120, 1),
        MACHINE_GUN("/weapon/machine.png", "/actor/machine3.png",70, 54,62,62, 150, 100, 5, 180, 1);

        public String forMapPath;
        public String forActorPath;
        private int width;
        private int height;
        private int magazine;//彈匣
        private int reloadingDelay;//裝彈延遲
        private int shootingDelay;//射擊延遲(射速)
        private int maxMagazine;//最大彈匣數量
        private int beginShoot;
        private int widthForActor;
        private int heightForActor;

        GunType(String forMapPath,String forActorPath, int width, int height,int widthForActor,int heightForActor, int maxMagazine, int magazine, int shootingDelay, int reloadingDelay, int beginShoot) {
            this.forMapPath = forMapPath;
            this.forActorPath = forActorPath;
            this.width = width;
            this.height = height;
            this.maxMagazine = maxMagazine;
            this.magazine = magazine;
            this.reloadingDelay = reloadingDelay;
            this.shootingDelay = shootingDelay;
            this.beginShoot = beginShoot;
            this.widthForActor = widthForActor;
            this.heightForActor = heightForActor;
        }
    }



    public void beginShoot() {
        beginShoot.play();
    }

    public void resetBeginShoot() {
        beginShoot.stop();
    }

    public GunType getGunType() {
        return gunType;
    }

    public boolean shoot() {
        if (canShoot) {
            if(shootingDelay.count()) {
                if (magazine <= 0) {
                    //補缺彈音檔
                    return false;
                }
                magazine--;
                return true;
            }
        }
        return false;
    }

    public void getBullet(int key) {
        surplusBullet += key;
        if (surplusBullet > magazineMaxQuantity) {
            surplusBullet = magazineMaxQuantity;
        }
    }

    public void reloading() {//要修正裝彈延遲
        if(surplusBullet == 0){//沒子彈可以reload則return
            return;
        }
        if (canReloading) {
            reloadingDelay.play();
            if (surplusBullet < magazineMax) {
                if(surplusBullet + magazine > magazineMax){
                    surplusBullet -= (magazineMax - magazine);
                    magazine = magazineMax;
                }else {
                    magazine += surplusBullet;
                    surplusBullet =0;
                }

            } else {
                surplusBullet -= (magazineMax-magazine);
                magazine = magazineMax;

            }
            canReloading = false;
            canShoot = false;
        }
    }

    @Override
    public void setState(State state) {

    }

    @Override
    public void update() {
        if (reloadingDelay.count()) {
            canReloading = true;
            canShoot = true;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(imgForDisplay,positionX,positionY,null);
    }

    public void paintComponent(Graphics g,int actorX,int actorY,Dir dir){
        if(dir == Dir.LEFT) {
            g.drawImage(imgForActor, actorX+3, actorY + 20,actorX+3+widthForActor, actorY + 20+heightForActor/2,0,0,widthForActor,heightForActor/2, null);
            return;
        }
        System.out.println(111);
        g.drawImage(imgForActor, actorX-widthForActor+3, actorY + 20,actorX+3, actorY + 20+heightForActor/2,0,heightForActor/2,widthForActor,heightForActor, null);
    }

    public int getSurplusBullet() {
        return surplusBullet;
    }

    @Override
    public String toString() {
        return (magazine)+"|"+(magazineMax);
    }

}

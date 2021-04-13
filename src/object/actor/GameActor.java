package object.actor;

import object.animator.GoblinAnimator;
import util.Delay;
import util.Global;
import object.GameObjForAnimator;
import weapon.Gun;

public class GameActor extends GameObjForAnimator {
    private WhichGun whichGun;
    private Global.Direction dirMove;
    private final int FLASH_MAX_DISTANCE = 300;
    private Delay delayForFlash;
    private boolean canFlash;

    public GameActor( String path,final int x, final int y) {
        super(x, y, 58, 58,100,10,3);
        animator = new GoblinAnimator(statePath.get(0),15,58);
        whichGun = WhichGun.ONE;
        whichGun.gun.translate(painter().centerX(),painter().centerY());
        dirMove = Global.Direction.NO;
        delayForFlash = new Delay(1);
        canFlash = true;
    }

    public void changeGun(int commandCode){
        if(commandCode == -1){
            whichGun = WhichGun.ONE;
        }else if(commandCode == -2){
            whichGun = WhichGun.TWO;
        }
    }

    private enum WhichGun{
        ONE(new Gun(Gun.GunType.MACHINE_GUN, 0, 0)),
        TWO(new Gun(Gun.GunType.SNIPER,0,0));

        private Gun gun;

        WhichGun(Gun gun){
            this.gun = gun;
        }
    }

    @Override
    public void changeDir(int mouseX){
        if(mouseX>painter().centerX()){
            dir = Dir.LEFT;
        }else {
            dir = Dir.RIGHT;
        }
    }

    public Gun getGun(){
        return whichGun.gun;
    }

    public void move(int commandCode){
        switch (commandCode){
            case 2:
                translateY(-moveSpeed);
                dirMove = Global.Direction.UP;
                break;
            case 3:
                translateY(moveSpeed);
                dirMove = Global.Direction.DOWN;
                break;
            case 1:
                translateX(moveSpeed);
                dirMove = Global.Direction.RIGHT;
                break;
            case 0:
                translateX(-moveSpeed);
                dirMove = Global.Direction.LEFT;
                break;
            default:
                dirMove = Global.Direction.NO;
                break;
        }
    }
/*
    @Override
    protected void setAnimator(String path, int countLimit) {
        animator = new Animator(path,countLimit) {
            @Override
            public void paintAnimator(Graphics g, int left, int right, int top, int bottom) {
                if (delay.count()) {
                    this.count = ++this.count % ACTOR_WALK.length;
                }

                g.drawImage(this.img, left, top, right , bottom
                        , animatorSize *ACTOR_WALK[this.count]
                        , animatorSize * dir.ordinal()
                        , animatorSize + animatorSize * ACTOR_WALK[this.count]
                        , animatorSize + animatorSize * dir.ordinal()
                        , null);
            }
        };
    }

 */

    @Override
    public void update() {
        switch (dirMove) {
            case RIGHT:
                if (painter().right() > Global.MAP_WIDTH) {
                    translateX(-moveSpeed);
                }
                break;
            case LEFT :
                if (painter().left() < 0) {
                    translateX(moveSpeed);
                }
                break;
            case UP :
                if (painter().top() < 0) {
                    translateY(moveSpeed);
                }
                break;
            case DOWN :
                if (painter().bottom() > Global.MAP_HEIGHT-15) {
                    translateY(-moveSpeed);
                }
                break;
            case NO:

        }
        if(delayForFlash.count()){
            canFlash = true;
        }
        whichGun.gun.update();
        updatePosition();
    }

    private void updatePosition(){
        Global.actorX = collider().centerX();
        Global.actorY = collider().bottom();
    }

    @Override
    protected void setStatePath() {
        statePath.add("/actor/actorrun.png");
    }

    public void flash(int mouseX,int mouseY){
        if(canFlash) {
            delayForFlash.play();
            int x = Math.abs(mouseX - painter().centerX());
            int y = Math.abs(mouseY - painter().centerY());
            if (x == 0 && y == 0) {
                return;
            }
            float d = (float) Math.sqrt(x * x + y * y);
            float distance = d;//計算斜邊,怪物與人物的距離
            if (distance > FLASH_MAX_DISTANCE) {
                distance = FLASH_MAX_DISTANCE;
            }

            float moveOnX = (float) Math.cos(Math.toRadians((Math.acos(x / d) / Math.PI * 180))) * distance;
            float moveOnY = (float) Math.sin(Math.toRadians((Math.asin(y / d) / Math.PI * 180))) * distance;
            if (mouseY < painter().centerY()) {
                moveOnY = -moveOnY;
            }
            if (mouseX < painter().centerX()) {
                moveOnX = -moveOnX;
            }
            translate((int) moveOnX, (int) moveOnY);
            canFlash = false;
        }
    }
}

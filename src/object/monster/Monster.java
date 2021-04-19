package object.monster;

import util.Delay;
import util.Global;
import object.GameObjForAnimator;

public abstract class Monster extends GameObjForAnimator {
    private Delay delayForCollision;
    protected Delay attackDelay;
    private boolean collision;
    protected boolean isChase;
    protected boolean confirmAtk;

    public Monster(int x, int y, int width, int height,int life,int atk,int moveSpeed) {
        this(x, y, width, height,x,y,width,height,life,atk,moveSpeed);
        attackDelay = new Delay(60);
        isChase = false;
    }

    public Monster(int x, int y, int width, int height,int x2, int y2, int width2, int height2,int life,int atk,int moveSpeed) {
        super(x, y, width, height, x2, y2, width2, height2, life, atk, moveSpeed);
        this.delayForCollision = new Delay(10);
        collision = true;
        confirmAtk = false;
    }

    public void chase() {
        float x = Math.abs(Global.actorX - painter().centerX());
        float y = Math.abs(Global.actorY - painter().centerY());
        if (x <= 20 && y <=20) {
            return;
        }
        float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
        float moveOnX = (float) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * this.moveSpeed); //  正負向量
        float moveOnY = (float) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * this.moveSpeed);
        if (Global.actorY < painter().centerY()) {
            moveOnY = -moveOnY;
        }
        if (Global.actorX < painter().centerX()) {
            moveOnX = -moveOnX;
        }
        translate((int) moveOnX, (int) moveOnY);
        changeDir(moveOnX);
    }

    @Override
    public void update() {
        if(isOut()){
            return;
        }
        if(state == State.DEATH){
            isChase = false;
            return;
        }
        if (delayForCollision.count()) {
            collision = true;
        }
        if(isChase) {
            chase();
            return;
        }
        isSeeingActor();
        updateComponent();
    }

    protected abstract void updateComponent();

    protected void isSeeingActor(){
        if(Math.abs(Global.actorX - painter().centerX()) < Global.WINDOW_WIDTH/2) {
            setState(State.RUN);
            isChase = true;
        }
    }


    public void isCollisionWithMonster(Monster other) {
        int r;
        if (collision) {
            delayForCollision.play();
            r = Global.random(0, 3);
            if (this.isCollision(other)) {
                switch (r) {
                    case 0:
                        translateX(moveSpeed*2);
                        other.translateX(-moveSpeed*2);
                        break;
                    case 1:
                        translateX(-moveSpeed*2);
                        other.translateX(moveSpeed*2);
                        break;
                    case 2:
                        translateY(moveSpeed*2);
                        other.translateY(-moveSpeed*2);
                        break;
                    case 3:
                        translateY(-moveSpeed*2);
                        other.translateY(-moveSpeed*2);
                        break;
                }
            }
            collision = false;
        }

    }

    public abstract void setState(State state);
    //public abstract String getType();
}

package object.monster;

import util.Delay;
import util.Global;
import object.GameObjForAnimator;

public abstract class Monster extends GameObjForAnimator {
    private Delay delayForCollision;
    private boolean collision;

    public Monster(String path,int countLimit,int x, int y, int width, int height,int life,int atk,int moveSpeed) {
        this(path,countLimit,x, y, width, height,x,y,width,height,life,atk,moveSpeed);
    }

    public Monster(String path,int countLimit,int x, int y, int width, int height,int x2, int y2, int width2, int height2,int life,int atk,int moveSpeed) {
        super(path, countLimit, x, y, width, height, x2, y2, width2, height2, life, atk, moveSpeed);
        this.delayForCollision = new Delay(10);
        collision = true;
    }

    public void chase(int actorX, int actorY) {
        float x = Math.abs(actorX - painter().centerX());
        float y = Math.abs(actorY - painter().centerY());
        if (x == 0 && y == 0) {
            return;
        }
        float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
        float moveOnX = (float) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * this.moveSpeed); //  正負向量
        float moveOnY = (float) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * this.moveSpeed);
        if (actorY < painter().centerY()) {
            moveOnY = -moveOnY;
        }
        if (actorX < painter().centerX()) {
            moveOnX = -moveOnX;
        }
        translate((int) moveOnX, (int) moveOnY);
        changeDir(moveOnX);
    }

    @Override
    public void update() {
        if(state == State.ALIVE) {
            if (delayForCollision.count()) {
                collision = true;
            }
        }
    }

//    private int collisionSide(Monster other) {
//        if (this.collider().left() - other.collider().right() < 8) {
//            return 0;
//        } else if (this.collider().right() - other.collider().left() < 8) {
//            return 1;
//        } else if (this.collider().top() - other.collider().bottom() < 8) {
//            return 2;
//        } else {
//            return 3;
//        }
//    }


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
}

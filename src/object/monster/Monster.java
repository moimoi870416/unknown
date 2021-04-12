package object.monster;

import unit.Global;
import object.GameObjForAnimator;

public abstract class Monster extends GameObjForAnimator {

    public Monster(String path,int countLimit,int x, int y, int width, int height,int life,int moveSpeed,int atk) {
        super(path,countLimit,x, y, width, height,x,y,width,height,life,atk,moveSpeed);
    }

    public Monster(String path,int countLimit,int x, int y, int width, int height,int x2, int y2, int width2, int height2,int life,int moveSpeed,int atk) {
        super(path,countLimit,x, y, width, height,x2,y2,width2,height2,life,atk,moveSpeed);
    }

    public void chase(int actorX,int actorY){
        float x = Math.abs(actorX - painter().centerX());
        float y = Math.abs(actorY - painter().centerY());
        if(x == 0 && y == 0){
            return;
        }
        float distance = (float)Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
        float moveOnX = (float)(Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * this.moveSpeed);
        float moveOnY = (float)(Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * this.moveSpeed);
        if (actorY < painter().centerY()) {
            moveOnY = -moveOnY;
        }
        if (actorX < painter().centerX()) {
            moveOnX = -moveOnX;
        }
        translate((int)moveOnX, (int)moveOnY);
        changeDir(moveOnX);

    }

    @Override
    public void update() {
        if(life <= 0){
            dir = Dir.DEAD;
        }

    }

    public void isCollisionWithMonster(Monster monster){
        if(collider().overlap(monster.collider())){
            int r = Global.random(0,3);
            switch (r){
                case 0:
                    translateX(moveSpeed);
                    break;
                case 1:
                    translateX(-moveSpeed);
                    break;
                case 2:
                    translateY(moveSpeed);
                    break;
                case 3:
                    translateY(-moveSpeed);
                    break;
            }
        }
    }
}

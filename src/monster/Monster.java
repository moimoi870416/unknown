package monster;

import objectdata.Character;

public abstract class Monster extends Character {
    private int MOVE_SPEED;
    private int atk;
    protected Dir dir;

    public Monster(String path,int x, int y, int width, int height,int moveSpeed,int atk) {
        super(x, y, width, height,path);
        this.atk = atk;
        this.MOVE_SPEED = moveSpeed;
        dir = Dir.LEFT;
    }

    public void chase(int actorX,int actorY){
        double x = Math.abs(actorX - painter().centerX());
        double y = Math.abs(actorY - painter().centerY());
        if(x == 0 && y == 0){
            return;
        }
        double distance = Math.sqrt(x * x + y * y);//計算斜邊
        double moveOnX = Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * MOVE_SPEED;
        double moveOnY = Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * MOVE_SPEED;
        if (actorY < painter().centerY()) {
            moveOnY = -moveOnY;
        }
        if (actorX < painter().centerX()) {
            moveOnX = -moveOnX;
        }
        move(moveOnX, moveOnY);
        changeDir(moveOnX);

    }

    private void move(double x,double y){
        //translate(x,y);
    }

    private void changeDir(double moveOnX){
        if(moveOnX>0){
            dir = Dir.LEFT;
        }else {
            dir = Dir.RIGHT;
        }
    }

    protected enum Dir{
        LEFT,
        RIGHT,
    }

}

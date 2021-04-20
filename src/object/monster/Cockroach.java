package object.monster;
import object.Rect;
import util.Animator;

public class Cockroach extends MonsterAddHitArea {
    private int moveDistance;

    public Cockroach(int x, int y) {
        super(x+30,y+30,100,92,x, y, 150, 150, 5000, 30, 5,false);
        animator = new Animator("/monster/cockroach/run.png",0,150,150,2);
        hitCollied = Rect.genWithCenter(x+75,y+30,30,30);
        animator.setArr(40);
        moveDistance = 800;
    }

    @Override
    protected void updateComponent() {

    }

    @Override
    public void setState(State state) {
        this.state = state;
        switch (state){
            case RUN -> {
                animator.setImg("/monster/cockroach/run.png",2);
                animator.setArr(40);
                animator.setDelayCount(0);
                animator.setPlayLoop();
            }
            case ATTACK -> {
                animator.setImg("/monster/cockroach/eat.png",2);
                animator.setArr(29);
                animator.setDelayCount(6);
                animator.setPlayOnce();
            }
            case DEATH -> animator.setImg("/monster/cockroach/run.png",2);
        }
    }

    @Override
    protected void setHitCollied(int x,int y) {
        hitCollied = Rect.genWithCenter(x+75,y+30,30,30);
    }
/*
    @Override
    public void update(){
        if(dir == Dir.RIGHT){
            collider().setLeft(collider().left()-35);
        }else {
            collider().setLeft(collider().left()+35);
        }
        if(isChase) {
            chase();
            return;
        }
        isSeeingActor();
    }
/*
    private boolean attack(){
        if(isChase) {
            focus = true;
            setState(State.STAND);
            if (attackDelay.count()) {
                setState(State.RUN);
                animator.setDelayCount(10);
                int x = Math.abs(Global.actorX - painter().centerX());
                int y = Math.abs(Global.actorY - painter().centerY());
                float distance = (float) Math.sqrt(x * x + y * y);//計算斜邊,怪物與人物的距離
                this.moveOnX = (int) (Math.cos(Math.toRadians((Math.acos(x / distance) / Math.PI * 180))) * moveSpeed * 5); //  正負向量
                this.moveOnY = (int) (Math.sin(Math.toRadians((Math.asin(y / distance) / Math.PI * 180))) * moveSpeed * 5);
                if (Global.actorY < painter().centerY()) {
                    this.moveOnY = -moveOnY;
                }
                if (Global.actorX < painter().centerX()) {
                    this.moveOnX = -moveOnX;
                }
                moveDistance = (int)Math.sqrt(moveOnX * moveOnX+ moveOnY * moveOnY);
                changeDir(moveOnX);
                readyAtk = false;
            }
            return true;

        }
        return false;
    }

    private void atkMove(){
        if(totalDistance < atkDistance){
            translate(moveOnX,moveOnY);
            totalDistance += moveDistance;
            return;
        }
        focus = false;
        readyAtk = true;
        attackDelay.play();
        totalDistance = 0;
        changeDir(Global.actorX-painter().centerX());
        animator.setDelayCount(30);

    }

 */
}

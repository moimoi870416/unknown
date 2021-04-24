package object.actor;

import controller.ConnectController;
import object.GameObject;
import util.*;
import object.GameObjForAnimator;
import weapon.Gun;

import java.awt.*;
import java.util.ArrayList;

import static util.Global.*;

public class GameActor extends GameObjForAnimator {
    private WhichGun currentGun;
    private WhichGun otherGun;
    private Global.Direction verticalDir;
    private Global.Direction horizontalDir;
    private Rotation rotation;
    private Bar blood;
    private Skill skill;
    private int connectID;
    private Actor actor;
    private boolean isFirstGun;
    private int mouseX;
    private int mouseY;

    public GameActor(Actor actor, final int x, final int y) {
        super(x, y, actor.getWidth(), actor.getHeight(), 100, 10, 3);
        this.actor = actor;
        setAnimator();
        setState(State.STAND);
        currentGun = WhichGun.ONE;
        otherGun = WhichGun.TWO;
        currentGun.gun.translate(painter().centerX(), painter().centerY());
        verticalDir = horizontalDir = Direction.NO;
        otherGun.gun.translate(painter().centerX(), painter().centerY());
        rotation = new Rotation();
        blood = new Bar(actor);
        this.moveSpeed = currentGun.gun.getGunType().getMoveSpeed();
        skill = new Skill();
        isFirstGun = false;
    }

    private void setAnimator() {
        switch (this.actor) {
            case FIRST -> animator = new Animator(actor.getPath(), 30, 58, 58, 2);
            case SECOND -> animator = new Animator(actor.getPath(), 30, 64, 68, 2);
            case THIRD -> animator = new Animator(actor.getPath(), 30, 80, 72, 2);
        }

    }


    public void setIsFirstGun(boolean isFirst){
        if(isFirst){
            changeGun(-1);
            return;
        }
        changeGun(-2);
    }

    @Override
    public void paintComponent(Graphics g) {
        if (state == State.DEAD) {
            return;
        }
        animator.paintAnimator(g, painter().left(), painter().right(), painter().top(), painter().bottom(), dir);
        rotation.paint(g, currentGun.gun, this);
        skill.skillPaint(g);
        blood.paint(g);
    }

    @Override
    protected void paintDebug(Graphics g) {

    }

    public boolean getIsFirstGun() {
        return isFirstGun;
    }

    public void changeGun(int commandCode) {
        if (commandCode == -1) {
            currentGun = WhichGun.ONE;
            otherGun = WhichGun.TWO;
            isFirstGun = true;
        } else if (commandCode == -2) {
            currentGun = WhichGun.TWO;
            otherGun = WhichGun.ONE;
            isFirstGun = false;
        }
        this.moveSpeed = currentGun.gun.getGunType().getMoveSpeed();
        ConnectController.getInstance().changeGunSend(this,commandCode);
    }

    private enum WhichGun {
        ONE(new Gun(Gun.GunType.UZI, Global.actorX, Global.actorY)),
        TWO(new Gun(Gun.GunType.SNIPER, Global.actorX, Global.actorY));
        private Gun gun;

        WhichGun(Gun gun) {
            this.gun = gun;
        }
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void changeDir(int mouseX) {
        if (mouseX > painter().centerX()) {
            dir = Dir.RIGHT;
        } else {
            dir = Dir.LEFT;
        }
    }

    public void setConnectID(int key) {
        this.connectID = key;
    }

    public int getConnectID() {
        return connectID;
    }

    public Bar getBlood() {
        return blood;
    }

    public void tradeGun(Gun gun) {
        currentGun.gun = gun;
    }

    public Gun gunOtherGun() {
        return otherGun.gun;
    }

    public Gun getCurrentGun() {
        return currentGun.gun;
    }

    public Skill getSkill() {
        return skill;
    }

    public void move(int commandCode) {
        if (state == State.DEATH || state == State.DEAD) {
            return;
        }
        if (state != State.RUN) {
            setState(State.RUN);
        }
        switch (commandCode) {
            case 2:
                translateY(-moveSpeed);
                verticalDir = Global.Direction.UP;
                break;
            case 3:
                translateY(moveSpeed);
                verticalDir = Global.Direction.DOWN;
                break;
            case 1:
                translateX(moveSpeed);
                horizontalDir = Global.Direction.RIGHT;
                break;
            case 0:
                translateX(-moveSpeed);
                horizontalDir = Global.Direction.LEFT;
                break;
            default:
                verticalDir = horizontalDir = Global.Direction.NO;
                break;
        }
    }

    @Override
    public void setState(State state) {
        if (this.state == State.DEAD) {
            return;
        }
        this.state = state;
        switch (actor) {
            case FIRST -> {
                switch (state) {
                    case STAND -> {
                        animator.setImg("/pictures/actor/actorStand.png", 2);
                        animator.setArr(3);
                        animator.setDelayCount(30);
                        animator.setPlayLoop();
                    }
                    case RUN -> {
                        animator.setImg("/pictures/actor/run.png", 2);
                        animator.setArr(4);
                        animator.setDelayCount(10);
                        animator.setPlayLoop();
                    }
                    case DEATH -> {
                        animator.setImg("/pictures/actor/actorDead.png", 2);
                        animator.setArr(16);
                        animator.setDelayCount(5);
                        animator.setPlayOnce();
                    }
                    case DEAD -> animator.setArr(0);

                }
            }
            case SECOND -> {
                switch (state) {
                    case STAND -> {
                        animator.setImg("/pictures/actor/actor2Stand(2).png", 2);
                        animator.setArr(12);
                        animator.setDelayCount(10);
                        animator.setPlayLoop();
                    }
                    case RUN -> {
                        animator.setImg("/pictures/actor/actor2Run(2).png", 2);
                        animator.setArr(5);
                        animator.setDelayCount(10);
                        animator.setPlayLoop();
                    }
                    case DEATH -> {
                        animator.setImg("/pictures/actor/actor2Dead (2).png", 2);
                        animator.setArr(6);
                        animator.setDelayCount(30);
                        animator.setPlayOnce();
                    }
                    case DEAD -> animator.setArr(0);

                }
            }
            case THIRD -> {
                switch (state) {
                    case STAND -> {
                        animator.setImg("/pictures/actor/actor3Run.png", 2);
                        animator.setArr(16);
                        animator.setDelayCount(10);
                        animator.setPlayLoop();
                    }
                    case RUN -> {
                        animator.setImg("/pictures/actor/actor3Run(2).png", 2);
                        animator.setArr(5);
                        animator.setDelayCount(10);
                        animator.setPlayLoop();
                    }
                    case DEATH -> {
                        animator.setImg("/pictures/actor/actor3Dead.png", 2);
                        animator.setArr(6);
                        animator.setDelayCount(30);
                        animator.setPlayOnce();
                    }
                    case DEAD -> animator.setArr(0);
                }
            }
        }
    }

    @Override
    public void update() {
        blood.barUpdate(collider().left(), collider().top(), this.life);
        if (life <= 0) {

            if (state == State.DEATH && animator.isFinish()) {
                setState(State.DEAD);
                return;
            }
            if (state != State.DEATH && state != State.DEAD) {
                setState(State.DEATH);
            }
        }
        if (state == State.DEATH || state == State.DEAD) {
            return;
        }
        switch (verticalDir) {
            case UP:
                if (painter().top() < 0) {
                    translateY(moveSpeed);
                }
                break;
            case DOWN:
                if (painter().bottom() > Global.MAP_HEIGHT - 70) {
                    translateY(-moveSpeed);
                }
                break;
        }
        switch (horizontalDir) {
            case RIGHT:
                if (painter().right() > Global.MAP_WIDTH) {
                    translateX(-moveSpeed);
                }
                break;
            case LEFT:
                if (painter().left() < 0) {
                    translateX(moveSpeed);
                }
                break;

            case UP:
                if (painter().top() < 0) {
                    translateY(moveSpeed);
                }
                break;
            case DOWN:
                if (painter().bottom() > Global.MAP_HEIGHT - 15) {
                    translateY(-moveSpeed);
                }
                break;
            case NO:
        }

//        skill.skillUpdate();
        currentGun.gun.update();
        currentGun.gun.translateForActor();
        updatePosition();

    }

    public void rotationUpdate(int mouseX, int mouseY) {
        rotation.rotationUpdate(this.collider().centerX(), this.collider().centerY(),
                this.collider().centerX(), this.collider().centerY(), dir, mouseX, mouseY);
    }

    public Rotation getRotation() {
        return rotation;
    }

    private void updatePosition() {
        Global.actorX = collider().centerX();
        Global.actorY = collider().bottom();
    }

    public class Skill {
        private int healCount;
        private boolean canHeal;
        private Delay healDelay;
        private Delay healCD;
        private final int HP_MAX = 100;
        private Animator healAnimator;
        private final int HEAL_HP = 5;

        private final int FLASH_MAX_DISTANCE = 300;
        private Delay delayForFlash;
        private boolean canFlash;
        private Animator flashAnimator;
        private int XForFlash;
        private int YForFlash;

        private Skill() {
            healCount = 0;
            healDelay = new Delay(45);
            healDelay.loop();
            canHeal = false;
            healCD = new Delay(1800);
            healAnimator = new Animator("/pictures/actor/heal.png", 5, 128, 128, 4);
            healAnimator.setArr(4, 2);

            delayForFlash = new Delay(600);
            canFlash = true;
            flashAnimator = new Animator("/pictures/actor/flash.png", 8, 48, 32, 2);
            flashAnimator.setArr(4);
            flashAnimator.setPlayOnce();
        }

        public void skillUpdate() {
            if (delayForFlash.count()) {
                canFlash = true;
            }
            healCD.count();
            if (canHeal) {
                if (healDelay.count()) {
                    heal();
                }
            }
        }

        public void skillPaint(Graphics g) {
            flashAnimator.paintAnimator(g, XForFlash - 24, XForFlash + 24, YForFlash - 16, YForFlash + 16, dir);
            if (canHeal) {
                healAnimator.paintAnimator(g, painter().left() - 35, painter().right() + 35, painter().top() - 35, painter().bottom() + 35, Dir.RIGHT);
            }
        }


        public void heal(){
            if(life == HP_MAX && !canHeal){
                return;
            }
            if (!canHeal && healCD.isStop()) {
                canHeal = true;
                healCD.play();
            }
            if (canHeal) {
                life += HEAL_HP;
                healCount++;
                healAnimator.setPlayOnce();
            }
            if (life > HP_MAX) {
                life = HP_MAX;
            }
            if (healCount >= 10) {
                canHeal = false;
                healCount = 0;
            }
        }

        public void flash(int mouseX, int mouseY, ArrayList<GameObject> arr) {
            if (state == State.DEATH || state == State.DEAD) {
                return;
            }
                delayForFlash.play();
                flashAnimator.setPlayOnce();
                XForFlash = painter().centerX();
                YForFlash = painter().centerY();
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
                flashInObj(arr);

        }

        private void flashInObj(ArrayList<GameObject> arr) {
            if(arr == null){
                return;
            }
            for (int i = 0; i < arr.size(); i++) {
                if (isCollision(arr.get(i))) {
                    if (XForFlash < arr.get(i).collider().left()) {
                        offSetX(arr.get(i).collider().left() - collider().width());
                    }
                    if (XForFlash > arr.get(i).collider().right()) {
                        offSetX(arr.get(i).collider().right());
                    }
                    if (YForFlash > arr.get(i).collider().bottom()) {
                        offSetY(arr.get(i).collider().bottom());
                    }
                    if (YForFlash < arr.get(i).collider().top()) {
                        offSetY(arr.get(i).collider().top() - collider().height());
                    }
                }
            }
        }

        public Delay getHealCD() {
            return healCD;
        }

        public Delay getDelayForFlash() {
            return delayForFlash;
        }

        public boolean getCanHeal(){
            return canHeal;
        }

        public boolean getCanFlash(){
            return canFlash;
        }



    }
}

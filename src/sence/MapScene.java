/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sence;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import camera.Camera;
import camera.MapInformation;
import controller.MapObjController;
import object.GameObjForAnimator;
import object.GameObjForPic;
import object.monster.BullBoss;
import unit.Global.Direction;
import controller.ImageController;
import object.actor.GameActor;
import static  unit.Global.*;

import weapon.Bullet;
import object.monster.Goblin;
import object.monster.Monster;
import object.GameObject;
import unit.CommandSolver;

public class MapScene extends Scene {
    private ArrayList<GameObject> mapObjArr;
    private boolean shooting ;
    private LinkedList<Bullet> testBullets;
    private LinkedList<Monster> monster;
    private int listenerMouseX;
    private int listenerMouseY;
    private int cameraX;
    private int cameraY;
    private int mouseX;
    private int mouseY;//滑鼠位置
    private GameActor gameActor;//主角
    private Camera camera;//鏡頭
    private Image map;//地圖
    private int actorX;
    private int actorY; //鏡頭中心=主角

    @Override
    public void sceneBegin() {
        map = ImageController.getInstance().tryGet("/map3.png");
        mapInit();
        testBullets = new LinkedList<>();
        MapInformation.setMapInfo(0, 0, WINDOW_WIDTH,WINDOW_HEIGHT);
        monster = new LinkedList<>();
        monster.add(new Goblin(100,100));
        monster.add(new BullBoss(200,200));
        gameActor = new GameActor(Actor.FIRST.getPath(),0,0);
        this.camera = new Camera.Builder(CAMERA_WIDTH, CAMERA_HEIGHT)
                .setChaseObj(gameActor,1,1)
                .setCameraStartLocation(-CAMERA_WIDTH/2,-CAMERA_HEIGHT/2)
                .gen();
    }

    private void cameraUpdate(){
        cameraX = camera.cameraWindowX();//取得鏡頭位於地圖的的左上角位置
        cameraY = camera.cameraWindowY();
        actorX = gameActor.collider().left();//取得人物的位置
        actorY = gameActor.collider().top();
        mouseX = listenerMouseX +cameraX;//滑鼠的絕對座標 ((listenerMouse是滑鼠監聽的回傳值
        mouseY = listenerMouseY +cameraY;
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(final Graphics g) {
        camera.start(g);
        g.drawImage(map,0,0,null);

/*
        monster.forEach(monster -> {
            if(camera.isCollision(monster)){
                monster.paint(g);
            }
          });
*/
        if(camera.isCollision(gameActor)){
            gameActor.paint(g);
        }
        mapObjArr.forEach(a -> a.paint(g));
        testBullets.forEach(testBullet -> testBullet.paint(g));
        camera.paint(g);
        camera.end(g);
    }
    public void bulletsUpdate(){
        for(int i=0 ; i<testBullets.size() ; i++){
            testBullets.get(i).update();
            if(testBullets.get(i).isOut()){
                testBullets.remove(i);
                i--;
                continue;
            }
            int x=0;
            for(int k=0 ; k<mapObjArr.size() ; k++){
                if(testBullets.get(i).isCollied(mapObjArr.get(k))){
                    testBullets.remove(i);
                    i--;
                    x++;
                    break;
                }
            }
            if(x == 0) {
                for (int k = 0; k < monster.size(); k++) {
                    if (testBullets.get(i).isCollied(monster.get(k))) {
                        if(monster.get(k).getLife() >=0){
                            monster.get(k).setLife(monster.get(k).getLife() - testBullets.get(i).getAtk());
                        }
                        testBullets.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
    }

    public void monsterUpdate(){
        for(int i=0 ; i<monster.size()-1 ; i++){
            if(monster.get(i).getLife() <= 0){
                monster.remove(i);
                i--;
                break;
            }
            monster.get(i).update();
            monster.get(i).chase(gameActor.collider().centerX(),gameActor.collider().bottom());
//            if(monster.get(i).isCollisionWithActor(gameActor)){
//                gameActor.setLife(gameActor.getLife()-monster.get(i).getAtk());
//            }
            if(!gameActor.isCollisionWithActor(monster.get(i))){
                monster.get(i).isCollisionWithMonster(monster.get(i+1));
            }
        }
        if(Math.random()*100 <1 && monster.size()<5){
           monster.add(new Goblin(50,(int)(Math.random()*1000)));
        }
    }

    public void shootUpdate(){
        if (shooting) {
            if (gameActor.getGun().isCanShoot()) {
                gameActor.getGun().shoot();
                System.out.println("CAMERA" + cameraX +"///" + cameraY);
                this.testBullets.add(new Bullet
                        (this.gameActor.painter().centerX(), this.gameActor.painter().centerY(),
                                mouseX, mouseY,
                                gameActor.getGun().getGunType()));
            }
        }
    }

    @Override
    public void update() {
        camera.update();
        gameActor.update();
        cameraUpdate();
        //monsterUpdate();
        bulletsUpdate();
        shootUpdate();
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if(state == CommandSolver.MouseState.MOVED || state == CommandSolver.MouseState.DRAGGED){
                gameActor.changeDir(e.getX());
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
            }
            if(state == CommandSolver.MouseState.PRESSED){
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
                shooting = true;
            }

            if(shooting && state == CommandSolver.MouseState.DRAGGED){
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();

            }
            if(state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED){
                shooting = false;
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if(commandCode >= 1 || commandCode <= 4) {
                    gameActor.getAnimator().setDelay().play();
                    gameActor.move(commandCode);
                }
                if(commandCode == Active.RELOADING.getCommandCode()){
                    gameActor.getGun().reloading();
                }
                if(commandCode == Active.NUMBER_ONE.getCommandCode() || commandCode == Active.NUMBER_TWO.getCommandCode()){
                    gameActor.changeGun(commandCode);
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                gameActor.getAnimator().setDelay().stop();
                if (commandCode == Direction.LEFT.ordinal() || commandCode == Direction.RIGHT.ordinal()
                        || commandCode == Direction.UP.ordinal() || commandCode == Direction.DOWN.ordinal()) {
                    gameActor.changeDir(4);
                }
                if(commandCode == Active.FLASH.getCommandCode()){
                    gameActor.flash(listenerMouseX, listenerMouseY);
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    public void mapInit() {
        mapObjArr = new MapObjController.Builder().setBmpAndTxt("genMap.bmp","genMap.txt")
                .setNameAndPath("bananastatue","/banana.png",true,new GameObjForPic("/banana.png",30,30,12,12))
                .gen()
                .setMap();
    }
}

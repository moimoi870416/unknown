/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sence;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import camera.Camera;
import camera.MapInformation;
import unit.Global.Direction;
import controller.ImageController;
import object.GameObjForPic;
import object.actor.GameActor;
import static  unit.Global.*;

import weapon.Bullet;
import maploader.MapInfo;
import maploader.MapLoader;
import object.monster.Goblin;
import object.monster.Monster;
import object.GameObject;
import unit.CommandSolver;

public class MapScene extends Scene {
    private boolean isShooting; //是否射擊
    private ArrayList<GameObject> gameObjectArr;
    private LinkedList<Bullet> testBullets;
    private LinkedList<Monster> monster;
    private int mouseX;
    private int mouseY; //滑鼠位置
    private GameActor gameActor; //主角
    private Camera camera; //鏡頭
    private Image map; //地圖
    private int cameraX;
    private int cameraY; //鏡頭中心=主角

    @Override
    public void sceneBegin() {
        map = ImageController.getInstance().tryGet("/map-grassland.png");
        mapInit();
        testBullets = new LinkedList<>();
        MapInformation.setMapInfo(0, 0, WINDOW_WIDTH,WINDOW_HEIGHT);
        monster = new LinkedList<>();
        monster.add(new Goblin(500 ,450));
//        monster.add(new BullBoss(200,200));
        gameActor = new GameActor(Actor.FIRST.getPath(),720,450);
        this.camera = new Camera.Builder(CAMERA_WIDTH, CAMERA_HEIGHT).setChaseObj(gameActor,1,1).gen();
    }

    private void cameraUpdate(){
        mouseX -=cameraX;
        mouseY -=cameraY;
        cameraX = gameActor.painter().left()-500;
        cameraY = gameActor.painter().top()-500;
        mouseX +=cameraX;
        mouseY  +=cameraY;
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(final Graphics g) {
        camera.start(g);
        g.drawImage(map,0,0,null);
        gameObjectArr.forEach(a -> a.paint(g));
        monster.forEach(monster -> {
            if(camera.isCollision(monster)){
                monster.paint(g);
            }
          });

        if(camera.isCollision(gameActor)){
            gameActor.paint(g);
        }
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
            for(int k=0 ; k<gameObjectArr.size() ; k++){
                if(testBullets.get(i).isCollied(gameObjectArr.get(k))){
                    testBullets.remove(i);
                    i--;
                    x++;
                    break;
                }
            }
            if(x == 0) {
                for (int k = 0; k < monster.size(); k++) {
                    if (testBullets.get(i).isCollied(monster.get(k))) {
                        if(monster.get(k).getLife() <=0){
                            monster.remove(k);
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
        if (isShooting) {
            if (gameActor.getGun().shootingDelay() && gameActor.getGun().shoot()) {
                this.testBullets.add(new Bullet(this.gameActor.painter().centerX(), this.gameActor.painter().centerY(), mouseX, mouseY, gameActor.getGun().getSpeedMove(),gameActor.getGun().getAtk(),gameActor.getGun().getFlyingDistance(), gameActor.getGun().getShootDeviation()));
                //System.out.println(gameActor.getGun().getCount() +"/"+gameActor.getGun().getMagazine());
                System.out.println(gameActor.painter().centerX() + "//" + mouseX);
            }
        }

        if(gameActor.getGun().getIsReloading()){
            gameActor.getGun().reloading();
//            System.out.println(gameActor.getGun().getCount() +"/"+gameActor.getGun().getMagazine());
        }
    }

    @Override
    public void update() {
        camera.update();
        cameraUpdate();
        gameActor.update();
        monsterUpdate();
        bulletsUpdate();
        shootUpdate();
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if(state == CommandSolver.MouseState.MOVED || state == CommandSolver.MouseState.DRAGGED){
                gameActor.changeDir(e.getX()+cameraX);
            }
            if(state == CommandSolver.MouseState.PRESSED){
                mouseX = e.getX() + cameraX;
                mouseY = e.getY() + cameraY;
                isShooting = true;
            }
            if(isShooting && state == CommandSolver.MouseState.DRAGGED){
                mouseX = e.getX() + cameraX;
                mouseY = e.getY() + cameraY;
            }
            if(state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED){
                isShooting = false;
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
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }


    public void mapInit() {
        try {
            final MapLoader mapLoader = new MapLoader("genMap.bmp", "genMap.txt");
            final ArrayList<MapInfo> test = mapLoader.combineInfo();
            gameObjectArr = mapLoader.createObjectArray("Name", 32, test, new MapLoader.CompareClass() {
                @Override
                public GameObject compareClassName(final String gameObject, final String name, final MapInfo mapInfo, final int size) {
                    GameObject tmp;
                    if (gameObject.equals(name)) {
                        tmp = new GameObjForPic("/tree1.png",mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                        return tmp;
                    }
                    return null;
                }
            });
//            for (int i = 0; i < test.size(); i++) {    //  這邊可以看array內容  {String name ,int x, int y, int xSize, int ySize}
//                System.out.println(test.get(i).getName());
//                System.out.println(test.get(i).getX());
//                System.out.println(test.get(i).getY());
//                System.out.println(test.get(i).getSizeX());
//                System.out.println(test.get(i).getSizeY());
//            }
        } catch (final IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

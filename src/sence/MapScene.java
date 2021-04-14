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
import object.GameObjForPic;
import object.monster.BullBoss;
import util.Global.Direction;
import controller.ImageController;
import object.actor.GameActor;
import static  util.Global.*;

import weapon.Bullet;
import object.monster.Goblin;
import object.monster.Monster;
import object.GameObject;
import util.CommandSolver;

public class MapScene extends Scene {
    private ArrayList<GameObject> mapObjArr;
    private boolean shooting ;
    private LinkedList<Bullet> testBullets;
    private LinkedList<Monster> monster;
    private int listenerMouseX;
    private int listenerMouseY;
    private int mouseX;
    private int mouseY;//滑鼠位置
    private GameActor gameActor;//主角
    private Camera camera;//鏡頭
    private Image map;//地圖

    @Override
    public void sceneBegin() {
        map = ImageController.getInstance().tryGet("/map/map.png");
        mapInit();
        testBullets = new LinkedList<>();
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
        monster = new LinkedList<>();
        monster.add(new Goblin(100,100));
        //monster.add(new BullBoss(200,200));
        gameActor = new GameActor(Actor.FIRST.getPath(),50,700);
        this.camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
                .setCameraMoveSpeed(2)
                .setChaseObj(gameActor,1,1)
                .setCameraStartLocation(-WINDOW_WIDTH /2,-WINDOW_HEIGHT /2)
                .gen();
    }

    private void mouseUpdate(){
        mouseX = listenerMouseX + camera.getCameraWindowX();//滑鼠的絕對座標 ((listenerMouse是滑鼠監聽的回傳值
        mouseY = listenerMouseY + camera.getCameraWindowY();
        gameActor.changeDir(mouseX);
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(final Graphics g) {
        camera.start(g);
        //g.drawImage(map,0,0,null);
        monster.forEach(monster -> {
            if(camera.isCollision(monster)){
                monster.paint(g);
            }
          });
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
                        int life = monster.get(k).getLife();
                        monster.get(k).offLife(testBullets.get(i).getAtk());
                        if(monster.get(k).getLife()<=0){
                            monster.remove(k);
                            k--;
                        }
                        if(testBullets.get(i).isPenetrate(life)) {
                            testBullets.remove(i);
                            i--;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void monsterUpdate(){
        for(int i=0 ; i<monster.size()-1 ; i++){
            monster.get(i).update();
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
            if (gameActor.getGun().shoot()) {
//                gameActor.getGun().shoot();
                this.testBullets.add(new Bullet
                        (this.gameActor.painter().centerX(), this.gameActor.painter().centerY(),
                                mouseX, mouseY,
                                gameActor.getGun().getGunType()));
                System.out.println(shootCount);
                shootCount++;
            }
        }
    }

    @Override
    public void update() {
        mouseUpdate();
        camera.update();
        gameActor.update();
        monsterUpdate();
        bulletsUpdate();
        shootUpdate();
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if(state == CommandSolver.MouseState.MOVED || state == CommandSolver.MouseState.DRAGGED){
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
            }
            if(state == CommandSolver.MouseState.PRESSED){
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
                gameActor.getGun().beginShoot();
                shooting = true;
            }

            if(shooting && state == CommandSolver.MouseState.DRAGGED){
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();

            }
            if(state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED){
                shooting = false;
                shootCount = 0;
                gameActor.getGun().resetBeginShoot();
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if(commandCode >= 1 || commandCode <= 4) {
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
                if (commandCode == Direction.LEFT.ordinal() || commandCode == Direction.RIGHT.ordinal()
                        || commandCode == Direction.UP.ordinal() || commandCode == Direction.DOWN.ordinal()) {
                    gameActor.move(commandCode);
                }
                if(commandCode == Active.SPACE.getCommandCode()){
                    gameActor.flash(mouseX,mouseY);
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    public void mapInit() {
        mapObjArr = new MapObjController.Builder().setBmpAndTxt("genMap.bmp","genMap.txt")
                .setNameAndPath("bananastatue", "/map/banana.png",true,new GameObjForPic("/map/banana.png",0,370,168,30))
                .setNameAndPath("tree1", "/map/tree3-208-336.png",true,new GameObjForPic("/map/tree3-208-336.png",0,100,208,336))
                .setNameAndPath("rock1","/map/rock-sand1-424-216.png",false,new GameObjForPic("/map/rock-sand1-424-216.png",0,0,424,216))
                .setNameAndPath("rock2","/map/rock-sand1-584-216.png",false,new GameObjForPic("/map/rock-sand1-584-216.png",0,0,584,216))
                .gen()
                .setMap();
    }
}

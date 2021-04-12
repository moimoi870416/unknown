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
import unit.Global.Direction;
import controller.ImageController;
import object.actor.GameActor;
import object.monster.BullBoss;
import unit.Global;
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
    private int mouseX;
    private int mouseY;
    private GameActor gameActor;
    private Camera camera;
    private Image map;
    private int actorX;
    private int actorY;

    @Override
    public void sceneBegin() {
        map = ImageController.getInstance().tryGet("/map3.png");
        mapInit();
        testBullets = new LinkedList<>();
        MapInformation.setMapInfo(0, 0, Global.WINDOW_WIDTH,Global.WINDOW_HEIGHT);
        monster = new LinkedList<>();
        monster.add(new Goblin(100,100));
        monster.add(new BullBoss(200,200));
        gameActor = new GameActor(Global.Actor.FIRST.getPath(),720,450);
        this.camera = new Camera.Builder(Global.CAMERA_WIDTH, Global.CAMERA_HEIGHT).setChaseObj(gameActor,1,1).setCameraStartLocation(0,0).setCameraWindowLocation(0,0).gen();
    }

    private void cameraUpdate(){
        actorX = gameActor.painter().left();
        actorY = gameActor.painter().top();
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(final Graphics g) {
        camera.start(g);
        g.drawImage(map,0,0,null);


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
                        if(monster.get(k).getLife() <=0){
                            monster.get(k).setDir(GameObjForAnimator.Dir.DEAD);
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
        for(int i=0 ; i<monster.size() ; i++){
            if(monster.get(i).getDir() == GameObjForAnimator.Dir.DEAD){
                monster.get(i).update();
            }else{
                monster.get(i).chase(gameActor.collider().centerX(), gameActor.collider().bottom());
                if (monster.get(i).isCollision(gameActor)) {
                    gameActor.setLife(gameActor.getLife() - monster.get(i).getAtk());
                }
            }
        }
        if(Math.random()*100 <1 && monster.size()<25){
           monster.add(new Goblin(50,(int)(Math.random()*1000)));
        }
    }

    public void shootUpdate(){
        if (shooting) {
            mouseX = listenerMouseX+actorX-Global.CAMERA_WIDTH/2+30;
            mouseY = listenerMouseY+actorY-Global.CAMERA_HEIGHT/2+30;
            if (gameActor.getGun().isCanShoot()) {
                gameActor.getGun().shoot();
                this.testBullets.add(new Bullet(this.gameActor.painter().centerX(), this.gameActor.painter().centerY(), mouseX, mouseY,gameActor.getGun().getGunType()));
            }
        }
    }

    @Override
    public void update() {
        camera.update();
        gameActor.update();
        cameraUpdate();
        monsterUpdate();
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
                if(commandCode == Global.Active.RELOADING.getCommandCode()){
                    gameActor.getGun().reloading();
                }
                if(commandCode == Global.Active.NUMBER_ONE.getCommandCode() || commandCode == Global.Active.NUMBER_TWO.getCommandCode()){
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
                if(commandCode == Global.Active.FLASH.getCommandCode()){
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

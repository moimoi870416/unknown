/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sence;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import object.GameObjForPic;
import object.actor.GameActor;
import weapon.Bullet;
import maploader.MapInfo;
import maploader.MapLoader;
import object.monster.Goblin;
import object.monster.Monster;
import object.GameObject;
import unit.CommandSolver;

public class MapScene extends Scene {
    private boolean shooting ;
    private int shootingCount;
    private ArrayList<GameObject> gameObjectArr;
    private LinkedList<Bullet> testBullets;
    private LinkedList<Monster> monster;
    private int mouseX;
    private int mouseY;
    private GameActor gameActor;


    @Override
    public void sceneBegin() {
        testBullets = new LinkedList<>();
        monster = new LinkedList<>();
        monster.add(new Goblin(50,50));
        gameActor = new GameActor(720,450);
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

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(final Graphics g) {
        monster.forEach(monster -> monster.paint(g));
        gameActor.paint(g);
        testBullets.forEach(testBullet -> testBullet.paint(g));
        gameObjectArr.forEach(a -> a.paint(g));
    }

    @Override
    public void update() {
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
        monster.forEach(monster->monster.chase(gameActor.collider().centerX(),gameActor.collider().centerY()));

        if(Math.random()*100 <1 && monster.size()<20){
            monster.add(new Goblin(50,(int)(Math.random()*900)));
        }

        if (shooting) {
            if (shootingCount % gameActor.getGun().getFiringSpeed() == 0 && gameActor.getGun().shoot()) {
                shoot();
            }
            shootingCount++;
        }
        if(gameActor.getGun().getIsReloading()){
            gameActor.getGun().reloading();
            System.out.println(gameActor.getGun().getCount() +"/"+gameActor.getGun().getMagazine());
        }
    }

    private void shoot(){
        this.testBullets.add(new Bullet(this.gameActor.painter().centerX() -5, this.gameActor.painter().centerY()-5, mouseX, mouseY, gameActor.getGun().getSpeedMove(),gameActor.getGun().getAtk(),gameActor.getGun().getFlyingDistance(), gameActor.getGun().getShootDeviation()));
        System.out.println(gameActor.getGun().getCount() +"/"+gameActor.getGun().getMagazine());
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if(state == CommandSolver.MouseState.MOVED || state == CommandSolver.MouseState.DRAGGED){
                gameActor.changeDir(e.getX());
            }
            if(state == CommandSolver.MouseState.PRESSED){
                mouseX = e.getX();
                mouseY = e.getY();
                shooting = true;
            }
            if(shooting && state == CommandSolver.MouseState.DRAGGED){
                mouseX = e.getX();
                mouseY = e.getY();
            }
            if(state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED){
                shooting = false;
                shootingCount = 0;
            }

        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                gameActor.getAnimator().setDelay().play();
                gameActor.move(commandCode);
                if(commandCode == 30){
                    gameActor.getGun().reloading();
                }
                if(commandCode == -1 || commandCode == -2){
                    gameActor.changeGun(commandCode);
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                gameActor.getAnimator().setDelay().stop();
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }
}

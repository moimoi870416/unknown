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

import actor.GameActor;
import bullet.Bullet;
import maploader.MapInfo;
import maploader.MapLoader;
import monster.Goblin;
import monster.Monster;
import objectdata.GameObject;
import bullet.TestBullet;
import object.Tree;
import unit.CommandSolver;

public class MapScene extends Scene {
    private boolean shooting ;
    private int shootingCount;
    private ArrayList<GameObject> gameObjectArr;
    private LinkedList<Bullet> testBullets;
    private int mouseX;
    private int mouseY;
    GameActor gameActor;
    Monster monster;

    @Override
    public void sceneBegin() {
        testBullets = new LinkedList<>();
        monster = new Goblin(200,200);
        gameActor = new GameActor(0,720,450);
        try {
            final MapLoader mapLoader = new MapLoader("genMap.bmp", "genMap.txt");
            final ArrayList<MapInfo> test = mapLoader.combineInfo();
            gameObjectArr = mapLoader.createObjectArray("Name", 32, test, new MapLoader.CompareClass() {
                @Override
                public GameObject compareClassName(final String gameObject, final String name, final MapInfo mapInfo, final int size) {
                    GameObject tmp = null;
                    if (gameObject.equals(name)) {
                        tmp = new Tree(mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
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
            this.gameObjectArr.forEach(a -> System.out.println(a.getClass().getSimpleName()));
        } catch (final IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(final Graphics g) {
        monster.paint(g);
        gameActor.paint(g);
        testBullets.forEach(testBullet -> testBullet.paint(g));
        this.gameObjectArr.forEach(a -> a.paint(g));
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
            for(int k=0 ; k<gameObjectArr.size() ; k++){
                if(testBullets.get(i).isCollied(gameObjectArr.get(k))){
                    testBullets.remove(i);
                    i--;
                    break;
                }
            }
        }
        monster.chase(gameActor.painter().centerX(),gameActor.painter().centerY());
        if (shooting) {
            if (shootingCount % 10 == 0) {
                shoot();
            }
            shootingCount++;
        }
        System.out.println(testBullets.size());
    }


    private void shoot(){
        this.testBullets.add(new TestBullet(this.gameActor.painter().centerX() -6, this.gameActor.painter().centerY()-6, 12, 11, mouseX, mouseY));
    }



    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
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
                gameActor.move(commandCode);
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {

            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }
}

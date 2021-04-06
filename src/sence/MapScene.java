/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sence;

import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import actor.GameActor;
import maploader.MapInfo;
import maploader.MapLoader;
import monster.Monster;
import object.GameObject;
import object.TestBullet;
import object.Tree;
import unit.CommandSolver;

public class MapScene extends Scene {
    private ArrayList<GameObject> gameObjectArr;
    private ArrayList<TestBullet> testBullets;
    GameActor gameActor;
    Monster monster;

    @Override
    public void sceneBegin() {
        testBullets = new ArrayList<>();
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
        gameActor.paint(g);
        testBullets.forEach(testBullet -> testBullet.paint(g));
        this.gameObjectArr.forEach(a -> a.paint(g));
    }

    @Override
    public void update() {
        for(int i=0 ; i<testBullets.size() ; i++){
            testBullets.get(i).update();
            if (testBullets.get(i).isOut()) {
                testBullets.remove(i);
                i--;
                continue;
            }
            for(int k=0 ; k<gameObjectArr.size() ; k++){
                if(gameObjectArr.get(k).isCollied(testBullets.get(i))){
                    testBullets.remove(i);
                    i--;
                    break;
                }
            }

        }

    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if(state == CommandSolver.MouseState.PRESSED) {
                this.testBullets.add(new TestBullet(this.gameActor.left() + 12, this.gameActor.top() + 12, gameActor.right() - 12, gameActor.bottom() - 11, e.getX(), e.getY()));
            }

        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                gameActor.keyPressed(commandCode, trigTime);
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

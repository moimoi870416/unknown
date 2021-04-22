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
import object.monster.*;
import util.Display;
import controller.ImageController;
import object.actor.GameActor;
import static util.Global.*;
import weapon.Bullet;
import object.GameObject;
import util.CommandSolver;
import weapon.Gun;

public class MapScene extends Scene {
    private ArrayList<GameObject> mapObjArr;
    private LinkedList<Bullet> testBullets;
    private LinkedList<Monster> monster;
    private int listenerMouseX;
    private int listenerMouseY;
    private GameActor gameActor;//主角
    private Camera camera;//鏡頭
    private Display display;//畫面物件
    private MapInfo mapInfo;
    private boolean shooting;

    @Override
    public void sceneBegin() {
        mapObjArr = new ArrayList<>();
        monster = new LinkedList<>();
        testBullets = new LinkedList<>();
        mapInfo = new MapInfo();
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
        gameActor = new GameActor(Actor.FIRST.getPath(), 300, 700);
        this.camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
                .setCameraMoveSpeed(2)
                .setChaseObj(gameActor, 20, 20)
                .setCameraStartLocation(-WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2)
                .gen();
        display = new Display(gameActor);
    }

    private void mouseUpdate() {
        mouseX = listenerMouseX + camera.getCameraWindowX();//滑鼠的絕對座標 ((listenerMouse是滑鼠監聽的回傳值
        mouseY = listenerMouseY + camera.getCameraWindowY();
        gameActor.changeDir(mouseX);
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void paint(final Graphics g) {
        camera.start(g);//鏡頭一定要第一個PAINT
        mapInfo.mapPaint(g);
        monster.forEach(monster -> {
            if (camera.isCollision(monster)) {
                monster.paint(g);
            }
        });
        if (camera.isCollision(gameActor)) {
            gameActor.paint(g);
        }
        //mapObjArr.forEach(a -> a.paint(g));
        mapObjArr.forEach(object ->{
            if (camera.isCollision(object)) {
                object.paint(g);
            }
        });
        testBullets.forEach(testBullet -> testBullet.paint(g));
        camera.paint(g);
        camera.end(g);
        display.paint(g);
    }


    public void bulletsUpdate() {
        for (int i = 0; i < testBullets.size(); i++) {
            if (testBullets.get(i).getState() == Bullet.State.STOP) {
                testBullets.remove(i);
                i--;
                break;
            }
            testBullets.get(i).update();
            if (testBullets.get(i).isOut()) {
                testBullets.remove(i);
                i--;
                continue;
            }
            int x = 0;
            for (int k = 0; k < mapObjArr.size(); k++) {
                if (testBullets.get(i).isCollied(mapObjArr.get(k))) {
                    testBullets.remove(i);
                    i--;
                    x++;
                    break;
                }
            }
            if (x == 0) {
                for (int k = 0; k < monster.size(); k++) {
                    if (testBullets.get(i).isColliedInHit(monster.get(k))) {
                        if (monster.get(k).getState() != GameObjForAnimator.State.DEATH) {
                            int life = monster.get(k).getLife();
                            monster.get(k).offLife(testBullets.get(i).getAtk());
                            if (monster.get(k).getLife() <= 0) {
                                monster.get(k).setState(GameObjForAnimator.State.DEATH);
                            }
                            if (testBullets.get(i).isPenetrate(life)) {
//                                testBullets.remove(i);
//                                i--;
//                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void monsterUpdate() {
        for (int i = 0; i < monster.size(); i++) {
            if (monster.get(i).getState() == GameObjForAnimator.State.DEAD) {
                monster.remove(i);
                i--;
                break;
            }
            monster.get(i).update();
            if (monster.get(i).isCollisionWithActor(gameActor)) {
               monster.get(i).attack(gameActor);
            }
            for (int k = 0; k < mapObjArr.size(); k++) {
                monster.get(i).isCollider(mapObjArr.get(k));
            }
            if (monster.size() > 1 && i != monster.size() - 1) {
                if (!gameActor.isCollisionWithActor(monster.get(i))) {
                    monster.get(i).isCollisionWithMonster(monster.get(i + 1));
                }
            }
        }

    }

    public void shootUpdate() {
        if (shooting) {
            if (gameActor.getCurrentGun().shoot()) {
                this.testBullets.add(new Bullet
                        (this.gameActor.painter().centerX(), this.gameActor.painter().centerY(),
                                mouseX, mouseY,
                                gameActor.getCurrentGun().getGunType()));
                shootCount++;
            }
        }
    }

    public void actorUpdate() {
        gameActor.update();
        for (int i = 0; i < mapObjArr.size(); i++) {
            gameActor.isCollider(mapObjArr.get(i));
        }
    }

    @Override
    public void update() {
        mouseUpdate();
        camera.update();
        actorUpdate();
        monsterUpdate();
        bulletsUpdate();
        shootUpdate();
        mapInfo.mapUpdate();
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state == CommandSolver.MouseState.MOVED || state == CommandSolver.MouseState.DRAGGED) {
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
            }
            if (state == CommandSolver.MouseState.PRESSED) {
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();
                gameActor.getCurrentGun().beginShoot();
                shooting = true;
                if(gameActor.getCurrentGun().getGunType() == Gun.GunType.MACHINE_GUN){
                    gameActor.setMoveSpeed(gameActor.getCurrentGun().getGunType().getMoveSpeed()/2);
                }
            }

            if (shooting && state == CommandSolver.MouseState.DRAGGED) {
                listenerMouseX = e.getX();
                listenerMouseY = e.getY();

            }
            if (state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED) {
                shooting = false;
                if(shooting) {
                    if (gameActor.getCurrentGun().shoot()) {
                        this.testBullets.add(new Bullet
                                (this.gameActor.painter().centerX(), this.gameActor.painter().centerY(),
                                        mouseX, mouseY,
                                        gameActor.getCurrentGun().getGunType()));
                    }
                }
                if(gameActor.getCurrentGun().getGunType() == Gun.GunType.MACHINE_GUN){
                    gameActor.setMoveSpeed(gameActor.getCurrentGun().getGunType().getMoveSpeed());
                }
                shootCount = 0;

                gameActor.getCurrentGun().resetBeginShoot();
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode >= 1 || commandCode <= 4) {
                    gameActor.move(commandCode);
                }
                if (commandCode == Active.RELOADING.getCommandCode()) {
                    gameActor.getCurrentGun().reloading();
                }
                if (commandCode == Active.NUMBER_ONE.getCommandCode() || commandCode == Active.NUMBER_TWO.getCommandCode()) {
                    gameActor.changeGun(commandCode);
                }

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (commandCode >= 1 || commandCode <= 4) {
                    gameActor.setState(GameObjForAnimator.State.STAND);
                }
                if (commandCode == Active.SPACE.getCommandCode()) {
                    gameActor.getSkill().flash(mouseX, mouseY,mapObjArr);
                }
                if(commandCode == Active.SKILL.getCommandCode()){
                    gameActor.getSkill().heal();
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }

    public class MapInfo {
        //之後會有切換圖片的行為，所以先開一個內部類
        private Image mapLeft;
        private Image mapMiddle;
        private Image mapRight;
        private Image mapFinal;
        private Image begin;
        private Image forest;
        private Image change;
        private Image desert;
        private Image boss;
        private final int mapWidth = 2048;
        private int count;

        public MapInfo() {
            mapBegin();
            mapForest(4096);
            mapChange(8192);
            mapDesert(10240);
            mapDesert(14336);
            mapBoss(18432);
            begin = ImageController.getInstance().tryGet(MapPath.BEGIN.mapPath);
            forest = ImageController.getInstance().tryGet(MapPath.FOREST.mapPath);
            desert = ImageController.getInstance().tryGet(MapPath.DESERT.mapPath);
            change = ImageController.getInstance().tryGet(MapPath.CHANGE.mapPath);
            boss = ImageController.getInstance().tryGet(MapPath.END.mapPath);

            mapLeft = begin;
            mapMiddle = forest;
            mapRight = forest;
            mapFinal = forest;
            this.count = 0;
        }

        public void mapPaint(Graphics g) {
            g.drawImage(mapLeft, mapWidth * count, 0, null);
            g.drawImage(mapMiddle, mapWidth * (count + 1), 0, null);
            g.drawImage(mapRight, mapWidth * (count + 2), 0, null);
            g.drawImage(mapFinal,mapWidth * (count +3),0,null);
        }

        public void mapUpdate() {
            if(actorX > 4096){
                mapLeft = forest;
                mapMiddle = forest;
                mapRight = forest;
                mapFinal = change;
                count = 1;
                if(actorX > 8192){
                    mapMiddle = change;
                    mapRight = desert;
                    mapFinal = desert;
                    count = 3;
                    if(actorX > 12288){
                        mapLeft = desert;
                        mapMiddle = desert;
                        count = 5;
                        if(actorX > 16384){
                            mapFinal = boss;
                            count = 6;
//                            if(actorX > 18432){
//                                camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
//                                        .setCameraMoveSpeed(2)
//                                        .setChaseObj(gameActor, 20, 20)
//                                        .setCameraStartLocation(-WINDOW_WIDTH / 2+18432, -WINDOW_HEIGHT / 2)
//                                        .gen();
//                                MAP_HEIGHT = 3072;
//                            }else {
//                                camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
//                                        .setCameraMoveSpeed(2)
//                                        .setChaseObj(gameActor, 20, 20)
//                                        .setCameraStartLocation(-WINDOW_WIDTH / 2, -WINDOW_HEIGHT / 2)
//                                        .gen();
//                                MAP_HEIGHT = 1024;
//                            }
                        }
                    }
                }
            }else {
                mapLeft = begin;
                mapMiddle = forest;
                mapRight = forest;
                mapFinal = forest;
                count = 0;
            }
        }

        private void mapBegin() {
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("beginMap.bmp", "beginMap.txt")
                    .setNameAndPath("bananaStatue", "/map/banana.png", true, new GameObjForPic("/map/banana.png", 0, 156, 168, 256))
                    .setNameAndPath("tree1", "/map/tree1-208-336.png", true, new GameObjForPic("/map/tree1-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree2", "/map/tree2-208-336.png", true, new GameObjForPic("/map/tree2-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree3", "/map/tree3-208-336.png", true, new GameObjForPic("/map/tree3-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("rock1", "/map/rock-sand1-424-216.png", true, new GameObjForPic("/map/rock-sand1-424-216.png", 0, 50, 425, 212))
                    .gen()
                    .setMap());

            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("beginMap.bmp", "beginMap.txt")
                    .setX(MAP_UNIT_WIDTH)
                    .setNameAndPath("tree1", "/map/tree1-208-336.png", true, new GameObjForPic("/map/tree1-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree2", "/map/tree2-208-336.png", true, new GameObjForPic("/map/tree2-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree3", "/map/tree3-208-336.png", true, new GameObjForPic("/map/tree3-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("rock1", "/map/rock-sand1-424-216.png", true, new GameObjForPic("/map/rock-sand1-424-216.png", 0, 50, 425, 212))
                    .gen()
                    .setMap());

            monster.add(new BullBoss(1200,500));

        }

        private void mapForest(int x){
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("beginMap.bmp", "beginMap.txt")
                    .setX(x)
                    .setNameAndPath("tree1", "/map/tree1-208-336.png", true, new GameObjForPic("/map/tree1-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree2", "/map/tree2-208-336.png", true, new GameObjForPic("/map/tree2-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree3", "/map/tree3-208-336.png", true, new GameObjForPic("/map/tree3-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("rock1", "/map/rock-sand1-424-216.png", true, new GameObjForPic("/map/rock-sand1-424-216.png", 0, 50, 425, 212))
                    .gen()
                    .setMap());
        }

        private void mapChange(int x){
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("changeMap.bmp", "changeMap.txt")
                    .setX(x)
                    .setNameAndPath("changetree1","/map/change-tree1(268-272).png",true,new GameObjForPic("/map/change-tree1(268-272).png", 0, 0, 242, 272))
                    .setNameAndPath("changetree2","/map/change-tree1(242-271).png",true,new GameObjForPic("/map/change-tree1(242-271).png", 0, 0, 242, 272))
                    .setNameAndPath("deserttree1","/map/desert-tree1(239-272).png",true,new GameObjForPic("/map/desert-tree1(239-272).png", 0, 0, 239, 272))
                    .setNameAndPath("signright","/map/signRight(152-104).png",true,new GameObjForPic("/map/signRight(152-104).png", 0, 0, 152, 104))
                    .setNameAndPath("sandbag1","/map/sandbag1(272-100).png",true,new GameObjForPic("/map/sandbag1(272-100).png", 0, 0, 272, 100))
                    .setNameAndPath("rock2", "/map/rock-sand1-584-216.png", true,new GameObjForPic("/map/rock-sand1-584-216.png", 0, -200, 584, 216))
                    .setNameAndPath("sandbag2","/map/sandbag2(288-80).png",true,new GameObjForPic("/map/sandbag2(288-80).png", 0, 0, 288, 80))
                    .setNameAndPath("rocko1","/map/rocko(208-136).png",true,new GameObjForPic("/map/rocko(208-136).png", 0, 0, 208, 136))
                    .setNameAndPath("rocko2","/map/rocko(208-120).png",true,new GameObjForPic("/map/rocko(208-120).png", 0, 0, 208, 120))
                    .gen()
                    .setMap());
        }

        private void mapDesert(int x){
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("desertMap.bmp", "desertMap.txt")
                    .setX(x)
                    .setNameAndPath("deserttree1", "/map/deserttree1(400-344).png", true, new GameObjForPic("/map/deserttree1(400-344).png", 0, 0, 400, 344))
                    .setNameAndPath("deserttree2", "/map/deserttree2(400-344).png", true, new GameObjForPic("/map/deserttree2(400-344).png", 0, 0, 400, 344))
                    .setNameAndPath("deserttree3", "/map/deserttree3(192-240).png", true, new GameObjForPic("/map/deserttree3(192-240).png", 0, 0, 192, 240))
                    .setNameAndPath("deserttree4", "/map/deserttree4(192-240).png", true, new GameObjForPic("/map/deserttree4(192-240).png", 0, 0, 192, 240))
                    .setNameAndPath("tree1", "/map/desert-tree1(222-254).png", true, new GameObjForPic("/map/desert-tree1(222-254).png", 0, 0, 222, 254))
                    .gen()
                    .setMap());
        }

        private void mapBoss(int x){
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("bossMap.bmp", "bossMap.txt")
                    .setX(x)
                    .setNameAndPath("farmhay","/map/farm_hay(184-144).png",true,new GameObjForPic("/map/farm_hay(184-144).png", 0, 0, 184, 144))
                    .setNameAndPath("farmhay2","/map/farmhay(72-160).png",true,new GameObjForPic("/map/farmhay(72-160).png", 0, 0, 72, 160))
                    .setNameAndPath("signright","/map/signRight(152-104).png",true,new GameObjForPic("/map/signRight(152-104).png", 0, -150, 152, 104))
                    .setNameAndPath("warmsign","/map/warmsign(240-160).png",true,new GameObjForPic("/map/warmsign(240-160).png", 0, -150, 240, 160))
                    .setNameAndPath("verticlawall1","/map/vertical_wall(24-216).png",true,new GameObjForPic("/map/vertical_wall(24-216).png", 0, 0, 32, 216))
                    .setNameAndPath("verticalwall2","/map/vertical_wall2(24-216).png",true,new GameObjForPic("/map/vertical_wall2(24-216).png", 0, 0, 32, 216))
                    .setNameAndPath("wall1","/map/horizontal_wall(176-96).png",true,new GameObjForPic("/map/horizontal_wall(176-96).png", 0, 0, 176, 96))
                    .setNameAndPath("wall2","/map/horizontal_wall(288-96).png",true,new GameObjForPic("/map/horizontal_wall(288-96).png", 0, 0, 288, 96))
                    .setNameAndPath("wall3","/map/horizontal_wall(304-96).png",true,new GameObjForPic("/map/horizontal_wall(304-96).png", 0, 0, 304, 96))
                    .setNameAndPath("oasis1","/map/oasis_tree (232-400).png",true,new GameObjForPic("/map/oasis_tree (232-400).png", 110, 0, 200, 400))
                    .setNameAndPath("oasis2","/map/oasis_tree2(232-400).png",true,new GameObjForPic("/map/oasis_tree2(232-400).png", 130, 0, 232, 400))
                    .gen()
                    .setMap());
        }

    }
}

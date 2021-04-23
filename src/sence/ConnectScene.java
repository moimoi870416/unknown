package sence;

import camera.MapInformation;
import client.ClientClass;
import client.CommandReceiver;
import controller.ImageController;
import controller.MapObjController;
import object.GameObjForPic;
import object.actor.GameActor;
import server.Server;
import controller.ConnectController;
import util.Global;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Global.*;

public class ConnectScene extends GameScene{
    private int playerCount;


    @Override
    protected void sceneBeginComponent() {
        connectLanArea();
        MAP_WIDTH = 19000;
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
        mapInfo = new ConnectSceneMapInfo();
        playerCount = 0;
        gameActorArr.add(new GameActor(Actor.SECOND,500,500));
        gameActorArr.get(playerCount++).setConnectID(ClientClass.getInstance().getID());

    }

    @Override
    protected void sceneEndComponent() {

    }

    @Override
    protected void connectUpdate() {
        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int commandCode, ArrayList<String> strs) {
                if(serialNum == gameActorArr.get(0).getConnectID()){
                    return;
                }
                switch (commandCode){
                    case  NetEvent.CONNECT: //自行定義所接收之指令代碼需要做什麼任務
                        boolean isBorn = false;
                        for (int i = 0; i < gameActorArr.size(); i++) {
                            if (gameActorArr.get(i).getConnectID() == serialNum) {
                                isBorn = true;
                                break;
                            }
                        }
                        if (!isBorn) {
                            gameActorArr.add(new GameActor(Actor.SECOND, Integer.parseInt(strs.get(0)),
                                    Integer.parseInt(strs.get(1))));
                            gameActorArr.get(playerCount++).setConnectID(serialNum);
                        }
                        break;
                    case NetEvent.ACTOR:
                        ConnectController.getInstance().actorReceive(gameActorArr,serialNum,strs);
                            break;
                    case NetEvent.BULLET_NEW:
                        ConnectController.getInstance().newBulletReceive(testBullets,strs);

                }
            }
        });
    }

    public void connectLanArea(){
        Scanner sc = new Scanner(System.in);

        System.out.println("創建伺服器 => 1, 連接其他伺服器 => 2");
        int opt = sc.nextInt();
        switch (opt) {
            case 1:
                Global.isServer = true;
                Server.instance().create(12345);
                Server.instance().start();
                System.out.println(Server.instance().getLocalAddress()[0]);
                try {
                    ClientClass.getInstance().connect("127.0.0.1", 12345); // ("SERVER端IP", "SERVER端PORT")
                } catch (IOException ex) {
                    Logger.getLogger(ConnectScene.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                System.out.println("請輸入主伺服器IP:");
                try {
                    ClientClass.getInstance().connect(sc.next(), 12345); // ("SERVER端IP", "SERVER端PORT")
                } catch (IOException ex) {
                    Logger.getLogger(ConnectScene.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }

    }

    public class ConnectSceneMapInfo extends MapInfo{
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

        public ConnectSceneMapInfo(){
            mapBegin();
            mapForest(4096);
            mapChange(8192);
            mapDesert(10240);
            mapDesert(14336);
            mapBoss(18432);
            begin = ImageController.getInstance().tryGet(Global.MapPath.BEGIN.mapPath);
            forest = ImageController.getInstance().tryGet(Global.MapPath.FOREST.mapPath);
            desert = ImageController.getInstance().tryGet(Global.MapPath.DESERT.mapPath);
            change = ImageController.getInstance().tryGet(Global.MapPath.CHANGE.mapPath);
            boss = ImageController.getInstance().tryGet(Global.MapPath.END.mapPath);

            mapLeft = begin;
            mapMiddle = forest;
            mapRight = forest;
            mapFinal = forest;
            this.count = 0;
        }

        @Override
        public void mapPaint(Graphics g) {
            g.drawImage(mapLeft, mapWidth * count, 0, null);
            g.drawImage(mapMiddle, mapWidth * (count + 1), 0, null);
            g.drawImage(mapRight, mapWidth * (count + 2), 0, null);
            g.drawImage(mapFinal,mapWidth * (count +3),0,null);
        }

        @Override
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
        private int randomY(){
            return random(336,1000);
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

//            for(int i=0 ; i<30 ; i++){
//                monster.add(new SmallMonster(random(2000,2500),randomY(), SmallMonster.Type.GOBLIN));
//                monster.add(new Stone(random(2000,2500),randomY()));
//                if(i%2 == 0){
//                    monster.add(new SmallMonster(random(2000,2500),randomY(), SmallMonster.Type.MUSHROOM));
//                }
//                if(i%3 == 0){
//                    monster.add(new Rino(random(2500,3200),randomY()));
//                }
//            }


        }

        private void mapForest(int x){//4096
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("beginMap.bmp", "beginMap.txt")
                    .setX(x)
                    .setNameAndPath("tree1", "/map/tree1-208-336.png", true, new GameObjForPic("/map/tree1-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree2", "/map/tree2-208-336.png", true, new GameObjForPic("/map/tree2-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("tree3", "/map/tree3-208-336.png", true, new GameObjForPic("/map/tree3-208-336.png", 0, 0, 208, 336))
                    .setNameAndPath("rock1", "/map/rock-sand1-424-216.png", true, new GameObjForPic("/map/rock-sand1-424-216.png", 0, 50, 425, 212))
                    .gen()
                    .setMap());
        }

        private void mapChange(int x){//8192
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

        private void mapDesert(int x){//10240 && 14336
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

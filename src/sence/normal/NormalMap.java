package sence.normal;

public class NormalMap {

    public NormalMap(){

    }

}


/*

    public void mapInit() {
        mapObjArr = new MapObjController.Builder().setBmpAndTxt("beginMap.bmp", "beginMap.txt")
//                .setNameAndPath("bananastatue", "/map/banana.png", true, new GameObjForPic("/map/banana.png", 0, 156, 168, 256))
//                .setNameAndPath("tree1", "/map/tree1-208-336.png", true, new GameObjForPic("/map/tree1-208-336.png", 0, 0, 208, 336))
//                .setNameAndPath("tree2", "/map/tree2-208-336.png", true, new GameObjForPic("/map/tree2-208-336.png", 0, 0, 208, 336))
//                .setNameAndPath("tree3", "/map/tree3-208-336.png", true, new GameObjForPic("/map/tree3-208-336.png", 0, 0, 208, 336))
//                .setNameAndPath("rock1", "/map/rock-sand1-424-216.png", true, new GameObjForPic("/map/rock-sand1-424-216.png", 0, 50, 425, 212))
                .gen()
                .setMap();

//        mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("beginMap.bmp", "beginMap.txt")
//                .setX(MAP_UNIT_WIDTH)
//                .setNameAndPath("tree1", "/map/tree1-208-336.png", true, new GameObjForPic("/map/tree1-208-336.png", 0, 0, 208, 336))
//                .setNameAndPath("tree2", "/map/tree2-208-336.png", true, new GameObjForPic("/map/tree2-208-336.png", 0, 0, 208, 336))
//                .setNameAndPath("tree3", "/map/tree3-208-336.png", true, new GameObjForPic("/map/tree3-208-336.png", 0, 0, 208, 336))
//                .setNameAndPath("rock1", "/map/rock-sand1-424-216.png", true, new GameObjForPic("/map/rock-sand1-424-216.png", 0, 50, 425, 212))
//                .gen()
//                .setMap());
//
//        mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("changeMap.bmp", "changeMap.txt")
//                .setX(MAP_UNIT_WIDTH-1024)
//                .setNameAndPath("changetree2","/map/change-tree1(242-271).png",true,new GameObjForPic("/map/change-tree1(242-271).png", 0, 0, 242, 272))
//                .setNameAndPath("deserttree1","/map/desert-tree1(239-272).png",true,new GameObjForPic("/map/desert-tree1(239-272).png", 0, 0, 239, 272))
//                .setNameAndPath("signright","/map/signRightpng(152-104).png",true,new GameObjForPic("/map/signRightpng(152-104).png", 0, 0, 152, 104))
//                .setNameAndPath("sandbag1","/map/sandbag1(272-100).png",true,new GameObjForPic("/map/sandbag1(272-100).png", 0, 0, 272, 100))
//                .setNameAndPath("rock2", "/map/rock-sand1-584-216.png", true,new GameObjForPic("/map/rock-sand1-584-216.png", 0, -200, 584, 216))
//                .setNameAndPath("sandbag2","/map/sandbag2(288-80).png",true,new GameObjForPic("/map/sandbag2(288-80).png", 0, 0, 288, 80))
//                .setNameAndPath("rocko1","/map/rocko(208-136).png",true,new GameObjForPic("/map/rocko(208-136).png", 0, 0, 208, 136))
//                .setNameAndPath("rocko2","/map/rocko(208-120).png",true,new GameObjForPic("/map/rocko(208-120).png", 0, 0, 208, 120))
//                .gen()
//                .setMap());
//
//        mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("desertMap.bmp", "desertMap.txt")
//                .setX(MAP_UNIT_WIDTH*3-1024)
//                .setNameAndPath("deserttree1", "/map/deserttree1(400-344).png", true, new GameObjForPic("/map/deserttree1(400-344).png", 0, 0, 400, 344))
//                .setNameAndPath("deserttree2", "/map/deserttree2(400-344).png", true, new GameObjForPic("/map/deserttree2(400-344).png", 0, 0, 400, 344))
//                .setNameAndPath("deserttree3", "/map/deserttree3(192-240).png", true, new GameObjForPic("/map/deserttree3(192-240).png", 0, 0, 192, 240))
//                .setNameAndPath("deserttree4", "/map/deserttree4(192-240).png", true, new GameObjForPic("/map/deserttree4(192-240).png", 0, 0, 192, 240))
//                .setNameAndPath("tree1", "/map/desert-tree1(222-254).png", true, new GameObjForPic("/map/desert-tree1(222-254).png", 0, 0, 222, 254))
//                .gen()
//                .setMap());
//
//        mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("bossMap.bmp", "bossMap.txt")
//                .setX(MAP_UNIT_WIDTH*4-1024)
//                .setNameAndPath("farmhay","/map/farm_hay(184-144).png",true,new GameObjForPic("/map/farm_hay(184-144).png", 0, 0, 184, 144))
//                .setNameAndPath("farmhay2","/map/farmhay(72-160).png",true,new GameObjForPic("/map/farmhay(72-160).png", 0, 0, 72, 160))
//                .setNameAndPath("signright","/map/signRight(152-104).png",true,new GameObjForPic("/map/signRight(152-104).png", 0, -150, 152, 104))
//                .setNameAndPath("warmsign","/map/warmsign(240-160).png",true,new GameObjForPic("/map/warmsign(240-160).png", 0, -150, 240, 160))
//                .setNameAndPath("verticlawall1","/map/vertical_wall(24-216).png",true,new GameObjForPic("/map/vertical_wall(24-216).png", 0, 0, 32, 216))
//                .setNameAndPath("verticalwall2","/map/vertical_wall2(24-216).png",true,new GameObjForPic("/map/vertical_wall2(24-216).png", 0, 0, 32, 216))
//                .setNameAndPath("wall1","/map/horizontal_wall(176-96).png",true,new GameObjForPic("/map/horizontal_wall(176-96).png", 0, 0, 176, 96))
//                .setNameAndPath("wall2","/map/horizontal_wall(288-96).png",true,new GameObjForPic("/map/horizontal_wall(288-96).png", 0, 0, 288, 96))
//                .setNameAndPath("wall3","/map/horizontal_wall(304-96).png",true,new GameObjForPic("/map/horizontal_wall(304-96).png", 0, 0, 304, 96))
//                .setNameAndPath("oasis1","/map/oasis_tree (232-400).png",true,new GameObjForPic("/map/oasis_tree (232-400).png", 110, 0, 200, 400))
//                .setNameAndPath("oasis2","/map/oasis_tree2(232-400).png",true,new GameObjForPic("/map/oasis_tree2(232-400).png", 130, 0, 232, 400))
//                .gen()
//                .setMap());


    }

private class mapInfo {
    //之後會有切換圖片的行為，所以先開一個內部類
    private Image mapLeft;
    private Image mapMiddle;
    private Image mapRight;
    private Image mapFinal;
    private Image mapChange;
    private Image mapLimit;
    private final int mapWidth = 2048;
    private int count;
    private boolean passing4000X;
    private boolean firstStage;
    private boolean secondStage;
    private boolean thirdStage;
    private boolean finalStage;
    private boolean back;

    private mapInfo() {
//            mapLeft = ImageController.getInstance().tryGet(MapPath.BEGIN.mapPath);
//            mapMiddle = ImageController.getInstance().tryGet(MapPath.FOREST.mapPath);
//            mapChange = ImageController.getInstance().tryGet(MapPath.CHANGE.mapPath);
//            mapRight = ImageController.getInstance().tryGet(MapPath.DESERT.mapPath);
//            mapFinal = ImageController.getInstance().tryGet(MapPath.END.mapPath);
        mapLimit = ImageController.getInstance().tryGet(Global.MapPath.LIMIT.mapPath);
        this.count = 0;
        firstStage = true;
        secondStage = false;
        thirdStage = false;
        finalStage = false;
        back = false;
    }

    public void mapPaint(Graphics g) {
        g.drawImage(mapLeft, mapWidth * count, 0, null);
//            g.drawImage(mapMiddle, mapWidth * (count + 1), 0, null);
//            g.drawImage(mapChange,mapWidth*(count+2),0,null);
//            g.drawImage(mapRight, mapWidth * (count + 3), 0, null);
//            g.drawImage(mapFinal, mapWidth * (count + 4), 0, null);
        g.drawImage(mapLimit,0,0,null);
    }

    public void mapUpdate() {
//            if(actorX>MAP_UNIT_WIDTH*4){
//                camera = new Camera.Builder(WINDOW_WIDTH, WINDOW_HEIGHT)
//                        .setCameraMoveSpeed(2)
//                        .setChaseObj(gameActor, 20, 20)
//                        .setCameraStartLocation(-WINDOW_WIDTH / 2+MAP_UNIT_WIDTH, -WINDOW_HEIGHT / 2)
//                        .gen();
//                MAP_HEIGHT = 2048;
//            }else {
//                MAP_HEIGHT = 1024;
//            }
//            if (!passing4000X) {
//                if (actorX > 4000) {
//                    mapLeft = ImageController.getInstance().tryGet(MapPath.SECOND.mapPath);
//                    count++;
//                    passing4000X = true;
//                }
//            }
//            if(!back) {
//                if (actorX > 4000) {
//
//
//                }
//                return;
//            }
//            if(back){
//
//            }
    }


//monster.add(new BullBoss(1200,500));

//        monster.add(new SmallMonster(1000,500, SmallMonster.Type.GOBLIN));
//        monster.add(new Stone(1100,500));
//        monster.add(new Cockroach(1400,500));
//        monster.add(new SmallMonster(1000,500, SmallMonster.Type.MUSHROOM));
//        monster.add(new SmallMonster(1000,500,SmallMonster.Type.MUSHROOM));
//        monster.add(new Rino(200,500));
//        monster.add(new Rino(1100,500));
//        monster.add(new Rino(500,500));
 */
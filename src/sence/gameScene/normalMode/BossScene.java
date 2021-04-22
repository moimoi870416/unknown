package sence.gameScene.normalMode;

import camera.MapInformation;
import controller.ImageController;
import controller.MapObjController;
import object.GameObjForPic;
import object.actor.GameActor;
import object.monster.BullBoss;
import sence.GameScene;
import static util.Global.*;

import java.awt.*;

import static util.Global.MAP_HEIGHT;
import static util.Global.MAP_WIDTH;

public class BossScene extends GameScene {


    @Override
    protected void sceneBeginComponent() {
        MAP_HEIGHT = 2048;
        MAP_WIDTH = 2048;
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
        gameActor = new GameActor(Actor.FIRST.getPath(), 1024, 1900);
        mapInfo = new BossMapInfo();
    }

    @Override
    protected void sceneEndComponent() {
        MAP_HEIGHT = 2048;
        MAP_WIDTH = 1024;
    }

    public class BossMapInfo extends GameScene.MapInfo {
        private Image map;
        public BossMapInfo(){
            map = ImageController.getInstance().tryGet(MapPath.END.mapPath);
            mapBoss();
            monster.add(new BullBoss(1024,1024));
        }

        @Override
        public void mapPaint(Graphics g) {
            g.drawImage(map,0,0,null);
        }

        @Override
        public void mapUpdate() {


        }

        private void mapBoss(){
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("bossMap.bmp", "bossMap.txt")
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

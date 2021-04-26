package sence.gameScene.normalMode;

import camera.MapInformation;
import controller.ImageController;
import controller.MapObjController;
import object.GameObjForPic;
import object.actor.GameActor;
import object.monster.Rino;
import object.monster.SmallMonster;
import object.monster.Stone;
import sence.ConnectScene;
import sence.GameScene;
import static util.Global.*;
import java.awt.*;
import java.util.ArrayList;

import static util.Global.MAP_HEIGHT;
import static util.Global.MAP_WIDTH;

public class BossScene extends ConnectScene {

    public BossScene(){
        gameActorArr = new ArrayList<>();
        gameActorArr.add(new GameActor(Actor.FIRST,1024,1800));
    }

    public BossScene(ArrayList<GameActor> gameActorArr){
        bossScene = true;
        this.gameActorArr = gameActorArr;
        this.gameActorArr.get(0).offSetX(1024);
        this.gameActorArr.get(0).offSetY(1800);
    }

    @Override
    protected void gameSceneBegin() {
        MAP_WIDTH = 2048;
        MAP_HEIGHT = 2048;
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
        mapInfo = new BossMapInfo();
        if(isServer) {
            //monster.add(new BullBoss(1024,200));
            monster.add(new Stone(1024,800));
            monster.add(new Rino(1024,1024));
            monster.add(new SmallMonster(1024,200, SmallMonster.Type.GOBLIN));
        }
    }

    @Override
    protected void gameSceneEnd() {
        MAP_HEIGHT = 2048;
        MAP_WIDTH = 2048;

    }

    public class BossMapInfo extends GameScene.MapInfo {
        private Image map;
        public BossMapInfo(){
            map = ImageController.getInstance().tryGet(MapPath.END.mapPath);
            mapBoss();
            //monster.add(new BullBoss(1024,1024));
        }

        @Override
        public void mapPaint(Graphics g) {
            g.drawImage(map,0,0,null);
        }

        @Override
        public void mapUpdate() {
            if(monster.size()<= 0){
                effectView.setVictory(true);
            }
            System.out.println(monster.get(0).getState());

        }

        private void mapBoss(){
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("bossMap.bmp", "bossMap.txt")
                    .setNameAndPath("farmhay", "/pictures/map/farm_hay(184-144).png",true,new GameObjForPic("/pictures/map/farm_hay(184-144).png", 0, 0, 184, 144))
                    .setNameAndPath("farmhay2", "/pictures/map/farmhay(72-160).png",true,new GameObjForPic("/pictures/map/farmhay(72-160).png", 0, 0, 72, 160))
                    .setNameAndPath("signright", "/pictures/map/signRight(152-104).png",true,new GameObjForPic("/pictures/map/signRight(152-104).png", 0, -150, 152, 104))
                    .setNameAndPath("warmsign", "/pictures/map/warmsign(240-160).png",true,new GameObjForPic("/pictures/map/warmsign(240-160).png", 0, -150, 240, 160))
                    .setNameAndPath("verticlawall1", "/pictures/map/vertical_wall(24-216).png",true,new GameObjForPic("/pictures/map/vertical_wall(24-216).png", 0, 0, 32, 216))
                    .setNameAndPath("verticalwall2", "/pictures/map/vertical_wall2(24-216).png",true,new GameObjForPic("/pictures/map/vertical_wall2(24-216).png", 0, 0, 32, 216))
                    .setNameAndPath("wall1", "/pictures/map/horizontal_wall(176-96).png",true,new GameObjForPic("/pictures/map/horizontal_wall(176-96).png", 0, 0, 176, 96))
                    .setNameAndPath("wall2", "/pictures/map/horizontal_wall(288-96).png",true,new GameObjForPic("/pictures/map/horizontal_wall(288-96).png", 0, 0, 288, 96))
                    .setNameAndPath("wall3", "/pictures/map/horizontal_wall(304-96).png",true,new GameObjForPic("/pictures/map/horizontal_wall(304-96).png", 0, 0, 304, 96))
                    .setNameAndPath("oasis1", "/pictures/map/oasis_tree (232-400).png",true,new GameObjForPic("/pictures/map/oasis_tree (232-400).png", 110, 0, 200, 400))
                    .setNameAndPath("oasis2", "/pictures/map/oasis_tree2(232-400).png",true,new GameObjForPic("/pictures/map/oasis_tree2(232-400).png", 130, 0, 232, 400))
                    .gen()
                    .setMap());
        }
    }

}

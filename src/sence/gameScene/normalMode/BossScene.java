package sence.gameScene.normalMode;

import camera.MapInformation;
import controller.AudioResourceController;
import controller.ImageController;
import controller.MapObjController;
import object.GameObjForPic;
import object.actor.GameActor;
import object.monster.BullBoss;
import object.monster.Rino;
import object.monster.SmallMonster;
import object.monster.Stone;
import sence.ConnectScene;
import sence.GameScene;
import util.Delay;

import static util.Global.*;

import java.awt.*;
import java.util.ArrayList;
import static util.Global.MAP_HEIGHT;
import static util.Global.MAP_WIDTH;

public class BossScene extends ConnectScene {
    private Delay BGMDelay;

    public BossScene() {
        gameActorArr = new ArrayList<>();
        this.gameActorArr.add(new GameActor(Actor.FIRST, 1024, 1800));
    }

    public BossScene(ArrayList<GameActor> gameActorArr) {

        this.gameActorArr = gameActorArr;
        this.gameActorArr.get(0).offSetX(1024);
        this.gameActorArr.get(0).offSetY(1800);
    }

    @Override
    protected void gameSceneBegin() {
        bossScene = true;
        AudioResourceController.getInstance().loop("/sounds/bgm/warning-2.wav", 0);
        BGMDelay = new Delay(300);
        BGMDelay.play();
        MAP_WIDTH = 2048;
        MAP_HEIGHT = 2048;
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
        mapInfo = new BossMapInfo();

        if (isServer || isSingle) {
            monster.add(new BullBoss(1024, 500));
        }
    }

    @Override
    protected void gameSceneEnd() {
        MAP_HEIGHT = 2048;
        MAP_WIDTH = 2048;

    }

    public class BossMapInfo extends GameScene.MapInfo {
        private Image map;
        private int x;
        private int y;
        private boolean xOK;
        private boolean yOK;
        private boolean start;

        public BossMapInfo() {
            map = ImageController.getInstance().tryGet(MapPath.END.mapPath);
            mapBoss();
            x = 0;
            y = 0;
            xOK = false;
            yOK = false;
            start = false;
            //monster.add(new BullBoss(1024,1024));

        }

        @Override
        public void mapPaint(Graphics g) {
            g.drawImage(
                    map,
                    0,
                    0,
//                    2048,
//                    2048,
//                    camera.painter().left(),
//                    camera.painter().top(),
//                    camera.painter().right(),
//                    camera.painter().bottom(),
                    null);
        }

        @Override
        public void mapUpdate() {
            if(BGMDelay.count()){
                AudioResourceController.getInstance().stop("/sounds/bgm/warning-2.wav");
                AudioResourceController.getInstance().loop("/sounds/bgm/BGM-BOSS.wav", 0);
                start = true;
            }
            if (monster.size() == 0) {
                return;
            }

            if (monster.size() == stoneCount && stoneCount == stoneDead) {
                effectView.setVictory(true);
            }
            if ((isServer || isSingle) && !bossDead && start) {
                xOK = false;
                yOK = false;
                for (int i = 0; i < gameActorArr.size(); i++) {
                    x = random(550,1750);
                    y = random(320,1950);
                    if (x < gameActorArr.get(i).collider().centerX() + WINDOW_WIDTH / 2 + 10 && x > gameActorArr.get(i).collider().centerX() - WINDOW_WIDTH / 2 - 10) {
                        return;
                    }
                    xOK = true;
                    if (y < gameActorArr.get(i).collider().centerX() + WINDOW_HEIGHT + 10 && y > gameActorArr.get(i).collider().centerY() - WINDOW_HEIGHT / 2 - 10) {
                        return;
                    }
                    yOK = true;
                }

                if ((xOK && yOK) && monster.size() < gameActorArr.size()*12 + stoneDead && Math.random()*100<20) {
                    int r = random(0, 10);
                    if (r >=0 && r <4) {
                        monster.add(new SmallMonster(x, y, SmallMonster.Type.MUSHROOM));
                    }
                    if (r >=4 && r <6) {
                        monster.add(new SmallMonster(x, y, SmallMonster.Type.GOBLIN));
                    }
                    if (r >=6 && r <8) {
                        monster.add(new Stone(x, y));
                    }
                    if (r >=9 && r<11 ) {
                        monster.add(new Rino(x, y));
                    }

                    monster.getLast().setIsChase(true);
                }
            }

        }

        private void mapBoss() {
            mapObjArr.addAll(new MapObjController.Builder().setBmpAndTxt("bossMap.bmp", "bossMap.txt")
                    .setNameAndPath("farmhay", "/pictures/map/farm_hay(184-144).png", true, new GameObjForPic("/pictures/map/farm_hay(184-144).png", 0, 0, 184, 144))
                    .setNameAndPath("farmhay2", "/pictures/map/farmhay(72-160).png", true, new GameObjForPic("/pictures/map/farmhay(72-160).png", 0, 0, 72, 160))
                    .setNameAndPath("signright", "/pictures/map/signRight(152-104).png", true, new GameObjForPic("/pictures/map/signRight(152-104).png", 0, -150, 152, 104))
                    .setNameAndPath("warmsign", "/pictures/map/warmsign(240-160).png", true, new GameObjForPic("/pictures/map/warmsign(240-160).png", 0, -150, 240, 160))
                    .setNameAndPath("verticlawall1", "/pictures/map/vertical_wall(24-216).png", true, new GameObjForPic("/pictures/map/vertical_wall(24-216).png", 0, 0, 32, 216))
                    .setNameAndPath("verticalwall2", "/pictures/map/vertical_wall2(24-216).png", true, new GameObjForPic("/pictures/map/vertical_wall2(24-216).png", 0, 0, 32, 216))
                    .setNameAndPath("wall1", "/pictures/map/horizontal_wall(176-96).png", true, new GameObjForPic("/pictures/map/horizontal_wall(176-96).png", 0, 0, 176, 96))
                    .setNameAndPath("wall2", "/pictures/map/horizontal_wall(288-96).png", true, new GameObjForPic("/pictures/map/horizontal_wall(288-96).png", 0, 0, 288, 96))
                    .setNameAndPath("wall3", "/pictures/map/horizontal_wall(304-96).png", true, new GameObjForPic("/pictures/map/horizontal_wall(304-96).png", 0, 0, 304, 96))
                    .setNameAndPath("oasis1", "/pictures/map/oasis_tree (232-400).png", true, new GameObjForPic("/pictures/map/oasis_tree (232-400).png", 110, 0, 200, 400))
                    .setNameAndPath("oasis2", "/pictures/map/oasis_tree2(232-400).png", true, new GameObjForPic("/pictures/map/oasis_tree2(232-400).png", 130, 0, 232, 400))
                    .gen()
                    .setMap());
        }
    }

}

package sence.gameScene;

import camera.MapInformation;
import controller.ImageController;
import object.actor.GameActor;
import sence.ConnectScene;
import sence.GameScene;
import static util.Global.*;
import java.awt.*;
import static util.Global.MAP_HEIGHT;
import static util.Global.MAP_WIDTH;

public class LimitMode extends ConnectScene {


    @Override
    protected void gameSceneBegin() {
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT);
//        gameActor = new GameActor(Actor.FIRST.getPath(), 500, 700);
        mapInfo = new LimitModeMapInfo();
    }

    @Override
    protected void gameSceneEnd() {

    }

    public class LimitModeMapInfo extends GameScene.MapInfo{
        private Image map;
        public LimitModeMapInfo(){
            map = ImageController.getInstance().tryGet(MapPath.LIMIT.mapPath);
        }

        @Override
        public void mapPaint(Graphics g) {
            g.drawImage(map,0,0,null);
        }

        @Override
        public void mapUpdate() {

        }


    }
}

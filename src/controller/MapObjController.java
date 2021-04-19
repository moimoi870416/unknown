package controller;

import maploader.MapInfo;
import maploader.MapLoader;
import object.GameObjForPic;
import object.GameObject;
import sence.Scene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * this class only read a picture without animator and collision for object on map.
 * add can input a object from outside and adjust collision,but maybe exist bug.
 * if need to adjust collision, kindly create a new class and input size after adjust not X-Y.
 */
public class MapObjController {
    private static MapObjController mapObjController;
    private ArrayList<GameObject> mapObjArr;
    private String bmp;
    private String txt;
    private int x;
    private int y;
    private int objSize;
    private ArrayList<KeyPair> keyPairs;

    private MapObjController(){
    }

    public static MapObjController getInstance() {//推遲到第一次呼叫getInstance()才創建實體
        if (mapObjController == null) {
            mapObjController = new MapObjController();
        }
        return mapObjController;
    }

    private MapObjController setLoadMap(String bmp, String txt,int x,int y, int size, ArrayList<KeyPair> keyPairs){
        this.bmp = bmp;
        this.txt = txt;
        this.x = x;
        this.y = y;
        this.objSize = size;
        this.keyPairs = keyPairs;
        mapObjArr = new ArrayList<>();
        return this;
    }

    public ArrayList<GameObject> setMap() {
        try {
            //在場景開始時使用創建MapLoader物件
            final MapLoader mapLoader = new MapLoader(this.bmp, this.txt);
            final ArrayList<MapInfo> mapInfos = mapLoader.combineInfo();//使用MapLoader中的combineInfo()方法產生MapInfo陣列
            for(int i=0 ; i<keyPairs.size() ; i++) {
                int k=i;
                this.mapObjArr.addAll(mapLoader.createObjectArray(keyPairs.get(i).objNameInTxt, this.objSize, mapInfos, new MapLoader.CompareClass() {
                    @Override
                    public GameObject compareClassName(final String gameObject, final String name, final maploader.MapInfo mapInfo, final int size) {
                        //進行name的比照並產生對應地圖物件
                        GameObject tmp;
                        if (gameObject.equals(name) && keyPairs.get(k).gameObject == null && !keyPairs.get(k).adjustCollision) {
                            tmp = new GameObjForPic(keyPairs.get(k).picPath,mapInfo.getX() * size+x, mapInfo.getY() * size+y, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                            return tmp;
                        }if (gameObject.equals(name) && keyPairs.get(k).gameObject == null && keyPairs.get(k).adjustCollision) {
                            tmp = new GameObjForPic(keyPairs.get(k).picPath,mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                            tmp.collider().setLeft(mapInfo.getX() * size +x + keyPairs.get(k).gameObject.collider().left());
                            tmp.collider().setTop(mapInfo.getY() * size +y+ keyPairs.get(k).gameObject.collider().top());
                            tmp.collider().setRight(mapInfo.getX() * size +x+ keyPairs.get(k).gameObject.collider().width());
                            tmp.collider().setBottom(mapInfo.getY() * size +y+ keyPairs.get(k).gameObject.collider().height());
                            tmp.painter().setLeft(mapInfo.getX() * size +x);
                            tmp.painter().setTop(mapInfo.getY() * size +y);
                            return tmp;
                        }
                        if(gameObject.equals(name) && keyPairs.get(k).gameObject != null && keyPairs.get(k).adjustCollision){
                            tmp = new GameObjForPic(keyPairs.get(k).picPath, mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size);
                            tmp.collider().setLeft(mapInfo.getX() * size +x + keyPairs.get(k).gameObject.collider().left());
                            tmp.collider().setTop(mapInfo.getY() * size +y+ keyPairs.get(k).gameObject.collider().top());
                            tmp.collider().setRight(mapInfo.getX() * size +x+ keyPairs.get(k).gameObject.collider().width());
                            tmp.collider().setBottom(mapInfo.getY() * size +y+ keyPairs.get(k).gameObject.collider().height());
                            tmp.painter().setLeft(mapInfo.getX() * size +x);
                            tmp.painter().setTop(mapInfo.getY() * size +y);
                            return tmp;
                        }
                        if(gameObject.equals(name) && keyPairs.get(k).gameObject != null && !keyPairs.get(k).adjustCollision){
                            tmp = keyPairs.get(k).gameObject;
                            tmp.painter().setLeft(mapInfo.getX() * size+x);
                            tmp.painter().setTop(mapInfo.getY() * size+y);
                            tmp.painter().setRight(mapInfo.getSizeX() * size);
                            tmp.painter().setBottom(mapInfo.getSizeY() * size);
                            tmp.collider().setLeft(mapInfo.getX() * size+x);
                            tmp.collider().setTop(mapInfo.getY() * size+y);
                            tmp.collider().setRight(mapInfo.getSizeX() * size);
                            tmp.collider().setBottom(mapInfo.getSizeY() * size);
                            return tmp;
                        }
                        return null;
                    }
                }));
            }
        } catch (final IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapObjArr;
    }

    public static class Builder {
        private String bmp;
        private String txt;
        private int x;
        private int y;
        private int objSize;
        private ArrayList<KeyPair> keyPairs;

        public Builder() {
            this.objSize = 32;
        }

        public Builder setBmpAndTxt(String bmp, String txt){
            keyPairs = new ArrayList<>();
            this.bmp = bmp;
            this.txt = txt;
            this.x = 0;
            this.y = 0;
            return this;
        }

        public Builder setX(int x){
            this.x = x;
            return this;
        }

        public Builder setY(int y){
            this.y = y;
            return this;
        }

        public Builder setSize(int size){
            this.objSize = size;
            return this;
        }

        public Builder setNameAndPath(String objNameInTxt, String picPath,boolean isAdjustCollision,GameObject gameObject){
            keyPairs.add(new KeyPair(objNameInTxt,picPath,isAdjustCollision,gameObject));
            return this;
        }

        public MapObjController gen(){
            return MapObjController.getInstance().setLoadMap(bmp,txt,x,y,objSize,keyPairs);
        }

    }
    private static class KeyPair {//內部類別
        private String objNameInTxt;//路徑
        private String picPath;//圖片
        private GameObject gameObject;
        private boolean adjustCollision;

        public KeyPair(final String objName, final String picPath,boolean collision,GameObject gameObject) {
            this.objNameInTxt = objName;
            this.picPath = picPath;
            this.gameObject = gameObject;
            this.adjustCollision = collision;
        }
    }

}


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
 * this class only read a picture without animator for object on map.
 */
public class MapObjController {
    private static MapObjController mapObjController;
    private ArrayList<GameObject> mapObjArr;
    private String bmp;
    private String txt;
    private int objSize;
    private ArrayList<KeyPair> keyPairs;

    private MapObjController(){
    }
/*
    private LoadMap(String bmp,String txt,int size,ArrayList<KeyPair> keyPairs){
        this.bmp = bmp;
        this.txt = txt;
        this.objSize = size;
        this.keyPairs = keyPairs;
    }
*/
    private static MapObjController getInstance() {//推遲到第一次呼叫getInstance()才創建實體
        if (mapObjController == null) {
            mapObjController = new MapObjController();
        }
        return mapObjController;
    }

    private MapObjController setLoadMap(String bmp, String txt, int size, ArrayList<KeyPair> keyPairs){
        this.bmp = bmp;
        this.txt = txt;
        this.objSize = size;
        this.keyPairs = keyPairs;
        return this;
    }

    public ArrayList<GameObject> setMap() {
        try {
            //在場景開始時使用創建MapLoader物件
            final MapLoader mapLoader = new MapLoader(this.bmp, this.txt);
            final ArrayList<MapInfo> mapInfos = mapLoader.combineInfo();//使用MapLoader中的combineInfo()方法產生MapInfo陣列
            for(int i=0 ; i<keyPairs.size() ; i++) {
                int k=i;
                this.mapObjArr = mapLoader.createObjectArray(keyPairs.get(i).objNameInTxt, this.objSize, mapInfos, new MapLoader.CompareClass() {
                    @Override
                    public GameObject compareClassName(final String gameObject, final String name, final maploader.MapInfo mapInfo, final int size) {
                        //進行name的比照並產生對應地圖物件
                        GameObject tmp = null;
                        if (gameObject.equals(name)) {
                            tmp = new GameObjForPic(keyPairs.get(k).picPath,mapInfo.getX() * size, mapInfo.getY() * size, mapInfo.getSizeX() * size, mapInfo.getSizeY() * size) {
                            };
                            return tmp;
                        }
                        return null;
                    }
                });
            }
        } catch (final IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mapObjArr;
    }

    public static class Builder {
        private String bmp;
        private String txt;
        private int objSize;
        private ArrayList<KeyPair> keyPairs;

        public Builder() {
            this.objSize = 32;
        }

        public Builder setBmpAndTxt(String bmp, String txt){
            keyPairs = new ArrayList<>();
            this.bmp = bmp;
            this.txt = txt;
            return this;
        }

        public Builder setSize(int size){
            this.objSize = size;
            return this;
        }

        public Builder setNameAndPath(String objNameInTxt, String picPath){
            keyPairs.add(new KeyPair(objNameInTxt,picPath));
            return this;
        }

        public MapObjController gen(){
            return MapObjController.getInstance().setLoadMap(bmp,txt,objSize,keyPairs);
        }

    }
    private static class KeyPair {//內部類別
        private String objNameInTxt;//路徑
        private String picPath;//圖片

        public KeyPair(final String objName, final String picPath) {
            this.objNameInTxt = objName;
            this.picPath = picPath;
        }
    }


}


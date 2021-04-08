package camera;

import object.Rect;

public class Mapinformation {
    // 記得要在地圖那邊做地圖資訊的設定唷～

    private static Rect mapInfo;

    public static Rect mapInfo() {
        return mapInfo;
    }

    public static void setMapInfo(int left, int top, int right, int bottom) {
        mapInfo = new Rect(left, top, right, bottom);
    }

}

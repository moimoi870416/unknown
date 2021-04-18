package object;

import controller.ImageController;

import java.awt.*;

public class GameObjForPic extends GameObject{
    private Image img;

    public GameObjForPic(String path,int x, int y, int width, int height) {
        this(path,x, y, width, height,x,y,width,height);
    }

    public GameObjForPic(String path,int x, int y, int width, int height,int x2, int y2, int width2, int height2) {
        super(x, y, width, height,x2,y2,width2,height2);
        img = ImageController.getInstance().tryGet(path);
    }

    @Override
    public CollisionDir isCollider(GameObject obj) {
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img,painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}

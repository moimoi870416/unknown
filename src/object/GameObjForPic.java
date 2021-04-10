package object;

import controller.ImageController;

import java.awt.*;

public class GameObjForPic extends GameObject{
    private Image img;

    public GameObjForPic(String path,int x, int y, int width, int height) {
        super(x, y, width, height);
        img = ImageController.getInstance().tryGet(path);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img,painter().left(), painter().top(), null);
    }

    @Override
    public void update() {

    }
}

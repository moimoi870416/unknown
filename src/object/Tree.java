package object;

import controller.ImageController;
import objectdata.GameObject;
import java.awt.*;

public class Tree extends GameObject {
    private Image img;

    public Tree(int x, int y, int width, int height) {
        super(x, y, width, height);
        img = ImageController.getInstance().tryGet("/tree1.png");
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, painter().centerX(), painter().centerY(), null);
    }

    @Override
    public void update() {

    }
}

package object;

import controller.ImageController;

import java.awt.*;

public class Tree extends GameObject{
    private Image img;

    public Tree(int x, int y, int width, int height) {
        super(x, y, width, height);
        img = ImageController.getInstance().tryGet("/tree1.png");
    }

    @Override
    public boolean isOut() {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img,left(),top(),null);
    }

    @Override
    public void update() {

    }
}

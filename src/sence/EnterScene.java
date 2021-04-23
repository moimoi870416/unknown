package sence;

import menu.Style;
import util.CommandSolver;

import java.awt.*;

public class EnterScene extends Scene {
    private Style playStyle1Light;//人物的
    private Style playStyle2Drank;//人物的
    private Style playStyle2Light;//人物的
    private Style playStyle3Light;//人物的
    private Style playStyle3Drank;//人物的


    //設定圖片
    public Style seStyle(int width, int height, String path) {
        return new Style.StyleRect(width, height, path);
    }

    @Override
    public void sceneBegin() {

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void update() {

    }
}

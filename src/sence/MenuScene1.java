package sence;

import controller.ImageController;
import controller.SenceController;
import menu.*;
import util.CommandSolver;
import java.awt.*;
import static util.Global.*;

public class MenuScene1 extends Scene {
    private BackgroundType.BackgroundImage menuImg;

    @Override
    public void sceneBegin() {
        menuImg = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/menu-2.png"));
        initTheme();
    }

    @Override
    public void sceneEnd() {
    }

    @Override
    public void update() {
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return (e, state, trigTime) -> {
            if (state != null) {
                switch (state) {
                    case PRESSED -> SenceController.getSenceController().change(new MenuScene2());
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        menuImg.paintBackground(g, false, true, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void initTheme() {



        Theme.add(setTheme(400,800,"/menu/button-3.png"));
        Theme.add(setTheme(400,800,"/menu/button-4.png"));
        Theme.add(setTheme(400,800,"/menu/button-1.png"));
        Theme.add(setTheme(400,800,"/menu/button-2.png"));
        Theme.add(setTheme(100,50,"/menu/button-0.png"));
    }

    private Theme setTheme(int width,int height,String path){
        return new Theme(Style.getGeneralStyle(width,height,path, true,Color.WHITE,5)
                ,Style.getGeneralStyle(width,height,path,true,new Color(255,215,0),5)
                ,Style.getGeneralStyle(width,height,path,true,Color.BLACK,5));
    }

}


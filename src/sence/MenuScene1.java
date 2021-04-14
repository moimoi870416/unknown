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

    private static void initTheme() {
        Style simple = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-3.png")))
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(5);
        Style aa = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-3.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);
        Style im = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-3.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);
        Theme.add(new Theme(im, simple, aa));
        Style simple2 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-4.png")))
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(5);
        Style aa2 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-4.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);
        Style im2 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-4.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);
        Theme.add(new Theme(im2, simple2, aa2));
        Style simple3 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-1.png")))
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(5);
        Style aa3 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-1.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);
        Style im3 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-1.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);
        Theme.add(new Theme(im3, simple3, aa3));
        Style simple4 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-2.png")))
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(5);
        Style aa4 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-2.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);
        Style im4 = new Style.StyleRect(400, 800, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-2.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);
        Theme.add(new Theme(im4, simple4, aa4));
        Style simple5 = new Style.StyleRect(100, 50, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-0.png")))
                .setHaveBorder(true)
                .setBorderColor(new Color(255, 215, 0))
                .setBorderThickness(5);
        Style aa5 = new Style.StyleRect(100, 50, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-0.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(5);
        Style im5 = new Style.StyleRect(100, 50, new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu/button-0.png")))
                .setHaveBorder(true)
                .setBorderColor(Color.WHITE)
                .setBorderThickness(5);
        Theme.add(new Theme(im5, simple5, aa5));
    }
}


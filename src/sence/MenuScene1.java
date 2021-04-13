package sence;

import camera.Camera;
import controller.ImageController;
import controller.SenceController;
import menu.*;
import menu.Button;
import menu.Label;
import unit.CommandSolver;

import java.awt.*;
import java.awt.event.MouseEvent;

import menu.impl.MouseTriggerImpl;
import unit.Global;

import static unit.Global.*;

public class MenuScene1 extends Scene {

    private BackgroundType.BackgroundImage menuImg;


    @Override
    public void sceneBegin() {
        menuImg = new BackgroundType.BackgroundImage(ImageController.getInstance().tryGet("/menu-4.png"));
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
            if(state == CommandSolver.MouseState.CLICKED || state == CommandSolver.MouseState.RELEASED || state == CommandSolver.MouseState.MOVED){
                SenceController.getSenceController().change(new MenuScene2());
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
}

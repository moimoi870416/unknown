package controller;

import sence.Scene;
import unit.CommandSolver;
import unit.GameKernel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SenceController implements GameKernel.PaintInterface, GameKernel.UpdateInterface, CommandSolver.MouseListener, CommandSolver.KeyListener {
    private static SenceController senceController;
    private static Scene currentScene;

    private SenceController() {
    }

    public static SenceController getSenceController() {
        if (senceController == null) {
            senceController = new SenceController();
        }
        return senceController;
    }

    public void change(final Scene scene) {
        if (this.currentScene != null) {
            this.currentScene.sceneEnd();
        }
        if (scene != null) {
            scene.sceneBegin();
        }
        this.currentScene = scene;
        this.currentScene.setSc(this);
    }

    @Override
    public void paint(final Graphics g) {
        if (this.currentScene != null) {
            this.currentScene.paint(g);
        }
    }

    @Override
    public void update() {
        if (this.currentScene != null) {
            this.currentScene.update();
        }
    }

    @Override
    public void keyPressed(final int commandCode, final long trigTime) {
        if (this.currentScene != null && this.currentScene.keyListener() != null) {
            this.currentScene.keyListener().keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(final int commandCode, final long trigTime) {
        if (this.currentScene != null && this.currentScene.keyListener() != null) {
            this.currentScene.keyListener().keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void mouseTrig(final MouseEvent e, final CommandSolver.MouseState state, final long trigTime) {
        if (this.currentScene != null && this.currentScene.mouseListener() != null) {
            this.currentScene.mouseListener().mouseTrig(e, state, trigTime);
        }
    }

    @Override
    public void keyTyped(final char c, final long trigTime) {
        currentScene.keyListener().keyTyped(c, trigTime);
    }
}

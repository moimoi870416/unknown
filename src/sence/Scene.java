package sence;

import controller.SenceController;
import unit.CommandSolver;
import unit.GameKernel;

public abstract class Scene implements GameKernel.PaintInterface, GameKernel.UpdateInterface {
    protected SenceController sc;

    public abstract void sceneBegin();

    public abstract void sceneEnd();

    public abstract CommandSolver.KeyListener keyListener();

    public abstract CommandSolver.MouseListener mouseListener();

    public void setSc(final SenceController sc) {
        this.sc = sc;
    }

}

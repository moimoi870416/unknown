package sence;

import controller.SenceController;
import util.CommandSolver;
import util.GameKernel;

public abstract class Scene implements GameKernel.PaintInterface, GameKernel.UpdateInterface {

    public abstract void sceneBegin();

    public abstract void sceneEnd();

    public abstract CommandSolver.KeyListener keyListener();

    public abstract CommandSolver.MouseListener mouseListener();

}

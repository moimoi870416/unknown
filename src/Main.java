import unit.Global;
import controller.SenceController;
import sence.MapScene;
import unit.CommandSolver;
import unit.GameKernel;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(final String[] args) {

        final JFrame jFrame = new JFrame();//創建一個視窗
        new Global.Builder().setScreenX(1440)
                .setFullScreen(false)
                .setScreenY(900)
                .gen();
        SenceController.getSenceController().change(new MapScene());
        final SenceController sc = SenceController.getSenceController();//創建場景實體,並賦予行為
        final GameKernel gk = new GameKernel.Builder()
                .input(new CommandSolver.BuildStream().mouseTrack().subscribe(sc).keyboardTrack()
                        .add(KeyEvent.VK_W, Global.Direction.UP.ordinal())
                        .add(KeyEvent.VK_S, Global.Direction.DOWN.ordinal())
                        .add(KeyEvent.VK_D, Global.Direction.RIGHT.ordinal())
                        .add(KeyEvent.VK_A, Global.Direction.LEFT.ordinal())
                        .add(KeyEvent.VK_R,30)
                        .add(KeyEvent.VK_1,-1)
                        .add(KeyEvent.VK_2,-2)
                        .add(KeyEvent.VK_ENTER, 0)
                        .add(KeyEvent.VK_SPACE, 1)
                        .next().keyCleanMode()
                        .subscribe(sc))
                //鍵盤監聽
                //遊戲的主程式
                .paint(sc)
                .fps(60)
                .ups(60)
                .update(sc)
                .gen();

        jFrame.setTitle("Game 8th");//視窗標題
        jFrame.setSize(Global.getScreenX(), Global.getScreenY());//視窗大小
        jFrame.setLocationRelativeTo(null);//視窗置中
        jFrame.setResizable(false);//固定視窗大小
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//按X是否結束程式
        jFrame.add(gk);//加入主程式
        jFrame.setVisible(true);//顯示視窗
        gk.run();//執行主程式
    }

}

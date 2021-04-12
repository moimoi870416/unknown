import controller.ImageController;
import unit.Global;
import controller.SenceController;
import sence.MapScene;
import unit.CommandSolver;
import unit.GameKernel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(final String[] args) {

        final JFrame jFrame = new JFrame();//創建一個視窗
        
        new Global.Builder().setScreenX(1440)
                .setFullScreen(false)
                .setScreenY(900)
                .gen();
        Image mouse1 = ImageController.getInstance().tryGet("/targeter.png");
        SenceController.getSenceController().change(new MapScene());
        final SenceController sc = SenceController.getSenceController();//創建場景實體,並賦予行為
        final GameKernel gk = new GameKernel.Builder()
                .input(new CommandSolver.BuildStream().mouseTrack().subscribe(sc).keyboardTrack()
                        .add(KeyEvent.VK_W, Global.Direction.UP.ordinal())
                        .add(KeyEvent.VK_S, Global.Direction.DOWN.ordinal())
                        .add(KeyEvent.VK_D, Global.Direction.RIGHT.ordinal())
                        .add(KeyEvent.VK_A, Global.Direction.LEFT.ordinal())
                        .add(KeyEvent.VK_R, Global.Active.RELOADING.getCommandCode())
                        .add(KeyEvent.VK_1,Global.Active.NUMBER_ONE.getCommandCode())
                        .add(KeyEvent.VK_2,Global.Active.NUMBER_TWO.getCommandCode())
                        .add(KeyEvent.VK_3,Global.Active.NUMBER_THREE.getCommandCode())
                        .add(KeyEvent.VK_4,Global.Active.NUMBER_FORE.getCommandCode())
                        .add(KeyEvent.VK_SPACE, Global.Active.SPACE.getCommandCode())
                        .add(KeyEvent.VK_F, Global.Active.FLASH.getCommandCode())
                        .add(KeyEvent.VK_E, Global.Active.CATCH_ITEM.getCommandCode())
                        .next().keyCleanMode()
                        .subscribe(sc))
                //鍵盤監聽
                //遊戲的主程式
                .paint(sc)
                .fps(60)
                .ups(60)
                .update(sc)
                .gen();

        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(mouse1, new Point(10, 10), "mouse1");
        jFrame.setCursor(cursor);
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

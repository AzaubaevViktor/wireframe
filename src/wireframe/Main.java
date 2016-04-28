package wireframe;

import wireframe.view.MainFrame;
import wireframe.view.MainFrameWin;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());

        Model model = new Model();

        MainFrame mainFrameWin = new MainFrameWin(800, 600, "Штука", model);
    }
}

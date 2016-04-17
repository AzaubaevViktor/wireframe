package wireframe.view;

import java.awt.event.KeyEvent;

public class MainFrameWin extends MainFrame {
    private final BSplineWin bsplineWin = new BSplineWin();

    public MainFrameWin(int x, int y, String title) {
        super(x, y, title);

        try {
            createAllMenus();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    private void createAllMenus() throws NoSuchMethodException {
        addSubMenu("Menu", KeyEvent.VK_M);
        addMenuItem("Menu/wireframe.BSpline", "Lol", KeyEvent.VK_B, "openBSplineWin");
    }

    public void openBSplineWin() {
        bsplineWin.setVisible(true);
    }
}

package wireframe.view;

import wireframe.Model;

import java.awt.event.KeyEvent;

public class MainFrameWin extends MainFrame {
    private final BSplineWin bsplineWin;
    private final AboutWin about;
    private final Model model;

    public MainFrameWin(int x, int y, String title, Model model) {
        super(x, y, title);

        about = new AboutWin();
        bsplineWin = new BSplineWin(model);
        this.model = model;

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
        addSubMenu("About", KeyEvent.VK_A);
        addMenuItem("About/About", "Azaz", KeyEvent.VK_A, "showAbout");
    }

    public void openBSplineWin() {
        bsplineWin.setVisible(true);
    }

    public void showAbout() { about.setVisible(true); }
}

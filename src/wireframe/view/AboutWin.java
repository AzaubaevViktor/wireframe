package wireframe.view;

import javax.swing.*;
import java.awt.*;

public class AboutWin extends JDialog {
    AboutWin() {
        setModal(true);
        setTitle("О Программе");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(400, 100);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Isoline |"));
        panel.add(new JLabel("FIT NSU, Korovin 13204 @2016"));
        panel.add(new JLabel("Матричные операции в модуле matrix"));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Реализация BSpline в файле BSpline.java"));
        setContentPane(panel);
    }
}

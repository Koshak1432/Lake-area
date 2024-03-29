package kosh.display;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ImageWindow {
    public ImageWindow(BufferedImage bufImage) {
        this.image = new ImageIcon(bufImage);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displayImage(String title) {
        JScrollPane pane = new JScrollPane(new JLabel(image), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle(title);
    }

    private final ImageIcon image;
    private final JFrame frame = new JFrame();
}

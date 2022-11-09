package kosh;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageWindow {
    public ImageWindow(BufferedImage bufImage) {
        this.image = new ImageIcon(bufImage);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displayImage() {
        JScrollPane pane = new JScrollPane(new JLabel(image), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
    }

    private final ImageIcon image;
    private final JFrame frame = new JFrame();
}

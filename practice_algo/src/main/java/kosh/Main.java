package kosh;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File file = new File("geoImages/20APR08082807-M2AS-013340352010_01_P001.TIF");
        BufferedImage bufImage = null;
        try {
            bufImage = ImageIO.read(file);
            System.out.println(bufImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bufImage != null) {
            ImageWindow imageWindow = new ImageWindow(bufImage);
            imageWindow.displayImage();
        }
    }
}
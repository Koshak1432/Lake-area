package kosh;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File file = new File("geoImages/20APR08082807-M2AS-013340352010_01_P001.TIF");
//        BufferedImage bufImage = null;
//        try {
//            bufImage = ImageIO.read(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (bufImage != null) {
//            System.out.println(bufImage);
//            ImageWindow imageWindow = new ImageWindow(bufImage);
//            imageWindow.displayImage();
//        }
        GdalFormater formater = new GdalFormater();
        boolean[] activeBands = new boolean[]{true, false, false, true, false, false, true, false};
//        boolean[] activeBands = new boolean[]{true, true, true};
        if (!formater.loadHeader(file)) {
            System.err.println("Couldn't load header");
            return;
        }
        Data data = formater.loadData(activeBands);

        ImageConstructor constructor = new ImageConstructor(data.getDataPoints()[0], data.getDataPoints()[2], data.getDataPoints()[1],
                                                            data.getWidth(), data.getHeight());
        BufferedImage img = constructor.constructImage();
        if (img != null) {
            System.out.println(img);
            ImageWindow imageWindow = new ImageWindow(img);
            imageWindow.displayImage();
        }
    }
}
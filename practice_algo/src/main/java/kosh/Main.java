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
        formater.loadHeader(file);
        Data data = formater.loadData(activeBands);
    }
}
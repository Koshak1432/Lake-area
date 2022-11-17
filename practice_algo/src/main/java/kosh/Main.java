package kosh;

import kosh.Kmeans.KMeans;

import java.awt.image.BufferedImage;
import java.io.File;

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
//        boolean[] activeBands = new boolean[]{true, false, false, true, false, false, true, false};
        boolean[] activeBands = null;
        if (!formater.loadHeader(file)) {
            System.err.println("Couldn't load header");
            return;
        }
        Data data = formater.loadData(activeBands);
        int k = 5;

        KMeans algo = new KMeans(data.getDataPoints(), k);

        int[] assignments = algo.kmeans();
        BandsAsRGB distribution = new BandsAsRGB(1, 3, 8);


        ImageConstructor constructor = new ImageConstructor(data.getDataPoints()[1], data.getDataPoints()[0], data.getDataPoints()[2],
                                                            data.getWidth(), data.getHeight());
        BufferedImage img = constructor.constructImage();
        if (img != null) {
            System.out.println(img);
            ImageWindow imageWindow = new ImageWindow(img);
            imageWindow.displayImage();
        }


    }
}
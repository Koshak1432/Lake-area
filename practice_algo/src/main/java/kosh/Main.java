package kosh;

import kosh.Kmeans.Cluster;
import kosh.Kmeans.KMeans;
import org.gdal.gdal.Band;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(USAGE);
            return;
        }
        ParsedArgs parsed = ArgsParser.parseArgs(args);
        if (parsed == null) {
            System.err.println(USAGE);
            return;
        }
        if (parsed.fileName() == null) {
            System.err.println("Couldn't get file name");
            return;
        }

        File file = new File(parsed.fileName());
        int k = parsed.k();
        System.out.println(parsed.fileName() + ", " + parsed.k());

        GdalFormater formater = new GdalFormater();
        if (!formater.loadHeader(file)) {
            System.err.println("Couldn't load header");
            return;
        }

        // какие грузить
        boolean[] activeBands = new boolean[formater.getBandsNumber()];
        int activeNum;
        BandsAsRGB colorsDistribution;
        System.out.println("Available bands: " + Arrays.toString(formater.getBandsDescription()));
        System.out.println("Select bands to load:");
        try (Scanner scanner = new Scanner(System.in)) {
            activeNum = ArgsParser.parseBandsToLoad(activeBands, scanner);

            System.out.println("Available bands to show: " + Arrays.toString(
                    ArgsParser.getAvailableBandsToShow(activeBands, activeNum)));
            System.out.println("Select bands to show(R G B)");
            colorsDistribution = ArgsParser.parseBandsToShow(activeBands, scanner);
            if (colorsDistribution == null) {
                System.err.println("Couldn't parse bands to show");
                return;
            }
        }

        Data data = formater.loadData(activeBands);
        data.setDistribution(colorsDistribution);
        int[] bandsDistribution = Util.getGeneralToLocalDistribution(activeBands, activeNum);
        data.setBandDistribution(bandsDistribution);


        // какие отображать из загруженных

        System.out.println("Active: " + Arrays.toString(activeBands));
        System.out.println("Red: " + colorsDistribution.red() + ", green: " + colorsDistribution.green() + ", blue: " + colorsDistribution.blue());

        KMeans algo = new KMeans(data.getDataPoints(), k);
        int iterations = 1;
        if (!algo.run(iterations)) {
            System.err.println("Error while running k-means");
            return;
        }
        int[] clusteringResult = algo.getAssignment();
        List<Cluster> clusters = algo.getClusters();
        System.out.println("ASSIGNMENT LEN: " + clusteringResult.length);

        // после применения алгоритма
        // срдение цвета -- сумма по пикселям/ колв-во пикселей встретившихся/ случайные цвета
        // цвет для кластера
        ImageConstructor constructor = new ImageConstructor(data.getWidth(), data.getHeight());
//        BufferedImage img = constructor.constructImage(data.getPointsRGB("red"), data.getPointsRGB("green"), data.getPointsRGB("blue"));
        BufferedImage img = constructor.constructImage(clusters, clusteringResult, colorsDistribution, bandsDistribution);
        if (img != null) {
            System.out.println(img);
            ImageWindow imageWindow = new ImageWindow(img);
            imageWindow.displayImage();
        }
    }
    private static final String fileNameOpt = "-f";
    private static final String clustersNumOpt = "-k";
    private static final String USAGE = "Gimme args: <file_to_open> <[bands_to_download]> <[bands_to_show]>";
}
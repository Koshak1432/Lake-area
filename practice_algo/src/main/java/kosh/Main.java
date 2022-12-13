package kosh;

import kosh.kmeans.Cluster;
import kosh.kmeans.Kmeans;
import kosh.display.ImageConstructor;
import kosh.display.ImageWindow;
import kosh.formaters.GdalFormater_M;
import kosh.parsing.ArgsParser;
import kosh.parsing.ParsedArgs;
import kosh.util.Util;

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
            System.err.println(USAGE);
            return;
        }
        System.out.println(parsed.fileName());

        File[] files = ArgsParser.getFileNames(parsed.fileName());
        int k = parsed.k();

        GdalFormater_M formater = new GdalFormater_M();
        try {
            if (!formater.loadHeader_M(files)) {
                System.err.println("Couldn't load header");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        boolean[] activeBands = new boolean[formater.getBandsNumber_M()];
        int activeNum;
        BandsAsRGB colorsDistribution;

        System.out.println("Available bands up to  " + formater.getBandsNumber_M());
        System.out.println("Select bands to load:");
        try (Scanner scanner = new Scanner(System.in)) {
            // какие грузить
            activeNum = ArgsParser.parseBandsToLoad(activeBands, scanner, formater.getBandsNumber_M());
            System.out.println("Available bands to show: " + Arrays.toString(
                    ArgsParser.getAvailableBandsToShow(activeBands, activeNum)));
            System.out.println("Select bands to show(R G B)");
            //какие показывать
            colorsDistribution = ArgsParser.parseBandsToShow(activeBands, scanner, formater.getBandsNumber_M());
            if (colorsDistribution == null) {
                System.err.println("Couldn't parse bands to show");
                return;
            }
        }

        Data data = formater.loadData_M(activeBands);
        // отображает номер канала(in general) в локальный номер канала, тот что в 1d dataPoints
        int[] bandsDistribution = Util.getGeneralToLocalDistribution(activeBands, activeNum);
        data.setColorDistribution(colorsDistribution, bandsDistribution);

        System.out.println("Active: " + Arrays.toString(activeBands));

        Kmeans kMeans = new Kmeans(data.getDataPoints(), k);
        int iterations = 1;
        if (!kMeans.run(iterations)) {
            System.err.println("Error while running k-means");
            return;
        }
        short[] kmeansAssignment = kMeans.getAssignment();
        List<Cluster> clusters = kMeans.getClusters();
        data.setClusters(clusters);
        data.setClassificationAssignment(kmeansAssignment);

        // NDWI посчитать водный индекс
        // 4 пункт всё суммирую и делю на количество
        BufferedImage clustersColorsClusteringImg = ImageConstructor.constructImageByClustersColors(data);
        BufferedImage beforeClusteringImg = ImageConstructor.constructImage(data);
//        BufferedImage randomColorsImg = ImageConstructor.constructImageRandomColors(data);

        ImageWindow beforeClustering = new ImageWindow(beforeClusteringImg);
//        ImageWindow randomColorsClustering = new ImageWindow(randomColorsImg);
        ImageWindow meanColors = new ImageWindow(clustersColorsClusteringImg);
        beforeClustering.displayImage("Source img");
//        randomColorsClustering.displayImage("Clustering result");
        meanColors.displayImage("Clustering result");

        File outFile = new File("clusteringRes.png");
        if (!Util.saveImg(outFile, clustersColorsClusteringImg)) {
            System.err.println("Couldn't save img to file " + outFile.getPath());
        }

        File classificationOut = new File("clusteringRes");
        boolean res = formater.saveClassification(classificationOut, data, "HFA", null,
                                    Util.getRandomColors(data.getNumberOfClusters()));
        System.out.println(res);
    }
    private static final String USAGE = "Gimme args: -f<file_to_open> -k<num_of_clusters>";
}
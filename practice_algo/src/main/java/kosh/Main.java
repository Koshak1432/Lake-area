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
import java.util.*;

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

        Set<Short> waterClasses = NDWIcalculator.getWaterClasses(clusters,
                                                           data.getBandByDescription("3"),
                                                             data.getBandByDescription("5"));
        if (waterClasses == null) {
            System.err.println("Couldn't calculate NDWI, load nir(5) and green(3) channels");
        } else {
            Cluster waterCl = new Cluster();
            for (int i = 0; i < kmeansAssignment.length; ++i) {
                if (waterClasses.contains(kmeansAssignment[i])) {
                    waterCl.addToRelatedPoints(i);
                    kmeansAssignment[i] = (short) clusters.size();
                }
            }
            clusters.add(waterCl);
        }

        // найти пиксель озера: для снимков, где озеро слева снизу x = 1100, y = 5700, т.е. idx = y * width + x
        int x = 1100;
        int y =5700;
        int lakeIdx = y * data.getWidth() + x;
        clusters.add(AreaFiller.fillArea(lakeIdx, kmeansAssignment, (short) clusters.size(), data.getWidth()));
        // поменять класс озера на новый, при этом добавить новый класс в clusters, а к нему в relatedPoints точки???
        // или просто в assignment писать новый класс и локально? увеличить кол-во кластеров на 1
        // в общем, не понятно, как отображать это всё

        data.setClusters(clusters);
        data.setClassificationAssignment(kmeansAssignment);

        int lakePixels = 0;
        int koef = 30 * 30;
        for (short value : kmeansAssignment) {
            if (value == clusters.size() - 1) {
                ++lakePixels;
            }
        }
        System.out.println("lake area(meters^2): " + lakePixels * koef);

        BufferedImage clustersColorsClusteringImg = ImageConstructor.constructImageByClustersColors(data, clusters.size() - 2);
        BufferedImage beforeClusteringImg = ImageConstructor.constructImage(data);

        ImageWindow beforeClustering = new ImageWindow(beforeClusteringImg);
        ImageWindow meanColors = new ImageWindow(clustersColorsClusteringImg);
        meanColors.displayImage("Clustering result");
        beforeClustering.displayImage("Source img");

        File outFile = new File("clusteringRes.png");
        if (!Util.saveImg(outFile, clustersColorsClusteringImg)) {
            System.err.println("Couldn't save img to file " + outFile.getPath());
        }

        File classificationOut = new File("clusteringRes");
        boolean res = formater.saveClassification(classificationOut, data, "HFA", null,
                                    Util.getRandomColors(data.getNumberOfClusters()));
        System.out.println("saved classification: " + res);
    }

    private static final String USAGE = "Gimme args: -f<file_to_open> -k<num_of_clusters>";
}
package kosh;

import kosh.Kmeans.Cluster;
import kosh.Kmeans.KMeans;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

        File file = new File(parsed.fileName());
        int k = parsed.k();
        System.out.println("file name: " + parsed.fileName() + ", number of clusters: " + parsed.k());

        GdalFormater formater = new GdalFormater();
        if (!formater.loadHeader(file)) {
            System.err.println("Couldn't load header");
            return;
        }

        boolean[] activeBands = new boolean[formater.getBandsNumber()];
        int activeNum;
        BandsAsRGB colorsDistribution;
        System.out.println("Available bands: " + Arrays.toString(formater.getBandsDescription()));
        System.out.println("Select bands to load:");
        try (Scanner scanner = new Scanner(System.in)) {
            // какие грузить
            activeNum = ArgsParser.parseBandsToLoad(activeBands, scanner);
            System.out.println("Available bands to show: " + Arrays.toString(
                    ArgsParser.getAvailableBandsToShow(activeBands, activeNum)));
            System.out.println("Select bands to show(R G B)");
            //какие показывать
            colorsDistribution = ArgsParser.parseBandsToShow(activeBands, scanner);
            if (colorsDistribution == null) {
                System.err.println("Couldn't parse bands to show");
                return;
            }
        }

        // todo delete mask
        // many to one file landsat

        Data data = formater.loadData(activeBands);
        // отображает номер канала(in general) в локальный номер канала, тот что в 1d dataPoints
        int[] bandsDistribution = Util.getGeneralToLocalDistribution(activeBands, activeNum);
        data.setColorDistribution(colorsDistribution, bandsDistribution);

        System.out.println("Active: " + Arrays.toString(activeBands));
        System.out.println("Red: " + colorsDistribution.red() + ", green: " + colorsDistribution.green() + ", blue: " + colorsDistribution.blue());

        KMeans kmeans = new KMeans(data.getDataPoints(), k);
        int iterations = 1;
        if (!kmeans.run(iterations)) {
            System.err.println("Error while running k-means");
            return;
        }
        int[] kmeansAssignment = kmeans.getAssignment();
        List<Cluster> clusters = kmeans.getClusters();
        data.setClusters(clusters);
        data.setClassificationAssignment(kmeansAssignment);

//        ImageConstructor constructor = new ImageConstructor(data.getWidth(), data.getHeight());
//        BufferedImage clustersColorsClusteringImg = constructor.constructImageByClustersColors(data);
        BufferedImage beforeClusteringImg = ImageConstructor.constructImage(data);
        BufferedImage randomColorsImg = ImageConstructor.constructImageRandomColors(data);

        ImageWindow beforeClustering = new ImageWindow(beforeClusteringImg);
        ImageWindow randomColorsClustering = new ImageWindow(randomColorsImg);
        beforeClustering.displayImage("Source img");
        randomColorsClustering.displayImage("Clustering result");

        File outFile = new File("test.png");
        if (!Util.saveImg(outFile, randomColorsImg)) {
            System.err.println("Couldn't save img to file " + outFile.getPath());
        }

        File classificationOut = new File("test2");
        boolean res = formater.saveClassification(classificationOut, data, "HFA", null,
                                    Util.getRandomColors(data.getNumberOfClusters()));
        System.out.println(res);
    }
    private static final String USAGE = "Gimme args: -f<file_to_open> -k<num_of_clusters>";
}
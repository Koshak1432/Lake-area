package kosh.display;

import kosh.BandsAsRGB;
import kosh.Data;
import kosh.kmeans.Cluster;
import kosh.util.Util;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

public class ImageConstructor {
    public ImageConstructor(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static BufferedImage constructImage(Data data) {
        int width = data.getWidth();
        int height = data.getHeight();
        short[] red = data.getDataPoints()[data.getColorDistribution().red()];
        short[] green = data.getDataPoints()[data.getColorDistribution().green()];
        short[] blue = data.getDataPoints()[data.getColorDistribution().blue()];
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, Util.getRGBColor(red[y * width + x], green[y * width + x], blue[y * width + x]));
            }
        }
        return image;
    }

    public static BufferedImage constructImageByClustersColors(Data data, Set<Short> except) {
        int width = data.getWidth();
        int height = data.getHeight();
        BandsAsRGB localDistribution = data.getColorDistribution();
        List<Cluster> clusters = data.getClusters();
        short[] assignment = data.getClassificationAssignment();
        int[] colors = new int[clusters.size()];
        for (int i = 0; i < clusters.size(); ++i) {
            short[] clusterMean = clusters.get(i).getBandsMeans();
            colors[i] = Util.getRGBColor(clusterMean[localDistribution.red()],
                                         clusterMean[localDistribution.green()],
                                         clusterMean[localDistribution.blue()]);
        }
        if (except != null) {
            Short[] waterClasses = except.toArray(Short[]::new);
            for (Short waterClass : waterClasses) {
                colors[waterClass] = Util.getRGBColor(0, 0, 255);
            }
        }

        return getFilledImage(width, height, assignment, colors);
    }

    private static BufferedImage getFilledImage(int width, int height, short[] assignment, int[] colors) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, colors[assignment[y * width + x]]);
            }
        }
        return image;
    }

    public static BufferedImage constructImageByClustersColors(Data data) {
        return constructImageByClustersColors(data, null);
    }


    public static BufferedImage constructImageRandomColors(Data data) {
        int[] colors = Util.getRandomColors(data.getNumberOfClusters());
        short[] assignment = data.getClassificationAssignment();

        int width = data.getWidth();
        int height = data.getHeight();
        return getFilledImage(width, height, assignment, colors);
    }



    private final int width;
    private final int height;
}

package kosh;

import kosh.Kmeans.Cluster;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageConstructor {
    public ImageConstructor(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // bands must be ordered([0] -- red, [1] -- green, [2] -- blue)
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

    public static BufferedImage constructImageByClustersColors(Data data) {
        int width = data.getWidth();
        int height = data.getHeight();
        BandsAsRGB localDistribution = data.getColorDistribution();
        List<Cluster> clusters = data.getClusters();
        short[] assignment = data.getClassificationAssignment();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                short[] clusterMean = clusters.get(assignment[y * width + x]).getBandsMeans();
                image.setRGB(x, y, Util.getRGBColor(clusterMean[localDistribution.red()],
                                               clusterMean[localDistribution.green()],
                                               clusterMean[localDistribution.blue()]));
            }
        }
        return image;
    }


    public static BufferedImage constructImageRandomColors(Data data) {
        int[] colors = Util.getRandomColors(data.getNumberOfClusters());
        short[] assignment = data.getClassificationAssignment();

        int width = data.getWidth();
        int height = data.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, colors[assignment[y * width + x]]);
            }
        }
        return image;
    }



    private final int width;
    private final int height;
}

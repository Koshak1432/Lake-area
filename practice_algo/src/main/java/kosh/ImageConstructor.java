package kosh;

import kosh.Kmeans.Cluster;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ImageConstructor {
    public ImageConstructor(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private int getRGBColor(int r, int g, int b) {
        return ((0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF));
    }

    // bands must be ordered([0] -- red, [1] -- green, [2] -- blue)
    public BufferedImage constructImage(short[] red, short[] green, short[] blue) {
        assert(red.length == green.length && red.length == blue.length && red.length == width * height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, getRGBColor(red[y * width + x], green[y * width + x], blue[y * width + x]));
            }
        }
        return image;
    }

    public BufferedImage constructImageByClustersColors(List<Cluster> clusters, int[] assignment, BandsAsRGB generalColorDistribution, int[] bandsDistribution) {
        assert (assignment.length == width * height);
        BandsAsRGB localDistribution = Util.transformDistribution(generalColorDistribution, bandsDistribution);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                short[] clusterMean = clusters.get(assignment[y * width + x]).getBandsMeans();
                image.setRGB(x, y, getRGBColor(clusterMean[localDistribution.red()],
                                               clusterMean[localDistribution.green()],
                                               clusterMean[localDistribution.blue()]));
            }
        }
        return image;
    }


    public BufferedImage constructImageRandomColors(int[] assignment, int clustersNum) {
        assert (assignment.length == width * height);
        Random random = new Random();
        Map<Integer, Integer> colors = new HashMap<>();
        for (int i = 0; i < clustersNum; ++i) {
            colors.put(i, getRGBColor(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, colors.get(assignment[y * width + x]));
            }
        }
        return image;
    }



    private final int width;
    private final int height;
}

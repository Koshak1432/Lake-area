package kosh;

import kosh.Kmeans.Cluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    public Data(int width, int height, int activeNumber, String fileName) {
        this.width = width;
        this.height = height;
        this.activeNum = activeNumber;
        this.fileName = fileName;
        dataPoints = new short[activeNumber][width * height];
    }

    public void setBandDescription(int idx, String description) {
        bandsDescriptions.put(idx, description);
    }

    public String getBandDescription(int band) {
        if (bandsDescriptions.containsKey(band)) {
            return bandsDescriptions.get(band);
        }
        return null;
    }



    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    // [4, 1, 8] на входе --> [1, 0, 2]
    // 4 --> 1 (вторым обрабатывался)
    // 1 --> 0, 8 --> 2
    public void setColorDistribution(BandsAsRGB generalDistr, int[] bandsDistribution) {
        this.colorDistribution = Util.transformDistribution(generalDistr, bandsDistribution);
    }

    public short[][] getDataPoints() {
        return dataPoints;
    }

    public void setClassificationAssignment(short[] classificationAssignment) {
        this.classificationAssignment = classificationAssignment;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public short[] getClassificationAssignment() {
        return classificationAssignment;
    }

    public int getNumberOfClusters() {
        if (clusters != null) {
            return clusters.size();
        }
        return -1;
    }

    public BandsAsRGB getColorDistribution() {
        return colorDistribution;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public int getNumBands() {
        return activeNum;
    }

    public void setBandsDescriptions(String[] descriptions) {
        for (int i = 0; i < descriptions.length; ++i) {
            bandsDescriptions.put(i, descriptions[i]);
        }
    }

    public void setDataPoints(short[][] dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void setActiveNum(int activeNum) {
        this.activeNum = activeNum;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private final int width;
    private final int height;
    private int activeNum;
    private final String fileName;
    private double resolution;
    private BandsAsRGB colorDistribution;
    // [bands][width * height] со значениями в отрезке [0, 255], т.е. в первом измерении канал, а во втором значение пикселя [y * w + x] по этому каналу
    private short[][] dataPoints;
    private short[] classificationAssignment = null;
    private List<Cluster> clusters = null;
    private Map<Integer, String> bandsDescriptions = new HashMap<>();
}

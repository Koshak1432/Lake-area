package kosh;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Data {
    public Data(int width, int height, int activeNumber, String fileName, int totalBandsNum) {
        this.width = width;
        this.height = height;
        this.activeNum = activeNumber;
        this.fileName = fileName;
        bandsDistribution = new int[totalBandsNum];
        Arrays.fill(bandsDistribution, -1);
        dataPoints = new short[activeNumber][width * height];
    }

    public void setBandDistribution(int[] distribution) {
        bandsDistribution = distribution;
    }

    public void setBandDescription(int idx, String description) {
        bandsDescriptions.put(idx, description);
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    // [4, 1, 8] на входе --> [1, 0, 2]
    // 4 --> 1 (вторым обрабатывался)
    // 1 --> 0, 8 --> 2
    public void setDistribution(BandsAsRGB generalDistr) {
        this.distribution = Util.transformDistribution(generalDistr, bandsDistribution);
    }

    public short[][] getDataPoints() {
        return dataPoints;
    }

    public short[] getPointsRGB(String what) {
        if (what.toLowerCase().equals("red")) {
            return dataPoints[distribution.red()];
        }
        if (what.toLowerCase().equals("green")) {
            return dataPoints[distribution.green()];
        }
        if (what.toLowerCase().equals("blue")) {
            return dataPoints[distribution.blue()];
        }
        return null;
    }

    public void setDataPoints(short[][] dataPoints) {
        this.dataPoints = dataPoints;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private final int width;
    private final int height;
    private final int activeNum;
    private final String fileName;
    private double resolution;
    private BandsAsRGB distribution;
    // [bands][width * height] со значениями в отрезке [0, 255], т.е. в первом измерении канал, а во втором значение пикселя [y * w + x]
    private short[][] dataPoints;
    private final Map<Integer, String> bandsDescriptions = new HashMap<>();
    private int[] bandsDistribution; // отображает номер канала(in general) в локальный номер канала, тот что в 1d dataPoints
}

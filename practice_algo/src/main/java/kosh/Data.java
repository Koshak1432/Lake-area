package kosh;

import java.awt.*;

public class Data {
    public Data(int width, int height, int activeNumber, String fileName) {
        this.width = width;
        this.height = height;
        this.activeNum = activeNumber;
        this.fileName = fileName;
    }


    public void setBandDescription(int idx, String description) {

    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public void setDistribution(int red, int green, int blue) {
        distribution = new BandsAsRGB(red, green, blue);
    }

    public short[][] getDataPoints() {
        return dataPoints;
    }

    private final int width;
    private final int height;
    private final int activeNum;
    private final String fileName;
    private double resolution;
    private BandsAsRGB distribution;
    private short[][] dataPoints; // [bands][width * height] в отрезке [0, 255], т.е. в первом измерении канал, а во втором значение пикселя [y * w + x]
    //

}

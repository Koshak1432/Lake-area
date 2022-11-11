package kosh;

import java.awt.image.BufferedImage;

public class ImageConstructor {
    public ImageConstructor(short[] red, short[] green, short[] blue, int width, int height) {
        assert(red.length == green.length && red.length == blue.length);
        bands = new short[3][red.length];
        bands[0] = red;
        bands[1] = green;
        bands[2] = blue;
        this.width = width;
        this.height = height;
    }

    private int getRGBColor(short r, short g, short b) {
        return ((0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF));
    }

    // bands must be ordered([0] -- red, [1] -- green, [2] -- blue)
    public BufferedImage constructImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, getRGBColor(bands[0][y * width + x], bands[1][y * width + x], bands[2][y * width + x]));
            }
        }
        return image;
    }

    private final short[][] bands;
    private final int width;
    private final int height;
}

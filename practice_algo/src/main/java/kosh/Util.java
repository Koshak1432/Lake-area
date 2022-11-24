package kosh;

import java.util.Arrays;

public class Util {
    public static int[] getGeneralToLocalDistribution(boolean[] activeBands, int activeNum) {
        int[] distribution = new int[activeBands.length];
        Arrays.fill(distribution, -1);
        for (int i = 0, bInd = 0; i < activeBands.length; ++i) {
            if (activeBands[i]) {
                distribution[i] = bInd++;
            }
        }
        return distribution;
    }

    public static BandsAsRGB transformDistribution(BandsAsRGB generalColorDistribution, int[] bandsDistribution) {
        System.out.println("general: red: " + generalColorDistribution.red() + ", green: " + generalColorDistribution.green() + ", blue: " + generalColorDistribution.blue());
        int red = bandsDistribution[generalColorDistribution.red()];
        int green = bandsDistribution[generalColorDistribution.green()];
        int blue = bandsDistribution[generalColorDistribution.blue()];
        assert (red >= 0 && green >= 0 && blue >= 0);
        System.out.println("local: red: " + red + ", green: " + green + ", blue: " + blue);
        return new BandsAsRGB(red, green, blue);
    }
}

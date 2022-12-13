package kosh;

import kosh.kmeans.Cluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NDWIcalculator {
    public NDWIcalculator(short[] green, short[] nir) {
        this.green = green;
        this.nir = nir;
    }

    public Set<Short> getWaterClasses(List<Cluster> clusters) {
        if (green == null || nir == null) {
            return null;
        }
        Set<Short> waterClasses = new HashSet<>();
        for (short i = 0; i < clusters.size(); ++i) {
            List<Integer> points = clusters.get(i).getRelatedPointsCoords();
            double sum = 0;
            for (Integer idx : points) {
                sum += getNDWI(idx);
            }
            double res = sum / points.size();
            System.out.println("NDWI res " + i + ": " + res);
            if (res > 0.2) {
                waterClasses.add(i);
            }
        }
        return waterClasses;
    }
    private double getNDWI(int idx) {
        return (double)(green[idx] - nir[idx]) / (green[idx] + nir[idx]);
    }

    private final short[] green;
    private final short[] nir;
}

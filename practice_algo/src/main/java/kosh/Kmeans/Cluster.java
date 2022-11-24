package kosh.Kmeans;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    public Cluster(short[] point) {
        bandsMeans = point; // взял какой-то пиксель
    }

    public void addToRelatedPoints(int point) {
        relatedPointsCoords.add(point);
    }

    public short[] getBandsMeans() {
        return bandsMeans;
    }

    public List<Integer> getRelatedPointsCoords() {
        return relatedPointsCoords;
    }

    private final List<Integer> relatedPointsCoords = new ArrayList<>();
    private final short[] bandsMeans; // центр масс
}

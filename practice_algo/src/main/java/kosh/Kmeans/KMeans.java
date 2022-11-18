package kosh.Kmeans;

import java.util.*;

public class KMeans {
    public KMeans(short[][] points, int k) {
        this.k = k;
        this.pixels = points[0].length;
        this.numBands = points.length;
//        this.distribution = new ClusterDistribution[pixels];
        this.assignment = new int[pixels];
        this.prevAssignment = new int[pixels];
        this.points = new short[pixels][numBands];

        for (int i = 0; i < pixels; ++i) {
            for (int j = 0; j < numBands; ++j) {
                this.points[i][j] = points[j][i];
            }
        }

        System.out.println("Pixels: " + pixels);
        System.out.println("points[0].len : " + points[0].length + ", points.len: " + points.length);
        System.out.println("Num bands: " + numBands + ", k: " + k);
    }




//    private List<List<Integer>> kmeanspp(short[][] dataPoints, int k) {
//        List<List<Integer>> centroids = new ArrayList<>();
//
//        // выбрать рандомный из всех точек и засунуть
//        Random random = new Random();
//        List<Integer> first = new ArrayList<>();
//        first.add(random.nextInt(pixels));
//        centroids.add(first);
//
//        //добавить взвешенные центроиды
//        for (int i = 0; i < k; ++i) {
//            centroids.add()
//        }
//
//
//        return centroids;
//    }

    private void updateCluster(Cluster cluster) {
        int sum;
        short bandMean;
        for (int i = 0; i < numBands; ++i) {
            sum = 0;
            for (Integer pixelIdx : cluster.getRelatedPointsCoords()) {
                sum += points[pixelIdx][i];
            }
            bandMean = (short) (sum / cluster.getRelatedPointsCoords().size());
            cluster.getBandsMeans()[i] = bandMean;
        }
    }

    private void initClusters() {
        Random random = new Random();
        Set<Integer> chosenIdxes = new HashSet<>();
        int idx;
        for (int i = 0; i < k; ++i) {
            do {
                idx = random.nextInt(pixels);
            } while (chosenIdxes.contains(idx));
            chosenIdxes.add(idx);
            clusters.add(new Cluster(Arrays.copyOf(points[idx], numBands)));
        }

    }

    private double calculateSSE() {
        double sum = 0;
        for (Cluster cluster : clusters) {
            for (Integer idx : cluster.getRelatedPointsCoords()) {
                sum += calculateDistance(points[idx], cluster.getBandsMeans());
            }
        }
        return sum;
    }

    private void addToClusters() {
        for (int i = 0; i < pixels; ++i) {
            clusters.get(assignment[i]).addToRelatedPoints(i);
        }
    }

    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.getRelatedPointsCoords().clear();
        }
    }

    public void run() {
        System.out.println("Start k-means");
        double bestSSE = Double.MAX_VALUE;
        double SSE;
        int[] bestAssignment = new int[0];
        List<Cluster> bestClusters = null;

        for (int i = 0; i < iterations; ++i) {
            long start = System.currentTimeMillis();
            SSE = kmeans();
            long end = System.currentTimeMillis();
            System.out.println("Time passed of 1 iteration(sec): " + (end - start) / 1000);
            System.out.println("SSE after iteration # " + i + " : " + SSE);
            if (SSE < bestSSE) {
                bestSSE = SSE;
                bestAssignment = Arrays.copyOf(assignment, assignment.length);
                bestClusters = new ArrayList<>(clusters);
            }
        }
        System.out.println("Best sse: " + bestSSE);
        assignment = bestAssignment;
        clusters = bestClusters;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public int[] getAssignment() {
        return assignment;
    }

    private double kmeans() {
        double SSE = Double.MAX_VALUE; // Sum of Squared Errors
        double minDistance;
        //select k init centroids
        initClusters();

        while (true) {
            clearClusters();
            System.out.println("Finding the nearest clusters...");
            for (int i = 0; i < pixels; ++i) {
                minDistance = Double.MAX_VALUE;
                int nearestClusterIdx = 0;
                for (int j = 0; j < clusters.size(); ++j) {
                    double dist = calculateDistance(points[i], clusters.get(j).getBandsMeans());
                    if (dist < minDistance) {
                        minDistance = dist;
                        nearestClusterIdx = j;
                    }
                }
                assignment[i] = nearestClusterIdx;
            }
            addToClusters();

            System.out.println("Updating clusters...");
            for (Cluster cluster : clusters) {
                updateCluster(cluster);
            }
            System.out.println("Calculating sse...");
            double newSSE = calculateSSE();
//            System.out.println("SSE: " + SSE + ", NEW SSE: " + newSSE + ", difference: " + (SSE - newSSE));
            if (SSE - newSSE <= PRECISION) {
                return newSSE;
            }
            SSE = newSSE;


//            if (Arrays.equals(assignment, prevAssignment)) {
//                break;
//            }
//            prevAssignment = Arrays.copyOf(assignment, assignment.length);
        }
    }

    // a and b are bands arrays
    private double calculateDistance(short[] a, short[] b) {
        double sum = 0;
        for (int i = 0; i < numBands; ++i) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return sum;
    }


    private final int k;
    private final int numBands;
    private final int pixels;
    private final short[][] points; // dim 1 -- pixel idx, dim 2 -- bands
    private final double PRECISION = 0.001;
    private List<Cluster> clusters = new ArrayList<>();
//    private final Map<Integer, Integer> distribution; // key -- pixel idx, value -- cluster idx
    private int[] assignment;
    private int[] prevAssignment;
    private final int iterations = 10;
}

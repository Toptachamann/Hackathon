//package test.back.code;
//
//import javafx.scene.paint.Stop;
//import org.apache.commons.lang3.time.StopWatch;
//import org.jgrapht.Graph;
//import org.jgrapht.graph.DefaultWeightedEdge;
//
//import java.lang.reflect.Array;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.*;
//
//public class ConcurrentSimilarityFinder {
//    static class Task implements Runnable {
//        int i;
//        int j;
//        double[][] matrix;
//        String first;
//        String second;
//
//
//        public Task(double[][] matrix, int i, int j, String first, String second) {
//            this.matrix = matrix;
//            this.i = i;
//            this.j = j;
//            this.first = first;
//            this.second = second;
//        }
//
//        @Override
//        public void run() {
//            SimilarityFinder finder = new SimilarityFinder();
//            matrix[i][j] = finder.findSimilarity(first, second, SimilarityFinder.Lang.JAVA);
//            System.out.println("Terminated " + i + " : " + j);
//        }
//    }
//
//    public void computeGraph(List<String> project1, List<String> project2) {
//        int m = project1.size(), n = project2.size();
//        ExecutorService executor =Executors.newFixedThreadPool(2);
//        double[][] matrix = new double[m][n];
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                Runnable task = new ConcurrentSimilarityFinder.Task(matrix, i, j, project1.get(i), project2.get(j));
//                executor.execute(task);
//            }
//        }
//            System.out.println("Start to wait");
//            executor.shutdown();
//        try {
//            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(Arrays.deepToString(matrix));
//    }
//
//    public static void main(String[] args) {
//        List<String> first = Arrays.asList("/home/timofey/Documents/All_info/Java_Projects/HackathonProject/src/main/resources/base/code1.txt",
//                "/home/timofey/Documents/All_info/Java_Projects/HackathonProject/src/main/resources/base/code2.txt",
//                "/home/timofey/Documents/All_info/Java_Projects/HackathonProject/src/main/resources/base/code3.txt");
//        List<String> second = Arrays.asList("/home/timofey/Documents/All_info/Java_Projects/HackathonProject/src/main/resources/input/code1.txt");
//        ConcurrentSimilarityFinder finder = new ConcurrentSimilarityFinder();
//        StopWatch watch = StopWatch.createStarted();
//        finder.computeGraph(first, second);
//        watch.stop();
//        System.out.println(watch);
//
//    }
//}

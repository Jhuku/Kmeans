import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * KMeans
 */
public class KMeans {
    public static class Point {
        double x;
        double y;
        char name;

        Point(char name, double x, double y) {
            this.x = x;
            this.y = y;
            this.name = name;
        }
    }

    public static class TwoPointsDistance{
        Point p1;
        Point p2;
        double dist;
        TwoPointsDistance(Point p1, Point p2, int dist){
            this.p1 = p1;
            this.p2 = p2;
            this.dist = dist;
        }
    }

    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        List<TwoPointsDistance> twoPointsDistances = new ArrayList<>();
        points.add(new Point('A', 6, 2));
        points.add(new Point('B', 7, 3));
        points.add(new Point('C', 9, 3));
        points.add(new Point('D', 8, 5));
        points.add(new Point('E', 9, 8));
        points.add(new Point('F', 8, 9));
        points.add(new Point('G', 7, 10));
        points.add(new Point('H', 6, 11));
        points.add(new Point('I', 8, 14));
        points.add(new Point('J', 6, 14));
        points.add(new Point('K', 4, 14));
        points.add(new Point('L', 2, 14));
        points.add(new Point('M', 2, 8));
        points.add(new Point('N', 2, 6));
        points.add(new Point('O', 3, 5));

        for(int i=0; i<points.size(); i++){
            
        }


        for(int i=0; i<points.size()-1; i++){
            System.out.println("Distance between ("+points.get(i).name+") and("+points.get(i+1).name+") = "+getDist(points.get(i), points.get(i+1)));

        }
        // points.add(new Point('A', 2, 2));
        // points.add(new Point('B', 2, 1));
        // points.add(new Point('C', 3, 1));
        // points.add(new Point('D', 10, 10));
        // points.add(new Point('E', 11, 9));
        // points.add(new Point('F', 12, 9));
        // points.add(new Point('G', 6, 6));
        // points.add(new Point('H', 7, 5));

        double[] bounds = new double[4];
        bounds = getBounds(points, bounds);
        System.out.println("Bounds = ");
        for (double i : bounds) {
            System.out.println("" + i);
        }
        int k = 3;
        Point[] k_points = new Point[k];
        // k_points[0].x=1;
        get_k_random_points(bounds, k_points, k);

        Map<Point, ArrayList<Point>> map = new ConcurrentHashMap<>();
        double flag = 0;
        double minDist = 0.0;
        boolean re_assignment_needed = true;
        Point k_category = null;
        int cc = 0;

        while (cc < 10) {
            // System.out.println("Random points are");
            // for (Point p : k_points) {
            //     System.out.print("(" + p.x + "," + p.y + ")");
            //     System.out.println("");
            // }
            // Assign each data points to the k centroids
            for (Point point : points) {
                flag = 0;
                for (Point p : k_points) {
                    if (flag == 0) {
                        minDist = getDist(point, p);
                        k_category = p;
                        flag = 1;
                    }
                    if (getDist(point, p) < minDist) {
                        minDist = getDist(point, p);
                        k_category = p;
                    }
                }
                // System.out.println("point: " + point.name + " belongs to (" + k_category.x +
                // "," + k_category.y + ")");
                if (!map.containsKey(k_category)) {
                    ArrayList<Point> alp = new ArrayList<>();
                    alp.add(point);
                    map.put(k_category, alp);
                } else {
                    ArrayList<Point> alp = map.get(k_category);
                    alp.add(point);
                    // if its already not there
                    map.put(k_category, alp);
                }
            }
            showMap(map);
            updateMapKeys(map, k_points);
            // System.out.println("Showing again");

            // showMap(map);
            re_assignment_needed = false;
            cc++;
            map.clear();
        }

    }

    private static void updateMapKeys(Map<KMeans.Point, ArrayList<KMeans.Point>> map, Point[] k_points) {
        Iterator<Map.Entry<Point, ArrayList<Point>>> entries = map.entrySet().iterator();
        int count = 1;
        while (entries.hasNext()) {
            Map.Entry<Point, ArrayList<Point>> entry = entries.next();
            Point p = entry.getKey();
            ArrayList<Point> alp = entry.getValue();
            double mean_x;
            double mean_y;

            double sum_x = 0;
            double sum_y = 0;
            for (Point i : alp) {
                sum_x = sum_x + i.x;
                sum_y = sum_y + i.y;
            }
            mean_x = sum_x / alp.size();
            mean_y = sum_y / alp.size();
            // System.out.println("Mean for points in (" + p.x + "," + p.y + ") is ( " + mean_x
            //         + "," + mean_y + " )");
            Point np = new Point('x', mean_x, mean_y);
            if (count < k_points.length) {
                k_points[count - 1] = np;
            }
            Point newKey = new Point('x', mean_x, mean_y);
            //map.put(newKey, alp);
            //map.remove(p);
            // System.out.println("MAPP after updating");
            // showMap(map);
            
            // entries.remove();
            count++;

        }
    }

    private static void showMap(Map<KMeans.Point, ArrayList<KMeans.Point>> map) {
        // Map<String, MyGroup> map = new HashMap<String, MyGroup>();
        System.out.println("Showing MAP-------------------");
        Iterator<Map.Entry<Point, ArrayList<Point>>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Point, ArrayList<Point>> entry = entries.next();
            ArrayList<Point> alp = entry.getValue();
            System.out.print(" Key = (" + entry.getKey().x + ", " + entry.getKey().y + ")=>");
            for (Point p : alp) {
                System.out.print(" " + p.name + "("+p.x+", "+p.y+")");
            }
            System.out.println("");
        }
    }

    private static double getDist(KMeans.Point point, KMeans.Point p) {
        return Math.sqrt(Math.pow((p.y - point.y), 2) + Math.pow((p.x - point.x), 2));
    }

    private static void get_k_random_points(double[] bounds, Point[] k_points, double size) {

        int count = 0;
        while (count < size) {
            Random rand = new Random();
            int random_integer_x = rand.nextInt((int) bounds[0] - (int) bounds[1]) + (int) bounds[1];
            // k_points[count].x = random_integer_x;
            int random_integer_y = rand.nextInt((int) bounds[2] - (int) bounds[3]) + (int) bounds[3];
            // k_points[count].y = random_integer_y;
            Point newPoint = new Point('x', random_integer_x, random_integer_y);
            k_points[count] = newPoint;
            count++;
        }
    }

    private static double[] getBounds(List<KMeans.Point> points, double[] bounds) {

        double maxRight = points.get(0).x;
        double minLeft = points.get(0).x;
        double maxTop = points.get(0).y;
        double minBottom = points.get(0).y;

        bounds[0] = maxRight;
        bounds[1] = minLeft;
        bounds[2] = maxTop;
        bounds[3] = minBottom;

        for (Point p : points) {
            if (p.x > maxRight) {
                bounds[0] = p.x;
                maxRight = p.x;
            }
            if (p.x < minLeft) {
                bounds[1] = p.x;
                minLeft = p.x;
            }
            if (p.y > maxTop) {
                bounds[2] = p.y;
                maxTop = p.y;
            }
            if (p.y < minBottom) {
                minBottom = p.y;
                bounds[3] = minBottom;
            }
        }
        return bounds;
    }
}
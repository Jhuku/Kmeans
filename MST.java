import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * KMeans
 */
public class MST {
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

    public static class TwoPointsDistance {
        Point p1;
        Point p2;
        double dist;

        TwoPointsDistance(Point p1, Point p2, double dist) {
            this.p1 = p1;
            this.p2 = p2;
            this.dist = dist;
        }
    }

    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        List<TwoPointsDistance> twoPointsDistances = Collections.synchronizedList(new ArrayList<TwoPointsDistance>());
        List<TwoPointsDistance> mst = new ArrayList<>();
        List<TwoPointsDistance> remaining = new ArrayList<>();
        Map<Character, Character> map = new ConcurrentHashMap<>();

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

        //populate group map
        for(Point p: points){
            map.put(p.name, 'x');
        }

        int flag = 0;
        double minDist = 0;
        TwoPointsDistance minTwoDistFinal = null;
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < i; j++) {

                double d = getDist(points.get(i), points.get(j));
                TwoPointsDistance minTwoDist = new TwoPointsDistance(points.get(i), points.get(j), d);
                twoPointsDistances.add(minTwoDist);
                if (flag == 0) {
                    minDist = d;
                    minTwoDistFinal = minTwoDist;
                    flag = 1;
                } else if (d < minDist) {
                    minDist = d;
                    minTwoDistFinal = minTwoDist;
                }
            }
        }
        // for (TwoPointsDistance tpd : twoPointsDistances) {
        //     System.out.println("P1=" + tpd.p1.name + " P2=" + tpd.p2.name + " Dist = " + tpd.dist);
        // }
        // System.out.println("The min dist is " + minTwoDistFinal.p1.name + ", " + minTwoDistFinal.p2.name + ", dist = "
        //         + minTwoDistFinal.dist);
        // mst.add(minTwoDistFinal);
        // twoPointsDistances.remove(minTwoDistFinal);

        double minDistance = 0.0;
        int count=0;
        while (twoPointsDistances.size()>0) {
            int flag1 = 0;
            for (TwoPointsDistance tpd : twoPointsDistances) {
                // System.out.println("P1="+tpd.p1.name+" P2="+tpd.p2.name+" Dist = "+tpd.dist);
                if (flag1 == 0) {
                    minDistance = tpd.dist;
                    minTwoDistFinal = tpd;
                    flag1 = 1;
                } else if (tpd.dist < minDistance) {
                    minTwoDistFinal = tpd;
                    minDistance = tpd.dist;
                }
                //twoPointsDistances.remove(minTwoDistFinal);
            }
            
            if(!formsCycle(mst, minTwoDistFinal)){
                mst.add(minTwoDistFinal);
                //System.out.println("Added: "+minTwoDistFinal.dist+" between ("+minTwoDistFinal.p1.name+" and "+minTwoDistFinal.p2.name+")");
            }
            // else{twoPointsDistances.remove(minTwoDistFinal);}
            // else{System.out.println("Not adding "+" between ("+minTwoDistFinal.p1.name+" and "+minTwoDistFinal.p2.name+")");}
            else{remaining.add(minTwoDistFinal);}
            twoPointsDistances.remove(minTwoDistFinal);
            count++;
        }
        System.out.println("MST  is-------------");
                for (TwoPointsDistance tpd : mst) {
            System.out.println("P1=" + tpd.p1.name + " P2=" + tpd.p2.name + " Dist = " + tpd.dist);
        }
        
        System.out.println("Remaining edges to join = ");
                for (TwoPointsDistance tpd : remaining) {
            System.out.println("P1=" + tpd.p1.name + " P2=" + tpd.p2.name + " Dist = " + tpd.dist);
        }

        // create map
        System.out.println("Parent findinggggg now");
                for (TwoPointsDistance tpd : remaining) {
            // System.out.println("parent of "+ tpd.p1.name + " is " +findParentOf(tpd.p1, mst));
            // System.out.println("parent of "+ tpd.p2.name + " is " +findParentOf(tpd.p2, mst));
            if(findParentOf(tpd.p1, mst) != findParentOf(tpd.p2, mst)){
                System.out.println("Adding: "+tpd.p1.name+" and "+tpd.p2.name);
                mst.add(tpd);
            }
        }

        System.out.println("Finally MST  is-------------");
                for (TwoPointsDistance tpd : mst) {
            System.out.println("P1=" + tpd.p1.name + " P2=" + tpd.p2.name + " Dist = " + tpd.dist);
        }
    }

    private static boolean formsCycle(List<MST.TwoPointsDistance> mst, MST.TwoPointsDistance minTwoDistFinal) {
        boolean f1=true;
        boolean f2=true;

        // //if both have same parent then its a cycle
        // char parent1 = findParentOf(minTwoDistFinal.p1, mst);
        // char parent2 = findParentOf(minTwoDistFinal.p2, mst);
        
        // // System.out.println("Parent of "+ minTwoDistFinal.p1.name + " is "+parent1);
        // // System.out.println("Parent of "+ minTwoDistFinal.p2.name + " is "+parent2);
        
        // if (parent1 == parent2){
        //     return true;
        // }
        // return false;
        for(MST.TwoPointsDistance item: mst){
            if(minTwoDistFinal.p1.name == item.p1.name || minTwoDistFinal.p1.name == item.p2.name){
                f1=false;
            } 
        }
        for(MST.TwoPointsDistance item: mst){
            if(minTwoDistFinal.p2.name == item.p1.name || minTwoDistFinal.p2.name == item.p2.name){
                f2=false;
            } 
        }
        if(f1 == false && f2 == false){
            return true;
        }
        return false;
    }

    private static char findParentOf(MST.Point p1, List<MST.TwoPointsDistance> mst) {
        boolean found=true; 
        for(MST.TwoPointsDistance item: mst){
            if(p1.name == item.p1.name){
                found = false;
                return findParentOf(item.p2, mst);
            }
        }
        if(found == true){
            return p1.name;
        }
        return 'x';
    }

    private static double getDist(Point point, Point p) {
        return Math.sqrt(Math.pow((p.y - point.y), 2) + Math.pow((p.x - point.x), 2));
    }
}
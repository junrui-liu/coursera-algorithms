
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class Test {
    
    public static void main(String[] args)
    {
//        testCompare();
        LineSegmentTest();
//        TreeTest();
    }
    
    /**
     * Unit tests the Point data type.
     */
    public static void pointTest() {
        // origin
        Point o = new Point(0, 0);
        // x-axis
        Point i = new Point(1, 0);
        Point i2 = new Point(2, 0);
        Point _i = new Point(-1, 0);
        // y-axis
        Point j = new Point(0, 1);
        Point j2 = new Point(0, 2);
        Point j3 = new Point(0, 3);
        Point _j = new Point(0, -1);
        // others
        Point p = new Point(1, 2);
        Point q = new Point(1, 1);
        
        StdOut.println(o.slopeTo(i));
        StdOut.println(o.slopeTo(j));
        StdOut.println(o.slopeTo(o));
        StdOut.println(i.slopeTo(j));
        
//        Comparator<Point> slopeO = o.slopeOrder();
//        StdOut.println(slopeO.compare(i, j));
//        StdOut.println(slopeO.compare(p, i));
//        StdOut.println(slopeO.compare(p, j));
//        
//        StdOut.println(slopeO.compare(_j, j));
//        StdOut.println(slopeO.compare(_i, i));
        
        Comparator<Point> slopeQ = q.slopeOrder();
        StdOut.println(slopeQ.compare(_i, _j));
        StdOut.println(slopeQ.compare(i2, j3));
        StdOut.println(slopeQ.compare(j3, _j));
       
    }

    public static void LineSegmentTest() {
        
        String[] test_file = {"input4000", "mystery10089", "grid6x6c", "random152"};
        String test_ext = ".txt";
        int nth = 2;
        String test_path = "/Users/jun/Downloads/cs/AL/W3/collinear/";
        try {
            Scanner sc = new Scanner(new File(test_path + test_file[nth] + test_ext));
            
            int n = sc.nextInt();
            int x, y;
            Point[] points = new Point[n];
            
            for (int i = 0; i < n; i++)
            {
                x = sc.nextInt();
                y = sc.nextInt();
                points[i] = new Point(x, y);
                //points[i].draw();
            }

//            BruteCollinearPoints c = new BruteCollinearPoints(points);
            FastCollinearPoints c = new FastCollinearPoints(points);
            LineSegment[] segments = c.segments();
            for (LineSegment seg: segments)
            {
                // seg.draw();
                StdOut.println(seg);
            }
            StdOut.println(c.numberOfSegments());
        }
        catch (FileNotFoundException e)
        {
            System.exit(1);
        }
    }
    
    private static void TreeTest()
    {
        int N = 10;
        int[] arr = new int[N];
        for (int i = 0; i < N; i++)
            arr[i] = i;
        StdRandom.shuffle(arr);
        BSTree<Integer> tree = new BSTree(arr[0]);
        for (int i = 1; i < N; i++)
        {
            tree.add(arr[i]);
        }
        StdOut.println(tree.toString());
        
    }
    
    private static void testCompare()
    {
        Point p = new Point(8, 3);
        Point q = new Point(8, 9);
        Point r = new Point(8, 3);
        StdOut.println(p.slopeOrder().compare(q, r));
    }
}

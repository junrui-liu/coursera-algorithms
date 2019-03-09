
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

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
    
    /**
     *  unit testing of the methods (optional) 
     * @param args 
     */
    public static void main(String[] args)  
    {
//        StdRandom.setSeed(1532339828272L); // 2/3
        StdRandom.setSeed(1532339848699L); // 0/1
        // test range
        double xmin = StdRandom.uniform(0, 0.25);
        double ymin = StdRandom.uniform(0, 0.25);
        double xmax = StdRandom.gaussian((1-xmin)/3*2, 0.25) + xmin;
        double ymax = StdRandom.gaussian((1-ymin)/3*2, 0.25) + ymin;
        
//        int n = 100;
//        KdTree t = new KdTree();
//        Point2D[] points = randomPoints(n);
//        for (Point2D p: points)
//            t.insert(p);
//        PointSET s = new PointSET();

//        String filename = args[0];
        String filename = "input10.txt";
        In in = new In(filename);
        KdTree t = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            t.insert(p);
        }
        
        StdOut.println(t.size());
        
        
//        RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
//        RectHV rect = new RectHV(0, 0, 1, 1);
//        rect.draw();
        
//        Iterable<Point2D> inrange = t.range(rect);
//        for (Point2D p: inrange)
//            t.drawDiagonalCross(p, 2);
        
//        t.draw();
//         test nearest
//        Point2D r1 = points[StdRandom.uniform(n)];
        Point2D r1 = new Point2D(0.417, 0.43);
        Point2D r2 = t.nearest(r1);
//        t.drawCross(r1);
//        t.drawCross(r2);
//        r1.drawTo(r2);
        
        
        StdOut.println(StdRandom.getSeed());
    }
    
    private static Point2D[] randomPoints(int n)
    {
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; i++)
        {
            double x, y;
            x = StdRandom.uniform();
            y = 0.5;
            Point2D p = new Point2D(x, y);
//            s.insert(p);
            points[i] = p;
        }
        return points;
    }
}

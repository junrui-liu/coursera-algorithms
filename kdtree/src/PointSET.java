/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private final SET<Point2D> s;
    private static final double SIDE_LENGTH = 0.01;

    /**
     * construct an empty set of points
     */
    public PointSET()
    {
        s = new SET<>();
    }

    /**
     * is the set empty?
     * @return
     */
    public boolean isEmpty()
    {   return s.isEmpty(); }

    /**
     * number of points in the set
     * @return
     */
    public int size()
    {   return s.size();    }

    /**
     * add the point to the set (if it is not already in the set)
     * @param p
     */
    public void insert(Point2D p)
    {
        if (p == null)
            throw new java.lang.IllegalArgumentException();
        s.add(p);
    }

    /**
     * does the set contain point p?
     * @param p
     * @return
     */
    public boolean contains(Point2D p)
    {   return s.contains(p);  }

    private void drawCross(Point2D p, double multiplier)
    {
        double len = SIDE_LENGTH * multiplier;
        (new Point2D(p.x(), p.y() + len)).drawTo(new Point2D(p.x(), p.y() - len));
        (new Point2D(p.x() + len, p.y())).drawTo(new Point2D(p.x()  - len, p.y()));

    }

    private void drawCross(Point2D p)
    {   drawCross(p, 1.0);  }

    private void drawDiagonalCross(Point2D p)
    {   drawDiagonalCross(p, 1.0);  }

    private void drawDiagonalCross(Point2D p, double multiplier)
    {
        double len = SIDE_LENGTH * multiplier;
        (new Point2D(p.x() + len, p.y() + len)).drawTo(new Point2D(p.x() - len, p.y() - len));
        (new Point2D(p.x() + len, p.y() - len)).drawTo(new Point2D(p.x() - len, p.y() + len));
    }

    /**
     * draw all points to standard draw
     */
    public void draw()
    {
        for (Point2D p: s)
        {
            drawDiagonalCross(p);
//            drawCross(p);
        }
//            p.drawTo(new Point2D(p.x() + SIDE_LENGTH, p.y() + SIDE_LENGTH));
//            p.draw();
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect)
    {
        Stack<Point2D> stack = new Stack<>();
        double x, y;
        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();
        for (Point2D p: s)
        {
            x = p.x();
            y = p.y();
            if (x >= xmin && x <= xmax && y >= ymin && y <= ymax)
                stack.push(p);
        }
        return stack;
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p)
    {
        if (s.size() == 0)
            return null;

        assert p != null;

        Point2D nearest = p;
        double nearestDistance = 0;
        double currentDistance;
        for (Point2D q: s)
        {
            assert q != null;
            if (nearest == p)   // not initialized
            {
                nearest = q;
                nearestDistance = p.distanceTo(q);
            }
            else
            {
                if (p == q) {
                } else{
                    currentDistance = p.distanceTo(q);
                    if (currentDistance < nearestDistance)
                    {
                        nearestDistance = currentDistance;
                        nearest = q;
                    }
                }
            }
        }
        return nearest;
    }

    /**
     * unit testing of the methods (optional)
     * @param args
     */
    public static void main(String[] args)
    {
    }
}

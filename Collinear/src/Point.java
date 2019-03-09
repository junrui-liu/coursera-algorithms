/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    //private final Comparator<Point> c;
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that)
    {
        if (this.y == that.y)
        {
            if (this.x == that.x)   // same point
                return Double.NEGATIVE_INFINITY;
            else    // y == y, i.e. horizontal
                return +0.0;
        }
        else if (this.x == that.x)  // x == x but y != y, i.e. vertical
            return Double.POSITIVE_INFINITY;
        else    // common case
            return 1.0 * (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    @Override
    public int compareTo(Point that) {
        if (this.y < that.y)
            return -1;
        else if (this.y > that.y)
            return 1;
        else  // y0 == y1, breaking ties by x
        {
            if (this.x < that.x)
                return -1;
            else if (this.x > that.x)
                return 1;
            else                      
                return 0;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new BySlopeInt(this);
    }
    
    private Comparator<Point> slopeOrderDouble() {
        return new BySlopeDouble(this);
    }
    
    private class BySlopeInt implements Comparator<Point>
    {
        Point p;
        public BySlopeInt(Point p)
        {   this.p = p;   }
        
        @Override
        public int compare(Point o1, Point o2) 
        {
            long dx1 = o1.x - p.x;
            long dx2 = o2.x - p.x; 
            long dy1 = o1.y - p.y;
            long dy2 = o2.y - p.y;
            
            if (dx1 != 0 && dx2 != 0)   // neither of two lines is vertical
            {
                
                long dx1_dy2 = dx1 * dy2;
                long dx2_dy1 = dx2 * dy1;

                if (dx1_dy2 < dx2_dy1)
                    return (dx1 * dx2 > 0)? 1: -1;
                else if (dx1_dy2 > dx2_dy1)
                    return (dx1 * dx2 > 0)? -1: 1;
                else
                    return 0;
            }  
//            else if (dx1 == 0 && dx2 != 0)  // dx1 == 0, dx2 != 0
//                return (dy1 == 0)? -1: 1;
//            else if (dx1 != 0 && dx2 == 0)           // dx2 == 0
//                return (dy2 == 0)? 1: -1;
//            else  // if (dx1 == 0 && dx2 == 0)
//               return 0;
            else if (dx1 == 0 && dy1 == 0 && dx2 == 0 && dy2 == 0) return 0;
            else if (dx1 == 0 && dy1 == 0) return -1;
            else if (dx2 == 0 && dy2 == 0) return 1;
            else if (dx1 == 0 && dx2 == 0) return 0;
            else if (dx1 == 0) return 1;
            else if (dx2 == 0) return -1;
            else
                throw new IllegalArgumentException();
        }
    }
    
    private class BySlopeDouble implements Comparator<Point>
    {
        Point p;
        public BySlopeDouble(Point p)
        {   this.p = p;   }
        
        @Override
        public int compare(Point o1, Point o2) 
        {
            if (o1 == null || o2 == null)
                throw new java.lang.NullPointerException();
//            int dx1 = o1.x - p.x;
//            int dx2 = o2.x - p.x; 
//            if (dx1 != 0 && dx2 != 0)
            {
                double k1 = p.slopeTo(o1);
                double k2 = p.slopeTo(o2);
//                if (k1 == Double.NEGATIVE_INFINITY || k2 == Double.NEGATIVE_INFINITY)
//                    return 1;
                if (k1 < k2)
                    return -1;
                else if (k1 > k2)
                    return 1;
                else
                    return 0;
            }
//            else if (dx1 == 0 && dx2 == 0)
//            {   return 0;   }
//            else if (dx1 == 0)
//            {   return 1;  }
//            else    // dx2 == 0
//            {   return -1;   }
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
}

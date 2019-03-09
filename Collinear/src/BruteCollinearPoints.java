
import edu.princeton.cs.algs4.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int numberOfSegments;
    
    /**
     * finds all line segments containing 4 points
     * @param points 
     */
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null)
            throw new java.lang.IllegalArgumentException("Null array");
        
        int len = points.length;
        for (int i = 0; i < len; i++)
        {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException("Null point");
            for (int j = 0; j < i; j++)
            {
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException("Repeated points");
            }
        }
        Point[] pointsCopy = new Point[len];
        System.arraycopy(points, 0, pointsCopy, 0, len);
        this.segments = findSegments(pointsCopy);
        this.numberOfSegments = segments.length;
    }
    
    /**
     * the number of line segments
     * @return 
     */
    public int numberOfSegments()
    {
        return numberOfSegments;
    }
    
    private static boolean isSamePoint(Point p, Point q)
    {   return p.slopeTo(q) == Double.NEGATIVE_INFINITY; }

    private static boolean isCollinear(Point p, Point q, Point r, Point s)
    {

        return isCollinear(p,q,r) && isCollinear(p,q,s) &&
               isCollinear(p,r,s) && isCollinear(q,r,s);
    }

    private static boolean isCollinear(Point p, Point q, Point r)
    {
        if (isSamePoint(p,q) || isSamePoint(q, r) || isSamePoint(p, r))
            return true;
        else
            return p.slopeOrder().compare(q, r) == 0;
    }
    
    private void addToStack(LineSegmentExt seg, Stack<LineSegmentExt> stack)
    {
        Stack<LineSegmentExt> tempStack = new Stack<>();
        while (!stack.isEmpty())
        {
            LineSegmentExt topSeg = stack.pop();
            if (seg.isCollinear(topSeg))
            {
                LineSegmentExt newSeg = seg.merge(topSeg);
                stack.push(newSeg);
                break;
            }
            else
                tempStack.push(topSeg);
        }
        if (stack.isEmpty())
            stack.push(seg);
        while (!tempStack.isEmpty())
            stack.push(tempStack.pop());
    }
    
    
    /**
     * return the line segments
     * @return 
     */
    public LineSegment[] segments()
    {   
        LineSegment[] copy = new LineSegment[this.numberOfSegments];
        System.arraycopy(this.segments, 0, copy, 0, numberOfSegments);
        return copy;
    }
    
    /**
     * find the line segments
     * @return 
     */
    private LineSegment[] findSegments(Point[] points)
    {
        if (points.length < 4)
            return new LineSegment[0];
        Stack<LineSegmentExt> stack = new Stack<>();
        LineSegmentExt seg ;
        int len = points.length;
        for (int i = 0; i < len - 3; i++) {
            Point p = points[i];
            for (int j = i+1; j < len - 2; j++) {
                Point q = points[j];
                for (int k = j+1; k < len - 1; k++) {
                    Point r = points[k];
                    if (isCollinear(p, q, r))
                    {
                        for (int m = k+1; m < len; m++)
                        {
                            Point s = points[m];
                            if (isCollinear(p, q, r, s))
                            {
                                seg = new LineSegmentExt(p, q, r, s);
                                addToStack(seg, stack);
                            }
                        }
                    }
                }
            }
        }
        LineSegment[] segs = new LineSegment[stack.size()];
        int count = 0;
        while (!stack.isEmpty())
        {
            seg = stack.pop();
            segs[count++] = seg.toLineSegment();
        }
        return segs;
    }
    
    private class LineSegmentExt implements Comparable<LineSegmentExt>
    {
        private Point lo;
        private Point hi;
        private double slope;
        
        public LineSegmentExt(Point p, Point q)
        {
            assert !isSamePoint(p,q);
            if (p.compareTo(q) < 0)
            {
                this.lo = p;
                this.hi = q;
            }
            else
            {
                this.lo = q;
                this.hi = p;
            }
            slope = this.lo.slopeTo(this.hi);
        }
        
        public LineSegmentExt(Point p, Point q, Point r)
        {
            this(p,q);
            assert BruteCollinearPoints.isCollinear(p, q, r);
            if (r.compareTo(this.lo) < 0)
                this.lo = r;
            else if (r.compareTo(this.hi) > 0)
                this.hi = r;
        }
        
        public LineSegmentExt(Point p, Point q, Point r, Point s)
        {
            this(p, q, r);
            assert BruteCollinearPoints.isCollinear(p, q, r, s);
            if (s.compareTo(this.lo) < 0)
                this.lo = s;
            else if (s.compareTo(this.hi) > 0)
                this.hi = s;
        }
        
        
        public boolean isCollinear(LineSegmentExt that)
        {
            return BruteCollinearPoints.isCollinear(lo, hi, that.lo, that.hi);
        }
        
        public LineSegmentExt merge(LineSegmentExt that)
        {
            assert isCollinear(that);
            LineSegmentExt new_seg;
            Point new_lo, new_hi;
            new_lo = this.lo.compareTo(that.lo) < 0? this.lo: that.lo;
            new_hi = this.hi.compareTo(that.hi) < 0? that.hi: this.hi;
            return new LineSegmentExt(new_lo, new_hi);
        }
        
        public LineSegment toLineSegment()
        {
            return new LineSegment(this.lo, this.hi);
        }
        
        @Override
        public int compareTo(LineSegmentExt that)
        {
            if (this.slope > that.slope)
                return 1;
            else if (this.slope < that.slope)
                return -1;
            else
                return 0;
        }
        
        @Override
        public String toString()
        {
            return this.lo.toString() + " -> " + this.hi.toString();
        }
    }
    
}

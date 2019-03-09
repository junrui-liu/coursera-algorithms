/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    
    private LineSegment[] segments;
    private final int numberOfSegments;
    
    /**
     * Finds all line segments containing 4 or more points
     * @param points 
     */
    public FastCollinearPoints(Point[] points)
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
     * Return the number of line segments
     * @return number of line segments
     */
    public int numberOfSegments()
    {
        return numberOfSegments;
    }
    
    /**
     * Return a copy of the array of line segments
     * @return a copy of array of line segments
     */
    public LineSegment[] segments()
    {   
        LineSegment[] copy = new LineSegment[this.numberOfSegments];
        System.arraycopy(this.segments, 0, copy, 0, numberOfSegments);
        return copy;
    }

    private static boolean isCollinear(Point p, Point q, Point r)
    {
        double pq = p.slopeTo(q);
        double qr = q.slopeTo(r);
        double pr = p.slopeTo(r);
        if (pq == Double.NEGATIVE_INFINITY ||
            qr == Double.NEGATIVE_INFINITY ||
            pr == Double.NEGATIVE_INFINITY)
        {   return true; }
        else
        {   return pq == qr;    }
    }
    
    private LineSegment makeLineSeg(Point p, Point q, Point r)
    {
        
        assert q.compareTo(r) < 0;      // assumes q < r
        if (p.compareTo(q) < 0)         // p < q < r -> select p and r
            return new LineSegment(p, r);
        else if (p.compareTo(r) > 0)    // q < r < p -> select q and p
            return new LineSegment(q, p);
        else                            // q < p < r -> select q and r
            return new LineSegment(q, r);
    }
    
    private LineSegment[] findSegments(Point[] points)
    {
        if (points.length < 4)
            return new LineSegment[0];
        
        int N = points.length;
        Arrays.sort(points);    // first sort by natural order
        
        Point[] temp = new Point[N];  // 
        List<LineSegment> list = new ArrayList<>();
        for (int i = 0; i < N; i++)
        {
            Point pi = points[i];
            
            /* TEMP is sorted by slope over natural order */
            System.arraycopy(points, 0, temp, 0, N);
            Arrays.sort(temp, pi.slopeOrder());
            
            /* Create two points: Poing J is at the start of a segment, 
             * and Point K is at the end of it.
             *
             * Assumes that point PI, PJ and PK are distinct.
             */
            int j = temp[0] != pi? 0: 1;
            int k = temp[j+1] != pi? j+1: j+2;
            Point pj = temp[j];
            Point pk;
            
            /* keep a count of # of points in the current segment */
            int count = 2;  // 1 for point PI, 1 for point PJ
            
            for ( ; k < N; k++)    
            {
                pk = temp[k];
                if (pk != pi)
                {
                    if (isCollinear(pi, pj, pk)) count++;    
                    /* Record the current segment if there're at at least
                     * 4 points and PI is the smallest (in natural order).
                     * 
                     * The latter condition guarantees that the recorded
                     * segments are distinct, since all segments have only
                     * two end points, one of which must be smaller than
                     * the other (assuming a segment have non-zero length).
                    */
                    else
                    {
                        if (count >= 4 && pi.compareTo(pj) <= 0) 
                           list.add(makeLineSeg(pi, pj, temp[k-1]));    
                        // record nothing if there're less than 4 pts
                        pj = pk;
                        count = 2;
                    }
                }
                // else if PK is P, do nothing but increment k.
            }
            /* Record the last segment */
            if (count >= 4 && pi.compareTo(pj) <= 0) 
               list.add(makeLineSeg(pi, pj, temp[k-1]));    
        }
        LineSegment[] segs = new LineSegment[list.size()];
        return list.toArray(segs);
    }
}

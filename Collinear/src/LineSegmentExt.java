
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class LineSegmentExt implements Comparable<LineSegmentExt>
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
        assert isCollinear(p, q, r);
        if (r.compareTo(this.lo) < 0)
            this.lo = r;
        else if (r.compareTo(this.hi) > 0)
            this.hi = r;
    }

    public LineSegmentExt(Point p, Point q, Point r, Point s)
    {
        this(p, q, r);
        assert isCollinear(p, q, r, s);
        if (s.compareTo(this.lo) < 0)
            this.lo = s;
        else if (s.compareTo(this.hi) > 0)
            this.hi = s;
    }


    public boolean isCollinear(LineSegmentExt that)
    {
        return isCollinear(lo, hi, that.lo, that.hi);
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

    private static boolean isSamePoint(Point p, Point q)
    {   return p.slopeTo(q) == Double.NEGATIVE_INFINITY; }

    public static boolean isCollinear(Point p, Point q, Point r, Point s)
    {

        return isCollinear(p,q,r) && isCollinear(p,q,s) &&
               isCollinear(p,r,s) && isCollinear(q,r,s);
    }

    public static boolean isCollinear(Point p, Point q, Point r)
    {
        if (isSamePoint(p,q) || isSamePoint(q, r) || isSamePoint(p, r))
            return true;
        else
            return p.slopeOrder().compare(q, r) == 0;
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
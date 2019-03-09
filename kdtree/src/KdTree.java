
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
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
public class KdTree
{
    private static final double SIDE_LENGTH = 0.01;
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

    private static class KdNode<D>
    {
        private D data;
        private final int dimension;
        private KdNode<D> left;
        private KdNode<D> right;

        public KdNode(D data, int dimension)
        {
            this.data = data;
            this.dimension = dimension;
            this.left = null;
            this.right = null;
        }
    }

//    private static final int K = 2;
    private KdNode<Point2D> root;
    private int size;

    /**
     * construct an empty kd tree of points
     */
    public KdTree()
    {
        root = null;
        size = 0;
    }

    /**
     * is the kd tree empty?
     * @return
     */
    public boolean isEmpty()
    {   return size == 0;   }


    public int size()
    {   return size;    }

    public void insert(Point2D p)
    {
        root = insert(p, root, 0);
    }

    private double comp(Point2D p, Point2D q, int dimension)
    {
        double compare;
            switch (dimension)
            {
                case 0: compare = p.x() - q.x(); break;
                case 1: compare = p.y() - q.y(); break;
                default: throw new java.lang.IllegalArgumentException();
            }
        return compare;
    }

    private KdNode<Point2D> insert(Point2D p, KdNode<Point2D> node, int dimension)
    {
        if (node == null)
        {
            size++;
            return new KdNode<>(p, dimension);
        }
        else
        {
            double compare = comp(p, node.data, dimension);
            if (compare < 0)
                node.left = insert(p, node.left, 1 - dimension);
            else if (compare > 0)
                node.right = insert(p, node.right, 1 - dimension);
            else if (comp(p, node.data, 1 - dimension) == 0)
                node.data = p;
            else
                node.right = insert(p, node.right, 1 - dimension);
            return node;
        }
    }

    /**
     * does the kd tree contain point p?
     * @param p
     * @return
     */
    public boolean contains(Point2D p)
    {   return contains(p, root, 0);}

    private boolean contains(Point2D p, KdNode<Point2D> node, int dimension)
    {
        if (node == null)
            return false;
        else
        {

            double compare = comp(p, node.data, dimension);
            if (compare < 0)
                return contains(p, node.left, 1 - dimension);
            else if (compare > 0)
                return contains(p, node.right, 1 - dimension);
            else if (comp(p, node.data, 1 - dimension) == 0)
                return true;
            else
                return contains(p, node.right, 1 - dimension);
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw()
    {   draw(root); }

    private void draw(KdNode<Point2D> node)
    {
        if (node != null)
        {
            draw(node.left);
            drawDiagonalCross(node.data);
//            node.data.draw();
            draw(node.right);
        }
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
        double xmax = rect.xmax();
        double ymin = rect.ymin();
        double ymax = rect.ymax();

        range(root, stack, rect);
        return stack;
    }

    private void range(KdNode<Point2D> node, Stack<Point2D> stack,
            RectHV r)
    {
        if (node != null)
        {
            double x = node.data.x();
            double y = node.data.y();
            double compareMin, compareMax;
            switch (node.dimension)
            {
                case 0:
                    compareMin = x - r.xmin();
                    compareMax = x - r.xmax();
                    break;
                case 1:
                    compareMin = y - r.ymin();
                    compareMax = y - r.ymax();
                    break;
                default:
                    throw new java.lang.IllegalArgumentException();
            }
            if (compareMin < 0)
                range(node.right, stack, r);
            else if (compareMax > 0)
                range(node.left, stack, r);
            else
            {
                if (r.contains(node.data))
                    stack.push(node.data);
                range(node.right, stack, r);
                range(node.left, stack, r);
            }
        }
    }

    /**
     * a nearest neighbor in the kd tree to point p; null if the kd tree is empty
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p)
    {
        return nearest(p, root);
    }

    private Point2D closerTo(Point2D o, Point2D p1, Point2D p2)
    {
        if (p1 == null) return p2;
        else if (p2 == null) return p1;

        double op1 = o.distanceSquaredTo(p1);
        double op2 = o.distanceSquaredTo(p2);
        return (op1 <= op2)? p1: p2;
    }

    private Point2D nearest(Point2D p, KdNode<Point2D> node)
    {
        if (node == null)
            return null;
        else
        {
            Point2D nearestChild, nearestOtherChild;
            double compare = comp(p, node.data, node.dimension);
            if (compare < 0)
            {
                nearestChild = closerTo(p, node.data, nearest(p, node.left));
                if (nearestChild == null)
                    return closerTo(p, node.data, nearest(p, node.right));
                else if (nearestChild.distanceSquaredTo(p) <= Math.pow(comp(node.data, p, node.dimension), 2))
                    return nearestChild;
                else
                {
                    nearestOtherChild = nearest(p, node.right);
                    return closerTo(p, nearestChild, nearestOtherChild);
                }
            }
            else if (compare > 0)
            {
                nearestChild = closerTo(p, node.data, nearest(p, node.right));
                if (nearestChild == null)
                    return closerTo(p, node.data, nearest(p, node.left));
                else if (nearestChild.distanceSquaredTo(p) <= Math.pow(comp(node.data, p, node.dimension), 2))
                    return nearestChild;
                else
                {
                    nearestOtherChild = nearest(p, node.left);
                    return closerTo(p, nearestChild, nearestOtherChild);
                }
            }
            else
            {
                nearestChild = closerTo(p, nearest(p, node.right), nearest(p, node.left));
                return closerTo(p, node.data, nearestChild);
            }
        }
    }
}

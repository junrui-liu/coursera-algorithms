import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class SAPNaive {

    private static class AncestorLength
    {
        private int ancestor;
        private int length;
    }

    private final Digraph G;
    // ancestors of v is represented as having positive distance to v,
    // ancestors of w is represented as having negative distance to w.
//    private final LinearProbingHashST<Integer, Integer> dist;
    private final LinearProbingHashST<Integer, Integer> H;
    private final Queue<Integer> entriesChanged;
    // TO-DO: software cache for length() & ancestor()

    // constructor takes a digraph (not necessarily a DAG)
    public SAPNaive(Digraph G)
    {
        int V = G.V();
        this.G = new Digraph(V);
        // defensive copy of input graph
        for (int v = 0; v < V; v++)
            for (int w: G.adj(v))
                this.G.addEdge(v, w);
//        dist = new LinearProbingHashST<>();
        H = new LinearProbingHashST<>(); 
        entriesChanged = new Queue<>();
    }

    private void cleanEntries()
    {
        for (int v: H.keys())
            H.delete(v);
    }
    
    private void bfsFirstPass(Iterable<Integer> vs)
    {
        Queue<Integer> q = new Queue<>();
        for (int v: vs)
        {
            q.enqueue(v);
            H.put(v, 1);
        }
        while (!q.isEmpty())
        {
            int v = q.dequeue();
            assert H.contains(v);
            for (int adj: G.adj(v))
            {
                if (!H.contains(adj))
                {
                    H.put(adj, H.get(v) + 1);
                    q.enqueue(adj);
                }
            }
        }
//        printH();
        return;
    }
    
    private void printH()
    {
        for (int i: H.keys())
            StdOut.println(String.format("%d: %d", i, H.get(i)));
    }
    
    private static class VertexWithDepth
    {
        private final int v;
        private final int depth;
        
        public VertexWithDepth(int v, int depth)
        {   this.v = v; this.depth = depth; }
    }
    
    private AncestorLength bfsSecondPass(Iterable<Integer> ws)
    {
        AncestorLength al = new AncestorLength();
        Queue<VertexWithDepth> q = new Queue<>();
        
        int championAncestor = -1;
        int championLength = -1;
        for (int w: ws)
        {
            if (H.contains(w))
            {
                championAncestor = w;
                championLength = H.get(w) - 1;
                q.enqueue(new VertexWithDepth(w, 1));
            }
            else
            {
                q.enqueue(new VertexWithDepth(w, 1));
                H.put(w, -1);
            }
        }
        while (!q.isEmpty())
        {
            VertexWithDepth vertexW = q.dequeue();
            int w = vertexW.v;
            int wDepth = vertexW.depth;
            if (championAncestor >= 0 && wDepth > championLength) break;
//            assert H.contains(w);
            for (int adj: G.adj(w))
            {
                if (!H.contains(adj))
                {
                    H.put(adj, H.get(w) - 1);
                    q.enqueue(new VertexWithDepth(adj, wDepth+1));
                }
                else
                {
                    int adjDistance = H.get(adj);
                    if (adjDistance > 0)
                    {
                        int length = wDepth + H.get(adj) - 1;
                        if (championAncestor < 0 || length < championLength)
                        {
                            championLength = length;
                            championAncestor = adj;
                        }
                        q.enqueue(new VertexWithDepth(adj, wDepth+1));
                    }
                }
            }
            int i = 0;
        }
        al.length = championLength;
        al.ancestor = championAncestor;
        cleanEntries();
        return al;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        checkVertex(v);
        checkVertex(w);
        if (v == w) return 0;
        Bag<Integer> vs = new Bag<>();
        vs.add(v);
        Bag<Integer> ws = new Bag<>();
        ws.add(w);
        bfsFirstPass(vs);
        AncestorLength al = bfsSecondPass(ws);
        return al.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        checkVertex(v);
        checkVertex(w);
        if (v == w) return v;
        Bag<Integer> vs = new Bag<>();
        vs.add(v);
        Bag<Integer> ws = new Bag<>();
        ws.add(w);
        bfsFirstPass(vs);
        AncestorLength al = bfsSecondPass(ws);
        return al.ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException();
        SET<Integer> vs = new SET<>();
        for (int vi: v)
        {
            checkVertex(vi);
            vs.add(vi);
        }
        Bag<Integer> ws = new Bag<>();
        for (int wi: w)
        {
            checkVertex(wi);
            if (vs.contains(wi)) return 0;
            ws.add(wi);
        }
        bfsFirstPass(vs);
        AncestorLength al = bfsSecondPass(ws);
        return al.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException();
        SET<Integer> vs = new SET<>();
        for (int vi: v)
        {
            checkVertex(vi);
            vs.add(vi);
        }
        Bag<Integer> ws = new Bag<>();
        for (int wi: w)
        {
            checkVertex(wi);
            if (vs.contains(wi)) return wi;
            ws.add(wi);
        }
        bfsFirstPass(vs);
        AncestorLength al = bfsSecondPass(ws);
        return al.ancestor;
    }

    private void checkVertex(Integer v)
    {
        if (v == null || v < 0 || v >= G.V())
            throw new java.lang.IllegalArgumentException();
    }

    // do unit testing of this class
//    public static void main(String[] args) {
//        String file = "digraph9";
//        String ext = ".txt";
////        In in = new In(args[0]);
//        In in = new In(file + ext);
//        Digraph G = new Digraph(in);
//        SAP sap = new SAP(G);
////        while (!StdIn.isEmpty()) {
////            int v = StdIn.readInt();
////            int w = StdIn.readInt();
////            int length   = sap.length(v, w);
////            int ancestor = sap.ancestor(v, w);
////            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
////        }
//        int v = 0;
//        int w = 1;
//        int length   = sap.length(v, w);
//        int ancestor = sap.ancestor(v, w);
//        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//    }

 }

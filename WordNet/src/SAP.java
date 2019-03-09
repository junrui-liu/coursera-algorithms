import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.LinearProbingHashST;
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
public class SAP {

    private final Digraph G;
    // ancestors of v is represented as having positive distance to v,
    // ancestors of w is represented as having negative distance to w.
    private STArray distanceToV;
    private STArray distanceToW;
    private final SAPCacheSingleCapacity cache;
//    private Queue<Integer> distanceToVChanged;
//    private Queue<Integer> distanceToWChanged;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        int V = G.V();

        // defensive copy of input graph
        this.G = new Digraph(V);
        for (int v = 0; v < V; v++)
            for (int w: G.adj(v))
                this.G.addEdge(v, w);
        // this.G = G;
        cache = new SAPCacheSingleCapacity();
//        dist = new LinearProbingHashLinearProbingHashST<>();
        distanceToV = new STArray(V);
        distanceToW = new STArray(V);
    }

    private void cleanEntries()
    {
        distanceToV = distanceToV.cleanAll();
        distanceToW = distanceToW.cleanAll();
    }

    private void print(LinearProbingHashST<Integer, Integer> dist)
    {
        for (int i: dist.keys())
            StdOut.println(String.format("%d: %d", i, dist.get(i)));
    }

    private void printDistances()
    {
        StdOut.print("Distances to v: ");
        for (int v: distanceToV.keys())
            StdOut.printf("%d: %d\t", v, distanceToV.get(v));
        StdOut.println();

        StdOut.print("Distances to w: ");
        for (int v: distanceToW.keys())
            StdOut.printf("%d: %d\t", v, distanceToW.get(v));
        StdOut.println();
    }

    private void printQueue(Queue<VD> q, boolean current)
    {
        if (!q.isEmpty())
        {
        for (VD vd: q)
            StdOut.printf("%d,\t", vd.id);
        if (current)
            StdOut.print("|");
        StdOut.println();
        for (VD vd: q)
            StdOut.printf("%d,\t", vd.depth);
        if (current)
            StdOut.print("|");
        StdOut.println();
        }
    }

    private void printQueues(Queue<VD> qV, Queue<VD> qW, boolean current)
    {
        StdOut.println("Queue V:");
        printQueue(qV, current);
        StdOut.println("Queue W:");
        printQueue(qW, !current);
    }

    private void printBags(Iterable<Integer> vs, Iterable<Integer> ws)
    {
        StdOut.print("vs: ");
        for (int v: vs)
            StdOut.printf("%d, ", v);
        StdOut.println();
        StdOut.print("ws: ");
        for (int v: ws)
            StdOut.printf("%d, ", v);
        StdOut.println();
    }

    private AncestorLength bfsUni(Iterable<Integer> vs, Iterable<Integer> ws)
    {
        if (vs == null || ws == null)
            throw new java.lang.IllegalArgumentException();
        for (int v: vs)
            checkVertex(v);
        for (int w: ws)
            checkVertex(w);
        return bfs(vs, ws);
    }

    private AncestorLength bfsUni(int v, int w)
    {
        checkVertex(v);
        checkVertex(w);
        if (v == w) return new AncestorLength(v, 0);
        AncestorLength cached;
        if ((cached = cache.query(v, w)) != null)
            return cached;
        Bag<Integer> vs = new Bag<>();
        vs.add(v);
        Bag<Integer> ws = new Bag<>();
        ws.add(w);
        AncestorLength al = bfs(vs, ws);
        cache.addToCache(v, w, al);
        return al;
    }

    // assume vs and ws are disjoint
    private AncestorLength bfs(Iterable<Integer> vs, Iterable<Integer> ws)
    {
        Queue<VD> qV = new Queue<>();
        for (int v: vs)
        {   qV.enqueue(new VD(v, 0)); distanceToV.put(v, 0);    }

        Queue<VD> qW = new Queue<>();
        for (int w: ws)
        {
            if (distanceToV.contains(w))
            {
                cleanEntries();
                return new AncestorLength(w, 0);
            }
            else
            {
                qW.enqueue(new VD(w, 0));
                distanceToW.put(w, 0);
            }
        }
        int championVertex = -1;
        int championLength = -1;

        // process both queues in lockstep. If switch == true: process qV, else: qW
        boolean current = true;
        boolean previousComplete = false;
        boolean currentComplete;

        Queue<VD> q;
        STArray dist;
        STArray otherDist;
        while (true)
        {
            if (current) {
                q = qV;
                dist = distanceToV;
                otherDist = distanceToW;
            }
            else {
                q = qW;
                dist = distanceToW;
                otherDist = distanceToV;
            }
//            printBags(vs, ws);
//            StdOut.printf("champion: %d, len: %d\n", championVertex, championLength);
//            printQueues(qV, qW, current);
//            printDistances();
//            StdOut.println();
            currentComplete = q.isEmpty();
            if (!currentComplete)
            {
                VD v = q.dequeue();
                currentComplete = championVertex >= 0 && v.depth >= championLength;
                if (!currentComplete) {
                    for (int adj: G.adj(v.id)) {
                        if (!dist.contains(adj))  // not yet visited on the current route
                        {
                            if (!otherDist.contains(adj)) // no overlap yet
                            {
                                dist.put(adj, v.depth + 1);
                                q.enqueue(new VD(adj, v.depth + 1));
                            }
                            else // overlap
                            {
                                int length = v.depth + otherDist.get(adj) + 1;
                                if (championVertex < 0 || length < championLength)
                                {
                                    championVertex = adj;
                                    championLength = length;
                                }
                                q.enqueue(new VD(adj, v.depth + 1));
                            }
//                            q.enqueue(new VD(adj, v.depth + 1));
                        }
                    }
                }
            }
            if (previousComplete && currentComplete) break;
            previousComplete = currentComplete;
            current = !current;
        }
        cleanEntries();
        AncestorLength al = new AncestorLength(championVertex, championLength);
        return al;
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {   return bfsUni(v, w).length; }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {   return bfsUni(v, w).ancestor;   }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {   return bfsUni(v, w).length; }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {   return bfsUni(v, w).ancestor;   }

    private void checkVertex(Integer v)
    {
        if (v == null || v < 0 || v >= G.V())
            throw new java.lang.IllegalArgumentException();
    }
 }

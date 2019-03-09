
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class RootedDAG {
    private final boolean[] marked;
    private final boolean[] onStack;
    private int markedCount;
    private boolean isRootedDAG;
    private int root;
    
    /*
     * Check that the graph is a rooted DAG (allowing multiple roots)
     *   1. Find a root (not necessarily unique) from a random vertex
     *   2. Run DFS on the reverse of G from the root vertex to check that
     *      every vertex is visited once (valid root) and only once 
     *      on the current stack (acyclic).
     */
    public RootedDAG(Digraph G)
    {
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        // assume it is a rooted DAG and try to break this assumption
        isRootedDAG = true;
        root = -1;
        
        for (int v = 0; v < G.V(); v++)
        {
            if (!marked[v]) // not visited
                dfs(G, v);
            if (!isRootedDAG) break;
        }
        
        if (markedCount < G.V())
            isRootedDAG = false;
        if (!isRootedDAG)
            root = -1;
        
//        // find a temporary root from a random vertex
//        int randomVertex = StdRandom.uniform(G.V());
//        final int tempRoot = findRoot(G, randomVertex);
//        if (tempRoot < 0)
//            isRootedDAG = false;
//        else // run DFS from root on G reversed
//        {    
//            dfs(G.reverse(), tempRoot);
//            // if temp root cannot get to every vertex, then it is not the root,
//            // hence the graph is not a rooted DAG
//            if (markedCount < G.V())
//                isRootedDAG = false;
//        }
//        
//        if (!isRootedDAG)
//            root = -1;
//        else
//            root = tempRoot;
    }
    
    private void dfs(Digraph G, int v)
    {
        onStack[v] = true;
        marked[v] = true;
        markedCount++;
        if (G.outdegree(v) == 0)
        {
            if (root < 0) root = v;
            else if (root != v) isRootedDAG = false;
        }
        for (int w: G.adj(v))
        {
            // do not search further if a cycle was detected on previous call which has returned
            if (!isRootedDAG) return;
            // recursively dfs unmarked neighbors
            else if (!marked[w]) dfs(G, w);
            // a cycle is detected if we've visited w on the current path
            else if (onStack[w])
                isRootedDAG = false;
            // ignore w if we already visited it but it isn't on the current path
            else {}
        }
        onStack[v] = false;
    }
    
//    private void dfs(Digraph G, int v)
//    {
//        onStack[v] = true;
//        marked[v] = true;
//        markedCount++;
//        for (int w: G.adj(v))
//        {
//            // do not search further if a cycle was detected on previous call which has returned
//            if (!isRootedDAG) return;
//            // recursively dfs unmarked neighbors
//            else if (!marked[w]) dfs(G, w);
//            // a cycle is detected if we've visited w on the current path
//            else if (onStack[w])
//                isRootedDAG = false;
//            // ignore w if we already visited it but it isn't on the current path
//            else {}
//        }
//        onStack[v] = false;
//    }
    
    
    private int findRoot(Digraph G, int v)
    {
        if (marked[v] == true)
            return -1;
        Iterator<Integer> neighbors = G.adj(v).iterator();
        if (!neighbors.hasNext())
            return v;
        else
        {
            marked[v] = true;
            int tempRoot = findRoot(G, neighbors.next());
            marked[v] = false;  // undo the marking before returning the root found
            return tempRoot;
        }
    }
    
    public int getRoot()
    {   return root;    }
    
    public boolean isRootedDAG()
    {   return isRootedDAG; }
    
    public static void main(String[] args)
    {
        final String[] invalidHypernyms = {"hypernyms3InvalidCycle.txt", "hypernyms3InvalidTwoRoots.txt", "hypernyms6InvalidCycle+Path.txt", "hypernyms6InvalidCycle.txt", "hypernyms6InvalidTwoRoots.txt"};
        final String[] specialHypernyms = {"hypernyms6TwoAncestors.txt", "hypernyms8ManyAncestors.txt", "hypernyms8WrongBFS.txt", "hypernymsManyPathsOneAncestor.txt", "hypernyms11AmbiguousAncestor.txt", "hypernyms11ManyPathsOneAncestor.txt", "hypernyms15Path.txt", "hypernyms15Tree.txt"};
        final String[] smallHypernyms = {"hypernyms100-subgraph.txt", "hypernyms10000-subgraph.txt", "hypernyms500-subgraph.txt", "hypernyms50000-subgraph.txt"};
        final String[] hugeHypernyms = {"hypernyms.txt", "hypernyms100K.txt", "hypernyms200K.txt", "hypernyms300K.txt"};
        final String[][] hypernymsFiles = {invalidHypernyms, specialHypernyms, smallHypernyms, hugeHypernyms};
        
        for (String[] hypernymsFile: hypernymsFiles) {
        
            StdOut.println("Testing the set: " + Arrays.toString(hypernymsFile));
            for (String digraphFilename: hypernymsFile){
                In in = new In(digraphFilename);

                Bag<int[]> hypernymsBag = new Bag<>();
                int maxVertex = 0;
                while (in.hasNextLine())
                {
                    String line = in.readLine();
                    String[] verticesStr = line.split(",");

                    int id = Integer.parseInt(verticesStr[0]);
                    int[] vertices = new int[verticesStr.length];
                    for (int j = 0; j < verticesStr.length; j++)
                    {
                        int w = Integer.parseInt(verticesStr[j]);
                        vertices[j] = w;
                        maxVertex = w > maxVertex? w: maxVertex;
                    }
                    hypernymsBag.add(vertices);
                }

                Digraph G = new Digraph(maxVertex + 1);
                for (int[] hypernyms: hypernymsBag)
                {
                    int id = hypernyms[0];
                    for (int i = 1; i < hypernyms.length; i++)
                        G.addEdge(id, hypernyms[i]);
                }

                RootedDAG r = new RootedDAG(G);
                StdOut.println(String.format(
                        "File: %s\nRooted DAG? %b\tRoot at %d", digraphFilename, r.isRootedDAG, r.getRoot()));
            }
            StdOut.println();
        }
    }
}

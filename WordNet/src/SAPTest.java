
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class SAPTest {
    private static final String EXT = ".txt";

    public static void main(String[] args) {
//        String file = "hypernyms500-subgraph";
        String file = "hypernyms300K";
          
//        testAllPairsHypernyms(file);
        testRandomPairHypernyms(file, 10, 1000);
//        testAllPairs(file);
//        testPair(file, 1, 4);
    }
    
    private static void testPair(String file, int v, int w)
    {
        
        In in = new In(file + EXT);
        Digraph G = new Digraph(in);
        testPair(G, v, w);
    }
    
    private static void testPair(Digraph G, int v, int w)
    {
//        SAPNaive sapNaive = new SAPNaive(G);
        SAP sap = new SAP(G);
//        int lengthRef = sapNaive.length(v, w);
//        int ancestorRef = sapNaive.ancestor(v, w);
        int length   = sap.length(v, w);
//        int ancestor = sap.ancestor(v, w);
//        if (lengthRef != length || ancestorRef != ancestor)
//        {
//            StdOut.printf("v = %d, w = %d\n", v, w);
//            StdOut.printf("Reference: length = %d, ancestor = %d\n", lengthRef, ancestorRef);
//            StdOut.printf("Student:   length = %d, ancestor = %d\n", length, ancestor);
//        }
    }
    
    private static void testPair(SAP sap, int v, int w)
    {
        int length   = sap.length(v, w);
//        int ancestor = sap.ancestor(v, w);
    }
    
    private static void testAllPairsHypernyms(String hypernyms)
    {
        Digraph G = initialzeDigraphFromHypernyms(hypernyms);
        testAllPairs(G);
    }
    
    private static void testRandomPairHypernyms(String hypernyms, int n, int k)
    {
        Digraph G = initialzeDigraphFromHypernyms(hypernyms);
        Stopwatch sw = new Stopwatch();
        double[] trials = new double[n];
        
        SAP sap = new SAP(G);
        for (int i = 0; i < n; i++)
        {
            double testSum = 0;
            
            for (int j = 0; j < k; j++)
            {
                int v = StdRandom.uniform(G.V());
                int w = StdRandom.uniform(G.V());
                double start = sw.elapsedTime();
                testPair(sap, v, w);
                double end = sw.elapsedTime();
                testSum += end - start;
            }
            trials[i] = testSum;
//            sap.printCacheCount();
        }
        StdOut.printf("Time elapsed: %.2f seconds, sd: %.2f seconds\n",
                StdStats.mean(trials), StdStats.stddev(trials));
    }
    
    private static Digraph initialzeDigraphFromHypernyms(String hypernyms)
    {
        In in = new In(hypernyms + EXT);
        // put each line of hypernyms into a bag, out of which a graph will be created
        Bag<int[]> hypernymsBag = new Bag<>();
        // keep track of max vertex encountered, to be used in creating the graph
        int maxVertex = 0;
        while (in.hasNextLine())
        {
            String line = in.readLine();
            String[] verticesStr = line.split(",");
            // id is in the zeroth entry
            int id = Integer.parseInt(verticesStr[0]);
            // parse vertices strings to integers
            int[] vertices = new int[verticesStr.length];
            for (int j = 0; j < verticesStr.length; j++)
            {
                int v = Integer.parseInt(verticesStr[j]);
                vertices[j] = v;
                maxVertex = v > maxVertex? v: maxVertex; // update max vertex
            }
            hypernymsBag.add(vertices);
        }
        // take the smaller size, i.e. max vertex + 1, since max vertex < idCount, 
        // and maybe not all id's are present in the vertices
        Digraph G = new Digraph(maxVertex + 1);
        for (int[] hypernymsList: hypernymsBag)
        {
            int id = hypernymsList[0];
            for (int i = 1; i < hypernymsList.length; i++)
                G.addEdge(id, hypernymsList[i]);
        }
        return G;
    }
    
    private static void testAllPairs(Digraph G)
    {
        SAPNaive sapNaive = new SAPNaive(G);
        SAP sap = new SAP(G);
        Stopwatch sw = new Stopwatch();
        for (int v = 0; v < G.V(); v++)
        {
            for (int w = 0; w < G.V(); w++)
            {
                int lengthRef = sapNaive.length(v, w);
                int ancestorRef = sapNaive.ancestor(v, w);
                int length   = sap.length(v, w);
                int ancestor = sap.ancestor(v, w);
                if (lengthRef != length || ancestorRef != ancestor)
                {
                    StdOut.printf("v = %d, w = %d\n", v, w);
                    StdOut.printf("Reference: length = %d, ancestor = %d\n", lengthRef, ancestorRef);
                    StdOut.printf("Student:   length = %d, ancestor = %d\n", length, ancestor);
                }
            }
        }
        double time = sw.elapsedTime();
        StdOut.printf("Time elapsed: %.2f seconds\n", time);
    }
    
    private static void testAllPairs(String file)
    {
        
        String EXT = ".txt";
        In in = new In(file + EXT);
        Digraph G = new Digraph(in);
        testAllPairs(G);
    }
}

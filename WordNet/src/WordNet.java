
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class WordNet {
    private Digraph G;
    private LinearProbingHashST<String,SET<Integer>> WordToIDSet;
//    private LinearProbingHashST<Integer, String> IDToSynset;
    private List<String> IDToSynset;
    private final SAP sap;
    
    /**
     * constructor takes the name of the two input files    
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null)
            throw new java.lang.IllegalArgumentException();
        
        WordToIDSet = new LinearProbingHashST<>();
//        IDToSynset = new LinearProbingHashST<>();
        IDToSynset = new ArrayList<>();
        int idCount = 0;
        
        /* Read synsets. */
        In in = new In(synsets);
        while (in.hasNextLine())
        {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
//            IDToSynset.put(id, synset);
            IDToSynset.add(id, synset);
            
            String[] synsetWords = fields[1].split(" ");
            for (String word: synsetWords)
            {
                SET<Integer> idSet;
                if (WordToIDSet.contains(word))
                {
                    idSet = WordToIDSet.get(word);
                    idSet.add(id);
                }
                else
                {
                    idSet = new SET<>();
                    idSet.add(id);
                    WordToIDSet.put(word, idSet);
                }
            }
            idCount++;  
        }
        
        
        /* Read hypernyms. */
        in = new In(hypernyms);
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
        // throw an error if the max vertex in hypernyms exceeds the max synset id
        // (i.e. more vertices than available)
        if (maxVertex >= idCount)
            throw new java.lang.IllegalArgumentException("More vertices requested than available.");
        // take the smaller size, i.e. max vertex + 1, since max vertex < idCount, 
        // and maybe not all id's are present in the vertices
        G = new Digraph(maxVertex + 1);
        for (int[] hypernymsList: hypernymsBag)
        {
            int id = hypernymsList[0];
            for (int i = 1; i < hypernymsList.length; i++)
                G.addEdge(id, hypernymsList[i]);
        }
        
        /* Check that the hypernyms graph is a rooted DAG. */
        RootedDAG rootedDAG = new RootedDAG(G);
        if (!rootedDAG.isRootedDAG())
            throw new  java.lang.IllegalArgumentException("Invalid rooted DAG.");
        
        this.sap = new SAP(G);
    }
    

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {   return WordToIDSet.keys();    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {   return WordToIDSet.contains(word);    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();
        SET<Integer> idSetA = WordToIDSet.get(nounA);
        SET<Integer> idSetB = WordToIDSet.get(nounB);
        return sap.length(idSetA, idSetB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();
        SET<Integer> idSetA = WordToIDSet.get(nounA);
        SET<Integer> idSetB = WordToIDSet.get(nounB);
        int ancestorID = sap.ancestor(idSetA, idSetB);
        // an ancestor always exists since the graph is a rooted DAG
        return IDToSynset.get(ancestorID);
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        String synsetFilename = "";
        String hypernymFilename = "";
        WordNet wordnet = new WordNet(synsetFilename, hypernymFilename);
        
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            
            int distance = wordnet.distance(nounA, nounB);
            String sap   = wordnet.sap(nounA, nounB);
            StdOut.printf("distance = %d, ancestor = %s\n", distance, sap);
        }
    }
}

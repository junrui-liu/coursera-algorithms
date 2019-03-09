
import edu.princeton.cs.algs4.In;
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
public class Outcast {
    
    private final WordNet wordnet;
    /**
    * constructor takes a WordNet object
    * @param wordnet 
    */
    public Outcast(WordNet wordnet)
    {
        this.wordnet = wordnet;
    }
    
    /**
    * given an array of WordNet nouns, return an outcast
    * @param nouns
    * @return 
    */
    public String outcast(String[] nouns)
    {
        if (nouns == null)
            throw new java.lang.IllegalArgumentException();
        
        int[] distances = new int[nouns.length];
        int maxIndex = 0;
        for (int i = 0; i < nouns.length; i++)
        {
            if (nouns[i] == null)
                throw new java.lang.IllegalArgumentException();
            for (int j = 0; j < nouns.length; j++)
            {
                if (i != j)
                    distances[i] += wordnet.distance(nouns[i], nouns[j]);
            }
            if (distances[i] > distances[maxIndex])
                maxIndex = i;
        }
        return nouns[maxIndex];
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
}
}

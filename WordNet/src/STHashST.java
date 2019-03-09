
import edu.princeton.cs.algs4.LinearProbingHashST;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class STHashST
{
    private LinearProbingHashST<Integer, Integer> ST;

    public STHashST(int V)
    {   ST = new LinearProbingHashST<>();  }

    public void put(int key, int val)
    {   ST.put(key, val);   }

    public boolean contains(int key)
    {   return ST.contains(key);   }

    public int get(int key)
    {   return ST.get(key); }

    public STHashST cleanAll()
    {   return new STHashST(0); }

    public Iterable<Integer> keys()
    {   return ST.keys();   }
}

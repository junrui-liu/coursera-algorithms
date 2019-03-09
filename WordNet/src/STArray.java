
import edu.princeton.cs.algs4.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class STArray
{
    private final int[] dist;
    private Queue<Integer> changed;

    public STArray(int V)
    {   dist = new int[V];  
        changed = new Queue<>();
    }

    public void put(int key, int val)
    {   
        dist[key] = val + 1; 
        changed.enqueue(key);
    }

    public boolean contains(int key)
    {   return dist[key] > 0;   }

    public int get(int key)
    {   return dist[key] - 1; }

    public STArray cleanAll()
    {   
        while (!changed.isEmpty())
            dist[changed.dequeue()] = 0;
        return this;
    }

    public Iterable<Integer> keys()
    {   return changed;   }
}

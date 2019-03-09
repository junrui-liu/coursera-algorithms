
import edu.princeton.cs.algs4.Bag;
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
public class STArrayBacktrack
{
    private final int[] dist;
    private final Stack<VDBacktrack> changed;

    public STArrayBacktrack(int V)
    {   dist = new int[V];  
        changed = new Stack<>();
    }

    public void put(int key, int val)
    {   
        dist[key] = val + 1; 
        changed.push(new VDBacktrack(key, val, -1));
    }
    
    public void put(int key, int val, int back)
    {   
        dist[key] = val + 1; 
        changed.push(new VDBacktrack(key, val, back));
    }

    public boolean contains(int key)
    {   return dist[key] > 0;   }

    public int get(int key)
    {   return dist[key] - 1; }

    public STArrayBacktrack cleanAll()
    {   
        while (!changed.isEmpty())
            dist[changed.pop().id] = 0;
        return this;
    }
    
    public int depthToVertex(int v)
    {
        for (VDBacktrack c: changed)
            if (c.id == v)
                return c.depth;
        return -1;
    }

    public Iterable<VDBacktrack> keys()
    {   return changed;   }
    
    public Iterable<VDBacktrack> floor(int v)
    {
        Bag<VDBacktrack> bag = new Bag<>();
        for (VDBacktrack c: changed)
            if (c.depth <= dist[v])
                bag.add(c);
        return bag;
    }
}

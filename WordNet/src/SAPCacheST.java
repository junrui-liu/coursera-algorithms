import edu.princeton.cs.algs4.ST;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class SAPCacheST implements SAPCache{
    ST<VertexPair, AncestorLength> cache;

    public SAPCacheST()
    {   this.cache = new ST<>();   }
    
    @Override
    public void addToCache(int v, int w, AncestorLength al)
    {
        cache.put(new VertexPair(v, w), al);
    }

    @Override
    public AncestorLength query(int v, int w)
    {
        VertexPair pair = new VertexPair(v, w);
        if (cache.contains(pair))
                return cache.get(pair);
        return null;
    }
    
    public void addToCache(Iterable<Integer> vs, Iterable<Integer> ws, AncestorLength al)
    {
        for (int v: vs)
            for (int w: ws)
                cache.put(new VertexPair(v, w), al);
    }
    
    public AncestorLength query(Iterable<Integer> vs, Iterable<Integer> ws)
    {
        for (int v: vs)
            for (int w: ws)
            {
                VertexPair pair = new VertexPair(v, w);
                if (cache.contains(pair))
                        return cache.get(pair);
            }
        return null;
    }
}

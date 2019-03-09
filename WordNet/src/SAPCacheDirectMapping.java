/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
//public class SAPCacheDirectMapping implements SAPCache{
public class SAPCacheDirectMapping
{   
    private int next;
    private final AncestorLength[] cache;
    private final int[] vCoord;
    private final int[] wCoord;
    
    public SAPCacheDirectMapping(int capacity)
    {
        next = 0;
        cache = new AncestorLength[capacity];
        vCoord = new int[capacity];
        wCoord = new int[capacity];
    }
    
//    @Override
    public void addToCache(int v, int w, AncestorLength al)
    {
        if (next >= cache.length)
            next = 0;
        cache[next] = al;
        vCoord[next] = v;
        wCoord[next] = w;
        next++;
    }
        

//    @Override
    public AncestorLength query(int v, int w)
    {
        for (int i = 0; i < cache.length; i++)
            if (vCoord[i] == v && wCoord[i] == w)
                return cache[i];
        return null;
    }
    
}

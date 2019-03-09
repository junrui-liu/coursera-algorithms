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
public class SAPCacheSingleCapacity
{   
    private AncestorLength al;
    private int v;
    private int w;
    
    public SAPCacheSingleCapacity()
    {
        al = null;
        v = -1;
        w = -1;
    }
    
//    @Override
    public void addToCache(int v, int w, AncestorLength al)
    {
        this.al = al;
        this.v = v;
        this.w = w;
    }
        

//    @Override
    public AncestorLength query(int v, int w)
    {
        if (this.v == v && this.w == w)
            return al;
        return null;
    }
    
}

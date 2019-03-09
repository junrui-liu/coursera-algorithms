/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public interface SAPCache {

    public abstract void addToCache(int v, int w, AncestorLength al);

    public abstract AncestorLength query(int v, int w);

}
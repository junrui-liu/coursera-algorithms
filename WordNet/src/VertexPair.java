/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class VertexPair implements Comparable<VertexPair>{
    public final int v;
    public final int w;
    public VertexPair(int v, int w)
    {   
        this.v = v;
        this.w = w;
    }
    
    @Override
    public int hashCode()
    {   return v * 31 + w;  }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VertexPair other = (VertexPair) obj;
        if (this.v != other.v) {
            return false;
        }
        if (this.w != other.w) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(VertexPair other) {
        int comp = Integer.compare(v, other.v);
        if (comp != 0) return comp;
        else
            return Integer.compare(w, other.w);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jun
 */

public class MergeSort extends Sorting {
    
    /**
     * Merge sort an array of long integers.
     * 
     * @param a An array of long integers
     */
    public static void mergeSort(long[] a)
    {
        int n = a.length;
        long[] aux = new long[n];
        sort(0, n-1, a, aux);
    }
    
    /**
     * Recursively sort the lower half and the upper half of the input array
     * and merge them together.
     * 
     * @param l The lower bound
     * @param hi The upper bound
     * @param a An array of long integers
     * @param aux An auxiliary array the same size as A
     */
    private static void sort(int l, int hi, long[] a, long[] aux)
    {
        if (l >= hi) return;
        int mid = (l + hi) / 2;
        if (l < mid)
            sort(l, mid, a, aux); 
        if (mid+1 < hi)
            sort(mid+1, hi, a, aux);
        merge(l, mid, hi, a, aux);
    }
    
    /**
     * Assuming they are both sorted, merge [L..MID] and [MID+1..HI] of
     * the input array such that [L..HI] becomes sorted.
     * 
     * @param l The lower bound of the lower half, sorted
     * @param mid The upper bound of the lower half, sorted
     * @param hi The upper bound of the upper half, sorted
     * @param a An array of long integers
     * @param aux An auxiliary array the same size as A
     */
    private static void merge(int l, int mid, int hi, long[] a, long[] aux)
    {   // left subarray:  a[l..mid], right subarary: a[mid+1..hi]
        for (int i = l; i <= hi; i++)
            aux[i] = a[i]; // make a copy of a in aux
        int r = mid + 1;   // pointer to the right subarray
        int i = l;         // pointer to the final array
        while (l <= mid && r <= hi)
            a[i++] = (aux[l] <= aux[r]) ? aux[l++] : aux[r++];
        // post-processing, if a subarray is exhausted before the other
        while (l <= mid)
            a[i++] = aux[l++];
        while (r <= hi)
            a[i++] = aux[r++];
    }
}

/**
 *
 * @author jun
 */

import java.util.concurrent.ThreadLocalRandom;

public class QuickSort extends Sorting
{
    /**
     * Quick sort an array of long integers.
     * 
     * @param a An array of long integers
     */
    public static void quickSort(long[] a)
    {   sort(a, 0, a.length-1);  }
    
    /**
     * Recursively quick sort an array between [LO..HI], inclusive
     * 
     * @param a An array of long integers
     * @param lo The lower bound, inclusive
     * @param hi The upper bound, inclusive
     */
    private static void sort(long[] a, int lo, int hi)
    {
        if (lo < hi)
        {
            int p = partition(a, lo, hi);
            sort(a, lo, p-1);
            sort(a, p+1, hi);
        }
    }
    
    /**
     * Partition the input array between LO and HI inclusive, with respect to
     * some random pivot p.
     * 
     * @param a An array of long integers
     * @param lo The lower bound, inclusive
     * @param hi The upper bound, inclusive
     * @return 
     */
    private static int partition(long[] a, int lo, int hi)
    {
        // randomly choose a pivot, but assume the pivot will be A[HI]
        int r = ThreadLocalRandom.current().nextInt(lo, hi+1);
        exch(a, r, hi);
        
        /* 
           <= region   : 0   .. i
           >  region   : i+1 .. j-1
           unprocessed : j   .. hi-1
           pivot       : hi
        */
        int i = lo - 1;
        for (int j = lo; j < hi; j++)
        {
            if (a[j] <= a[hi])
            {
                exch(a, j, i+1);
                i++;
            }
        }
        exch(a, i+1, hi);
        return i+1;
    }
    
    /**
     * Test if a is correctly partitioned with respect to pivot at index p.
     * For testing purpose.
     * 
     * @param a An array of long integers
     * @param p The index of the pivot element
     * @return true iff all elements in A[0..p-1] are no larger than A[p],
     * and all elements in A[p+1..A.length] are strictly larger than A[p].
     */
    private static boolean isPartitioned(long[] a, int p)
    {
        for (int i = 0; i < p; i++)
            if (a[i] > a[p])
                return false;
        for (int i = p+1; i < a.length; i++)
            if (a[i] <= a[p])
                return false;
        return true;
    }   
}

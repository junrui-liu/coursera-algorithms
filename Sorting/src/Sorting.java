/**
 *
 * @author jun
 */

import java.util.Arrays;

/**
 *  A collection of common routines used by sorting algorithms.
 */
public abstract class Sorting
{   
    /**
     * Return true iff the input array is sorted in non-decreasing order.
     * @param a An array of long integers
     * @return true iff the input array is sorted in non-decreasing order
     */
    public static boolean isSorted(long[] a)
    {
        if (a.length == 0) return true;
        for (int i = 1; i < a.length; i++)
            if (a[i-1] > a[i])
                return false;
        return true;
    }
    
    /**
     * Test if the SORTED array is a permutation of the ORIGINAL array,
     * sorted in non-decreasing order.
     * @param original The original array
     * @param sorted The proposed sorted array
     * @return true iff SORTED is a non-decreasing permutation of ORIGINAL
     */
    public static boolean isSortedPermutationOf(long[] original, long[] sorted)
    {
        Arrays.sort(original);
        if (original.length != sorted.length) return false;
        for (int i = 0; i < original.length; i++)
            if (original[i] != sorted[i])
                return false;
        return true;
    }
    
    /**
     * Swap the element at index I and J of array A. (No bound-checking.)
     * @param a An array of long integer
     * @param i An integer in [0..A.length-1]
     * @param j An integer in [0..A.length-1]
     */
    public static void exch(long[] a, int i, int j)
    {
        long temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    /**
     * Print the standard representation of the input array to standard out.
     * @param a An array of long integers
     */
    public static void print(long[] a)
    {   System.out.println(Arrays.toString(a));   }
}

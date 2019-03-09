/**
 *
 * @author jun
 */

public class InsertionSort extends Sorting
{
    /**
     * Insertion sort the input array
     * @param a An array of long integers
     */
    public static void insertionSort(long[] a)
    {
        for (int i = 0; i < a.length; i++)
            insert(i, a);
        
        // or simply: insertSort(i, 0, a);
    }
    
    /**
     * Insert A[I] to the left sorted region (A[0..I-1])
     * such that A[0..I] also becomes sorted.
     * @param i An index between 0 .. A.length-1, inclusive
     * @param a An array of long integers
     */
    private static void insert(int i, long[] a)
    {
        long v = a[i];
        while (--i >= 0 && v < a[i])
            a[i+1] = a[i]; // shift elements right by one position
        a[i+1] = v;
        
        // or simply: insert(i, 0, a);
    }
        
    /**
     * Insertion sort the input array between LO and HI, inclusive.
     * For testing purpose.
     * @param a An array of long integers
     * @param lo The lower bound
     * @param hi The upper bound
     */
    public static void insertionSort(long[] a, int lo, int hi)
    {
        for (int i = lo; i < hi && i < a.length; i++)
            insert(i, lo, a);
    }
    
    /**
     * Insert A[I] to the left sorted region (A[lo..I-1])
     * such that A[lo..I] also becomes sorted.
     * @param i An index between 0 .. A.length-1, inclusive
     * @param lo The lower bound of the sorted region
     * @param a An array of long integers
     */
    private static void insert(int i, int lo, long[] a)
    {
        long v = a[i];
        while (--i >= lo && v < a[i])
            a[i+1] = a[i]; // shift elements right by one position
        a[i+1] = v;
    }
}

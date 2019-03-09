/**
 *
 * @author jun
 */

public class HeapSort extends Sorting
{
    /**
     * Heap sort an array of long integers in exactly the same fashion
     * as in CLRS, the only difference being the heap is zero-indexed.
     * 
     * @param a An array of long integers
     */
    public static void heapSort(long[] a)
    {
        buildMaxHeap(a);
        int size = a.length;
        for (int i = a.length - 1; i >= 1; i--)
        {
            exch(a, i, 0);
            size--;
            maxHeapify(0, size, a);
        }
    }
    
    /**
     * Helper function that computes the index of the left child of I.
     * 
     * @param i An index in a zero-indexed heap.
     * @return the index of the left child of I
     */
    private static int left(int i)
    {   return i*2+1;    }
    
    /**
     * Helper function that computes the index of the right child of I.
     * 
     * @param i An index in a zero-indexed heap.
     * @return the index of the right child of I
     */
    private static int right(int i)
    {   return i*2+2;  }
    
    /**
     * Helper function that computes the index of the parent of I.
     * 
     * @param i An index in a zero-indexed heap.
     * @return the index of the parent of I
     */
    private static int parent(int i)
    {   return (i+1)/2-1;    }
    
    /**
     * Build a max heap rooted at 0 from the input array. Exactly the same
     * as in CLRS.
     * 
     * @param a An array of long integers
     */
    private static void buildMaxHeap(long[] a)
    {
        int n = a.length;
        int m = (int) Math.floor(a.length/2);
        for (int i = m-1; i >= 0; i--)
            maxHeapify(i, n, a);
    }
    
    /**
     * Assuming that A[I..SIZE] is a max heap rooted at I except that the root
     * element is misplaced, the procedure restores the heap order 
     * by iteratively sinking down the root element.
     * 
     * Implement an iterative version of the procedure in CLRS (Exercise 6.2-5).
     * Taking inspiration from Exercise 6.5-6, eliminate the exchange statement
     * in line 10 (p. 154) which would require 3 array assignments at each level
     * (now down to 1 array assignment).
     * 
     * @param i The root of the heap
     * @param size The upper bound of the heap
     * @param a An array of long integers
     */
    private static void maxHeapify(int i, int size, long[] a)
    {
        int l, r, largest; // variables to store indices
        long largest_v;    // value of the largest of a[i], left & right child
        long v = a[i];     // stores the value of a[i]
        while (true)
        {
            // -1 to signal the heap order is maintained
            largest = -1;
            largest_v = v;
            
            l = left(i);
            if (l < size && a[l] > v) // update largest & largest_v if necessary
            {
                largest = l;
                largest_v = a[l];
            }
            
            r = right(i);
            if (r < size && a[r] > largest_v) // update largest if necessary
                largest = r;
            
            if (largest < 0) // break the loop if the heap order is not violated
                break;
            
            // else float the larger child up by one level to sink v
            a[i] = a[largest];
            i = largest;
        }  
        a[i] = v; // fill in the original a[i] at the current node position
    }
}

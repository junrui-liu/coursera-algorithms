/**
 *
 * @author jun
 */

import java.lang.management.ThreadMXBean;      // for CPU timing
import java.lang.management.ManagementFactory; // for CPU timing
import java.util.concurrent.ThreadLocalRandom; // for random longs


public class Main {
    /* Constants */
    private final static int N_TRIALS = 10000;
    private final static int QUICK = 0;
    private final static int MERGE = 1;
    private final static int HEAP  = 2;
    private final static int INSERT= 3;
    private final static int[] SORTS = {QUICK, MERGE, HEAP, INSERT};
    private final static boolean[] FLAGS={true, true, true, true};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {   standardTests(13);   }
    
    /**
     * randLong
     * Return random long uniformly distributed between 0 and Long.MAX_VALUE
     * @return A random long integer
     */
    private static long randLong()
    {   return ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);    }
    
    /**
     * standardTests
     * Perform benchmark tests as specified in the assignment
     * @param pow 
     */
    private static void standardTests(int pow)
    {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        
        // test arrays are of sizes 2^p for p >= 0
        int size = 1;
        for (int p = 0; p < pow; p++, size *= 2)
        {
            // accumulative timing of each sorting algorithm
            // Java does the zeroing for us
            double[] timings = new double[SORTS.length];
            
            // time elapsed (ratio) compared to quick sort
            double[] compare = new double[SORTS.length];
            
            // hold the copies of the original array
            long[][] copies = new long[SORTS.length][size];
            
            // run N trials for each array size 2^p
            for (int trial = 0; trial < N_TRIALS; trial++)
            {
                // randomize the original array
                long[] original = new long[size];
                for (int i = 0; i < size; i++)
                    original[i] = randLong();
                
                // benchmark each sort
                for (int sort: SORTS)
                    // run the benchmark iff the flag is set
                    if (FLAGS[sort])
                    {
                        // copy from the original array
                        System.arraycopy(original, 0, copies[sort], 0, size);
                        
                        // start timer
                        long start = bean.getCurrentThreadCpuTime();
                        switch(sort)
                        {
                            case INSERT:
                                InsertionSort.insertionSort(copies[sort]);
                                break;
                            case MERGE:
                                MergeSort.mergeSort(copies[sort]);
                                break;
                            case HEAP:
                                HeapSort.heapSort(copies[sort]);
                                break;
                            case QUICK:
                                QuickSort.quickSort(copies[sort]);
                                break;
                        }
                        long end = bean.getCurrentThreadCpuTime();
                        assert(Sorting.isSortedPermutationOf(original, copies[sort]));
                        timings[sort] += (end - start);
                    }
            }
            
            // average across the trials
            for (int sort: SORTS)
            {
                timings[sort] /= N_TRIALS;
                // compare w/ quickSort
                compare[sort] = timings[sort]/timings[QUICK]; 
            }
            
            // Print benchmark result for each array size
            System.out.println("Array size: 2^" + p);
            if (FLAGS[QUICK]) System.out.print(String.format("Quick:  (%.2fx) %10.0fns | ", compare[QUICK], timings[QUICK]));
            if (FLAGS[HEAP])  System.out.print(String.format("Heap:   (%.2fx) %10.0fns | ", compare[HEAP],  timings[HEAP]));
            if (FLAGS[MERGE]) System.out.print(String.format("Merge:  (%.2fx) %10.0fns | ", compare[MERGE], timings[MERGE]));
            if (FLAGS[INSERT])System.out.print(String.format("Insert: (%.2fx) %10.0fns", compare[INSERT],timings[INSERT]));
            System.out.println();
        }
    }
    
    /**
     * testN
     * Test a sorting algorithm with a random input array of size N.
     * @param n Array size
     */
    private static void testN(int n)
    {
        long[] original = new long[n];
        long[] copy = new long[n];
        for (int i = 0; i < original.length; i++)
            original[i] = randLong();
        System.arraycopy(original, 0, copy, 0, n);
        // Put a sort here
        assert (!Sorting.isSortedPermutationOf(original, copy));
    }
    
    /**
     * testFrom1ToN
     * Test a sorting algorithm with random input arrays of sizes [1..n-1]
     * @param n The maximum array size
     */
    private static void testFrom1ToN(int n)
    {
        for (int i = 0; i < n; i++)
            testN(i);
    }
}

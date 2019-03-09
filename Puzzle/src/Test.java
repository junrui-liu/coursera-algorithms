
import edu.princeton.cs.algs4.StdOut;
import java.io.File;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class Test {
    public static void main(String[] args)
    {
        //testCoding();
        testBoardNaive();
//        testLehmer();
//        testShuffle();
//        testFac();
    }
    
    private static void testFac()
    {
        for (int i = 0; i < 30; i++)
            StdOut.println(fac(i) + "L, " + "// " + i);
    }
    
    private static void testBoardNaive()
    {
        String filepath = "/Users/jun/Downloads/cs/AL/W4/8puzzle/";
        String fileext = ".txt";
        String filename = "puzzle02";
        
        try
        {
            Scanner sc = new Scanner(new File(filepath + filename + fileext));
            int N = sc.nextInt();
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    blocks[i][j] = sc.nextInt();
                }
            }
            
            Board b = new Board(blocks);
//            SmallBoard b = new SmallBoard(blocks);
            StdOut.println(b);
//            for (int n: b.lehmer())
//                StdOut.println(n);
            for (Board neighbor: b.neighbors())
                StdOut.println(neighbor);
            
            Board twin = b.twin();
            for (Board neighbor: twin.neighbors())
                StdOut.println(neighbor);
            
//            
            sc.close();
        }
        catch (java.io.FileNotFoundException e)
        {
            
        }
    }
    
    private static long fac(int i)
    {
        long product = 1;
        while (i > 1)
            product *= i--;
        return product;
    }
    
    private static long codeToSum(int[] arr)
    {
        int len = arr.length;
        int i = len;
        int sum = 0;
        while (i >= 2)
        {
            sum += arr[len-i] * fac(i-1);
            i--;
        }
        return sum;   
    }
    
    private static void testLehmer()
    {
        long tests = (long) Math.pow(2, 8);
        for (long i = 0; i < tests; i++)
        {
            int[] code = Factoradic.sumToPerm(i);
//            int[] codel = Factoradic.sumToPerm(i, code.length);
            int[] plainLehmer = Lehmer.decode(code);
            int[] code2Lehmer = Lehmer.encode(plainLehmer);
            int[] plainShuffle = Shuffle.decode(code);
            int[] code2Shuffle = Shuffle.encode(plainShuffle);
            long i2 = Factoradic.permToSum(code2Lehmer);
            
            StdOut.println(i + " -> " +
//                    str(code) + "/" +
//                    str(codel) + " -> " +
                    str(plainLehmer) + "/" +
                    str(plainShuffle) + " -> " + 
                    str(code2Shuffle) + " -> " + i2);
        }
    }
//    
    private static void testCoding()
    {
        int[] code = {8, 7, 6, 5, 4, 3, 2, 1, 0};
        StdOut.println(codeToSum(code));
        
    }
    
    private static String str(int[] arr)
    {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (int i: arr)
        {
            s.append(i);
            s.append(", ");
        }
        s.append("]");
        return s.toString();
    }
    
//    private static void testShuffle()
//    {
////        long seed = StdRandom.getSeed();
////        StdOut.println(seed);
//        StdRandom.setSeed(1531297453280L);
//        
//        int[] arr = {0, 1, 2, 3, 4};
//        StdOut.print(str(arr) + " -> ");
//        
//        int[] keys = Shuffle.shuffleAndReturnKeys(arr);
//        int[] keys2 = Shuffle.encode(arr);
//        StdOut.println(str(arr) + ": " + str(keys));
//        StdOut.println("Inverse: " + str(keys2));
//        
//        int[] arr2 = Shuffle.decode(keys);
//        StdOut.println(str(arr2));
//    }
}

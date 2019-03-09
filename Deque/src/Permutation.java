/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
public class Permutation {
    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);
        int count = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            if (count < k)
            {
                rq.enqueue(s);
            }
            else
            {
                if (randBoolean(1.0 * k / (count + 1))) 
                                        // count + 1: one is for the new item
                {
                    
                    rq.dequeue();
                    rq.enqueue(s);
                }
            }
            count++;
        }
        
        for (String e: rq)
        {   StdOut.println(e);   }
        
    }
    
    private static boolean randBoolean(double prob)
    {   return StdRandom.uniform() < prob; }
    
//    private static void printStringArray(String[] arr)
//    {
//        StdOut.print("[");
//        for (String s: arr)
//        {
//            StdOut.print(s);
//            StdOut.print(", ");
//        }
//        StdOut.println("]");
//    }
}

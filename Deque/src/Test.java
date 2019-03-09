
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

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
    /**
     * unit testing (optional)
     * @param args 
     */
    public static void main(String[] args)
    {
        //StdRandom.setSeed(1530620543240L);
        StdOut.println(StdRandom.getSeed());
        
        testRandomQueue(20);
    }
    
    private static int[] generateRandomCommands(int numTests)
    {
        int[] commands = new int[numTests];
        int count = 0;
        for (int i = 0; i < numTests; i++)
        {
            if (count < 1) commands[i] = 0;
            else commands[i] = StdRandom.uniform(3);
            if (commands[i] == 0) count++;
            if (commands[i] == 1) count--;
        }
        return commands;
    }
    
    private static void testRandomQueue(int numTests)
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        int count = 0;
        int[] commands = generateRandomCommands(numTests);
        //int[] commands = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < commands.length; i++)
        {
            int choice = commands[i];
            String msg = "";
            StdOut.println("");
            try {
                switch (choice)
                {
                    case 0:
                        msg = String.format("enqueue: %d", count);
                        StdOut.print(msg);
                        rq.enqueue(count);
                        count++;
                        break;
                    case 1:
                        if (rq.size() > 0) {
                            msg = "remove randomly: ";
                            StdOut.print(msg);
                            msg += rq.dequeue();
                            count++;
                        } else {
                            msg = "Nothing to remove";
                        }     
                        break;
                    case 2:
                        if (rq.size() > 0) {
                            msg = "sample randomly: ";
                            StdOut.print(msg);
                            msg += rq.sample();
                            count++;
                        } else {
                            msg = "Nothing to remove";
                        }
                        break;
                    default: msg = "Invalid command";
                }
            }
            catch (java.lang.ArrayIndexOutOfBoundsException e)
            {
                StdOut.println("-----Error: Array Index Out Of Bounds-----");
                StdOut.println("Operation: " + choice + msg);
                String state = String.format("empty? %b\t size: %d",
                    rq.isEmpty(), rq.size());
                StdOut.println("State: " + state);
                StdOut.println("Deque: " + rq);
                throw e;
            }
            catch (java.lang.NullPointerException e)
            {
                StdOut.println("-----Error: Null Pointer-----");
                StdOut.println("Operation: " + choice + msg);
                String state = String.format("empty? %b\t size: %d",
                    rq.isEmpty(), rq.size());
                StdOut.println("State: " + state);
                StdOut.println("Deque: " + rq);
            }
            catch (java.util.NoSuchElementException e)
            {
                StdOut.println("-----Error: No Such Element-----");
                StdOut.println("Operation: " + choice + msg);
                String state = String.format("empty? %b\t size: %d",
                    rq.isEmpty(), rq.size());
                StdOut.println("State: " + state);
                StdOut.println("Deque: " + rq);
            }
            StdOut.println("Operation: " + msg);
            String state = String.format("empty? %b\t size: %d",
                    rq.isEmpty(), rq.size());
            StdOut.println("State: " + state);
            StdOut.println("Deque: " + rq);
        }   
    }
    
    private static void testDeque(int numTests)      
    {
        Deque<Integer> deque = new Deque<>();
        int count = 0;
        for (int i = 0; i < numTests; i++)
        {
            int choice = StdRandom.uniform(4);
            String msg = "";
            StdOut.println("");
            try {
            switch (choice)
            {
                case 0:
                    msg = String.format("add to first: %d", count);
                    deque.addFirst(count);
                    count++;
                    break;
                case 1:
                    msg = String.format("add to last: %d", count);
                    deque.addLast(count);
                    count++;
                    break;
                case 2:
                    if (deque.size() > 0) {
                        msg = String.format("remove from first: %d", deque.removeFirst());
                        count++;
                    } else {
                        msg = "Nothing to remove";
                        i--;
                    }     
                    break;
                case 3:
                    if (deque.size() > 0) {
                        msg = String.format("remove from last: %d", deque.removeLast());
                        count++;
                    } else {
                        msg = "Nothing to remove";
                        i--;
                    }
                    break;
                default: msg = "Invalid command";
            }
            }
            catch (java.lang.ArrayIndexOutOfBoundsException e)
            {
                StdOut.println("-----Error: Array Index Out Of Bounds-----");
                StdOut.println("Operation: " + choice + msg);
                String state = String.format("empty? %b\t size: %d",
                    deque.isEmpty(), deque.size());
                StdOut.println("State: " + state);
                StdOut.println("Deque: " + deque);
            }
            catch (java.lang.NullPointerException e)
            {
                StdOut.println("-----Error: Null Pointer-----");
                StdOut.println("Operation: " + choice + msg);
                String state = String.format("empty? %b\t size: %d",
                    deque.isEmpty(), deque.size());
                StdOut.println("State: " + state);
                StdOut.println("Deque: " + deque);
            }
            catch (java.util.NoSuchElementException e)
            {
                StdOut.println("-----Error: No Such Element-----");
                StdOut.println("Operation: " + choice + msg);
                String state = String.format("empty? %b\t size: %d",
                    deque.isEmpty(), deque.size());
                StdOut.println("State: " + state);
                StdOut.println("Deque: " + deque);
            }
            StdOut.println("Operation: " + msg);
            String state = String.format("empty? %b\t size: %d",
                    deque.isEmpty(), deque.size());
            StdOut.println("State: " + state);
            StdOut.println("Deque: " + deque);
            
        }
    }
}

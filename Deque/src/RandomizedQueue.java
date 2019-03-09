
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item>{
    
    private static final int NO_MAX = -1;
    
    private Item[] stack; // stack is implemented using an array 
    private int next;
    
    
    /**
     * construct an empty stack
     */
    public RandomizedQueue()
    {
        stack = (Item[]) new Object[1];
        next = 0;
    }
    
    
    public boolean isEmpty()
    { return next == 0; }
    
    private boolean isFull()
    { return next == stack.length; }
    
    private void resize(int newLen)
    {
        Item[] newStack = (Item[]) new Object[newLen];
        for (int i = 0; i < next; i++) {
            newStack[i] = stack[i];
        }
        stack = newStack;
    }
    
    /**
     * return the number of items on the stack
     * @return number of items on the stack
     */
    public int size() 
    { return next; }
    
    /**
     * add the item to the end
     * @param item the item to be added to the front of the stack
     */
    public void enqueue(Item item)
    {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        if (isFull())
            resize(stack.length * 2);
        stack[next] = item;
        next++;
        
    }
    
    /**
     * remove and return a randomly selected item
     * @return a randomly selected item
     */
    public Item dequeue()
    {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        int randomIndex = StdRandom.uniform(next);
        Item randomItem = stack[randomIndex];
        next--;
        stack[randomIndex] = stack[next];
        stack[next] = null;
        if (next < stack.length / 4)
            resize(stack.length / 2);
        return randomItem;
    }
    
    /**
     * return a randomly selected item
     * @return a randomly selected item
     */
    public Item sample()
    {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        return stack[StdRandom.uniform(next)];
    }
    
    /**
     * return an iterator over items in random order
     * @return 
     */
    @Override
    public Iterator<Item> iterator()
    {
        return new RQIterator(this);
    }
    
//    /**
//     * Return a string representation of the stack
//     * @return string representation of the stack
//     */
//    @Override
//    public String toString()
//    {
//        StringBuilder repr = new StringBuilder("");
//        for (int i = 0; i < stack.length; i++)
//        {
//                if (stack[i] == null)
//                    repr.append("-");
//                else
//                    repr.append(stack[i].toString());
//                repr.append(" ");
//        }
//        // test iterator
//        repr.append(" | ");
//        for (Item i: this)
//        {
//            repr.append(i.toString()).append(" ");
//        }
//        return repr.toString();
//    }
    
    
    private class RQIterator implements Iterator<Item>
    {
        private final RandomizedQueue<Item> rq;
        private final int[] shuffledIndices;
        private int currentIndex;
        
        public RQIterator(RandomizedQueue<Item> rq)
        {
            this.rq = rq;
            shuffledIndices = new int[rq.next];
            for (int i = 0; i < shuffledIndices.length; i++)
                shuffledIndices[i] = i;
            
//            printArray(shuffledIndices);
            StdRandom.shuffle(shuffledIndices);
//            printArray(shuffledIndices);
            
            currentIndex = 0;
        }

        @Override
        public boolean hasNext()
        {   return currentIndex < shuffledIndices.length;   }

        @Override
        public Item next()
        {
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            return rq.stack[shuffledIndices[currentIndex++]];
        }

        @Override
        public void remove()
        {   throw new java.lang.UnsupportedOperationException();    }
        
    }
//    
//        private void printArray(int[] arr)
//        {
//        StdOut.print("[");
//        for (int i: arr)
//        {
//            StdOut.print(i);
//            StdOut.print(", ");
//        }
//        StdOut.println("]");
//        }
//    
}

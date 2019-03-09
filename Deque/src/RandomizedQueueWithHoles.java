/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueueWithHoles<Item> implements Iterable<Item>
//public class RandomizedQueueWithHoles<Item>
{
    private Item[] queue; // queue is implemented using an array 
    private int next;
    private int last;
    private int queueSize;
    private int arrayLen;
    private int maxSize;
    
    /**
     * construct an empty queue with specified max size
     */
    public RandomizedQueueWithHoles(int k)
    {
        queue = (Item[]) new Object[1];
        arrayLen = 1;
        queueSize = 0;
        next = 0;
        last = 0;
        maxSize = k;
    }
    
    /**
     * construct an empty queue
     */
    public RandomizedQueueWithHoles()
    {
        queue = (Item[]) new Object[1];
        arrayLen = 1;
        queueSize = 0;
        next = 0;
        last = 0;
        maxSize = -1;
    }
    
    public boolean isEmpty()
    {   return queueSize == 0; }
    
    private boolean isFull()
    {   return arrayLen == queueSize; }
    
    private boolean isClashed()
    {   return last == next && queueSize > 1 && queueSize < arrayLen; }
    
    private void resize(int newLen)
    {   resize(newLen, 0); }
    
    private void resizeClash(int newLen)
    {   
        resize(newLen, 0); }
    
    private void resize(int newLen, int offset)
    {
        Item[] newQueue = (Item[]) new Object[newLen];
        int oldArrayLen = queue.length;
        int count = 0;
        
        for (int i = last; count < queueSize; i=(i+1)%oldArrayLen) {
            if (queue[i] != null)
            {
                newQueue[count + offset] = queue[i % oldArrayLen];
                count++;
            }
        }
        queue = newQueue;
        arrayLen = newLen;
        next = (count + offset) % newLen;
        last = offset;
    }
    
    /**
     * return the number of items on the queue
     * @return number of items on the queue
     */
    public int size() 
    {   return queueSize; }
    
    /**
     * add the item to the end
     * @param item the item to be added to the end of the queue
     */
    public void enqueue(Item item)
    {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        if (isClashed())
            resizeClash(arrayLen);
        if (isFull())
            resize(arrayLen * 2, 1);
        last = (last - 1 >= 0)? last -1: last -1 + arrayLen;
        queue[last] = item;
        queueSize++;
    }
    
    /**
     * remove and return the item from the front
     * @return the first item in the queue
     */
    public Item dequeue()
    {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        int difference = next - last;
        int distance = (difference > 0)? difference: difference + arrayLen;
        //int randomIndex;
        int randomIndex = (last + StdRandom.uniform(distance)) % arrayLen;
        while (queue[randomIndex] == null)
            randomIndex = randomIndex+1 < arrayLen? randomIndex+1: randomIndex+1-arrayLen;
//        while (true)
//        {
//            randomIndex = (last + StdRandom.uniform(distance)) % arrayLen;
//            if (queue[randomIndex] != null)
//                break;
//        }
        Item randomItem = queue[randomIndex];
        queue[randomIndex] = null;
        queueSize--;
        
        if (queueSize > 0) // Shrink the distance btw LAST and NEXT
        {
            while (queue[last] == null)
                last = (last+1 < arrayLen)? last+1: last+1 - arrayLen;
            while (true)
            {
                int first = (next - 1 >= 0)? next - 1: next - 1 + arrayLen;
                if (queue[first] != null) break;
                next = first;
            }
        }
        else
        {
            next = 0;
            last = 0;
        }    
        
        if (queueSize < arrayLen / 4)
            resize(arrayLen / 2);
        return randomItem;
    }
    
    public Item sample()
    {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        int randomIndex;
        int difference = next - last;
        int distance = (difference > 0)? difference: difference + arrayLen;
        while (true)
        {
            randomIndex = (last + StdRandom.uniform(distance)) % arrayLen;
            if (queue[randomIndex] != null)
                break;
        }
        Item randomItem =  queue[randomIndex];
        return randomItem;
    }
    
    /**
     * return an iterator over items in order from front to end
     * @return 
     */
    @Override
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueWithHolesIterator(this);
    }
    
    private class RandomizedQueueWithHolesIterator implements Iterator<Item>
    {
        private final Item[] arr;
        private int total;
        private int index;
        
        public RandomizedQueueWithHolesIterator(RandomizedQueueWithHoles<Item> rq)
        {
            arr = (Item[]) new Object[rq.queueSize];
            int count = 0;
            for (int i = 0; count < rq.queueSize; i++)
            {
                Item item = rq.queue[i];
                if (item != null)
                {
                    arr[count] = item;
                    count++;
                }
            }
            total = count;
            index = 0;
            StdRandom.shuffle(arr);
        }

        @Override
        public boolean hasNext()
        {   return index < queueSize; }

        @Override
        public Item next()
        {   
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            Item i = arr[index];
            index++;
            return i;
        }

        @Override
        public void remove()
        {   throw new java.lang.UnsupportedOperationException(); }
        
    }
    
//    /**
//     * Return a string representation of the queue
//     * @return string representation of the queue
//     */
//    @Override
//    public String toString()
//    {
//        StringBuilder repr = new StringBuilder("");
//        for (int i = 0; i < arrayLen; i++)
//        {
//                if (i == next) repr.append(">");
//                if (i == last) repr.append("<");
//                if (queue[i] == null)
//                    repr.append("-");
//                else
//                    repr.append(queue[i].toString());
//                repr.append(" ");
//        }
//        repr.append(" | ");
//        for (Item i: this)
//        {
//            repr.append(i.toString()).append(" ");
//        }
//        return repr.toString();
//    }
    
}
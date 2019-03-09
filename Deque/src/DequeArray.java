/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */

import java.util.Iterator;

public class DequeArray<Item> implements Iterable<Item>
{
    private Item[] deque; // deque is implemented using an array 
    private int next;
    private int last;
    private int dequeSize;
    private int arrayLen;
    /**
     * construct an empty deque
     */
    public DequeArray()
    {
        deque = (Item[]) new Object[1];
        arrayLen = 1;
        dequeSize = 0;
        next = 0;
        last = 0;
    }
    
    public boolean isEmpty()
    { return dequeSize == 0; }
    
    private boolean isFull()
    { return arrayLen == dequeSize; }
    
    private void resize(int newLen)
    {
        Item[] newDeque = (Item[]) new Object[newLen];
        int oldArrayLen = deque.length;
        int newNext = (last + dequeSize) % newLen;
        // TODO: Iterator here
        for (int i = last; i < newNext; i++) {
            newDeque[i % newLen] = deque[i % oldArrayLen];
        }
        deque = newDeque;
        arrayLen = newLen;
        next = newNext;
    }
    
    /**
     * return the number of items on the deque
     * @return number of items on the deque
     */
    public int size() 
    { return dequeSize; }
    
    /**
     * add the item to the front
     * @param item the item to be added to the front of the deque
     */
    public void addFirst(Item item)
    {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        if (isFull()) resize(arrayLen * 2);
        deque[next] = item;
        next = (++next < arrayLen)? next: next - arrayLen;
        dequeSize++;
    }
    
    /**
     * add the item to the end
     * @param item the item to be added to the end of the deque
     */
    public void addLast(Item item)
    {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        if (isFull()) resize(arrayLen * 2);
        last = (--last >= 0)? last: last + arrayLen;
        deque[last] = item;
        dequeSize++;
    }
    
    /**
     * remove and return the item from the front
     * @return the first item in the deque
     */
    public Item removeFirst()
    {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        next = (--next >= 0)? next: next + arrayLen;
        Item firstItem = deque[next];
        deque[next] = null;
        dequeSize--;
        if (dequeSize < arrayLen / 4) resize(arrayLen / 2);
        return firstItem;
    }
    
    /**
     * remove and return the item from the end
     * @return the last item in the deque
     */
    public Item removeLast()
    {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        Item lastItem = deque[last];
        deque[last] = null;
        last = (++last < arrayLen)? last: last - arrayLen;
        dequeSize--;
        if (dequeSize < arrayLen / 4)
            resize(arrayLen / 2);
        return lastItem;
    }
    
    /**
     * return an iterator over items in order from front to end
     * @return 
     */
    // public Iterator<Item> iterator()
    
    /**
     * Return a string representation of the deque
     * @return string representation of the deque
     */
    @Override
    public String toString()
    {
        StringBuilder repr = new StringBuilder("");
        // TODO: Iterator
        for (int i = 0; i < arrayLen; i++)
        {
                if (i == next) repr.append(">");
                if (i == last) repr.append("<");
                if (deque[i] == null)
                    repr.append("-");
                else
                    repr.append(deque[i].toString());
                repr.append(" ");
        }
        repr.append(" | ");
        for (Item i: this)
        {
            repr.append(i.toString()).append(" ");
        }
        return repr.toString();
    }
    
    @Override
    public Iterator<Item> iterator()
    {
        return new DequeIterator(this);
    }
    
    private class DequeIterator implements Iterator<Item>
    {
        private DequeArray<Item> deque;
        private int pointer;
        private int count;
        
        public DequeIterator(DequeArray<Item> deque)
        {
            this.deque = deque;
            pointer = deque.last;
            count = 0;
        }

        @Override
        public boolean hasNext()
        {   return count < deque.size(); }

        @Override
        public Item next()
        {
            Item thisItem = deque.deque[pointer];
            count++;
            pointer = (++pointer < deque.arrayLen)? pointer:
                                                    pointer - deque.arrayLen;
            return thisItem;
        }

        @Override
        public void remove()
        { throw new java.lang.UnsupportedOperationException(); }
    }
}
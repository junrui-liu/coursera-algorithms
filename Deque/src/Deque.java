
import java.util.Iterator;

/*
 * To change this license header, choose License Firsters in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author jun
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item>
{
    @Override
    public Iterator<Item> iterator()
    {   return new DequeIterator(this); }
    
    private class DequeIterator implements Iterator<Item>
    {
        private final Deque<Item> deque;
        Node2Way<Item> current;
        DequeIterator(Deque<Item> deque)
        {   
            this.deque = deque;
            current = head;
        }
        
        @Override
        public boolean hasNext()
        {   return current != null; }

        @Override
        public Item next()
        {
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            Item currentItem = current.data;
            current = current.next;
            return currentItem;
        }

        @Override
        public void remove()
        {   throw new java.lang.UnsupportedOperationException(); }
        
    }

    private class Node2Way<Item>
    {
        private final Item data;
        private Node2Way<Item> next;
        private Node2Way<Item> prev;
        
        public Node2Way(Item data)
        {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
    
    private Node2Way<Item> head;
    private Node2Way<Item> tail;
    private int size;

    public Deque()
    {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty()
    {   return size == 0; }
    
    public int size()
    {   return size; }

    public void addFirst(Item data)
    {
        if (data == null)
            throw new java.lang.IllegalArgumentException();
        Node2Way<Item> node = new Node2Way<>(data);
        if (head != null)
        {
            node.next = head;
            head.prev = node;
        }
        else { tail = node; }
        head = node;
        size++;
    }

    public void addLast(Item data)
    {
        if (data == null)
            throw new java.lang.IllegalArgumentException();
        Node2Way<Item> node = new Node2Way<>(data);
        if (tail != null)
        {
            node.prev = tail;
            tail.next = node;
        }
        else { head = node; }
        tail = node;
        size++;
    }

    public Item removeFirst()
    {
        Node2Way<Item> node;
        if (head != tail)   // then neither is null, i.e. >= 2 nodes
        {
            node = head;
            head = head.next;
            head.prev = null;
        }
        else
        {
            if (head != null)   // head & tail -> same node
            {
                node = head;
                head = null;
                tail = null;
            }
            else    // head = tail = null
                throw new java.util.NoSuchElementException();
        }
        size--;
        return node.data;
    }

    public Item removeLast()
    {
        Node2Way<Item> node;
        if (head != tail)   // then neither is null, i.e. >= 2 nodes
        {
            node = tail;
            tail = tail.prev;
            tail.next = null;
        }
        else
        {
            if (tail != null)   // head & tail points to the same node
            {
                node = tail;
                head = null;
                tail = null;
            }
            else    // head = tail = null
                throw new java.util.NoSuchElementException();
        }
        size--;
        return node.data;
    }
    
//    @Override
//    public String toString()
//    {
//        StringBuilder s = new StringBuilder("< ");
//        for (Item i: this)
//        {
//            s.append(i.toString()).append(" ");
//        }
//        return s.append(">").toString();
//    }
    
}

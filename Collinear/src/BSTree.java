
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 * @param <D>
 */
public class BSTree<D extends Comparable<D>> implements Iterable<D>{
    
    private final Tree<D> root;
    private int N;
    
    public BSTree()
    {
        root = new Tree<>(null);
        N = 0;
    }
    
    public BSTree(D data)
    {
        root = new Tree<>(data);
        N = 1;
    }
    
    public int size()
    {   return N;   }
    
    public void add(D data)
    {
        if (root.data == null)
        {
            root.data = data;
            N++;
            return;
        }
        Tree<D> current = root;
        while (current != null)
        {
            if (current.data.compareTo(data) == 0)     // duplicated data
                return;
            else if (data.compareTo(current.data) < 0) // data < this node
            {
                if (current.left == null)
                {
                    current.left = new Tree<>(data);
                    N++;
                    return;
                }
                else
                    current = current.left;
            }
            else                                    // data > this node
            {
                if (current.right == null)
                {
                    current.right = new Tree<>(data);
                    N++;
                    return;
                }
                else
                    current = current.right;
                    
            }
        }
    }
    
    public Object[] flatten()
    {
        Object[] arr = new Object[N];
        Stack<Tree<D>> stack = new Stack<>();
        stack.push(root);
        int count = 0;
        Tree<D> top;
        while (count < N)
        {
            top = stack.pop();
            try {
                arr[count++] = top.data;
            }
            catch (java.lang.ArrayIndexOutOfBoundsException e)
            {
                int i = 0;
            }
            if (top.left != null) stack.push(top.left);
            if (top.right != null) stack.push(top.right);
        }
        return arr;
    }

    @Override
    public Iterator<D> iterator()
    {   return new BSTreeIterator(this);    }
    
    private class BSTreeIterator implements Iterator<D>
    {
        private final Object[] arr;
        private int current;
        
        public BSTreeIterator(BSTree<D> tree)
        {
            this.arr = tree.flatten();
            current = 0;
        }
        
        @Override
        public boolean hasNext()
        {   return current < arr.length;    }
        
        @Override
        public D next()
        {   
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            return (D) arr[current++];
        }
        
    }
    
    private class Tree<D>
    {
        private D data;
        private Tree<D> left;
        private Tree<D> right;

        Tree(D data)
        {
            this.data = data;
            left = null;
            right = null;
        }
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Stack<Tree<D>> stack = new Stack<>();
        stack.push(root);
        Tree<D> head;
        while (!stack.isEmpty())
        {
            head = stack.pop();
            sb.append(head.data.toString());
            if (head.left != null) stack.push(head.left);
            if (head.right != null) stack.push(head.right);
        }
        return sb.toString();
    }
}

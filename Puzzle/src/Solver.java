/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;
//import java.io.File;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author jun
 */
public class Solver {
    
    private static final String TEST_FILE = "puzzle4x4-34";
//    private static final String TEST_FILE = "puzzle12";
    private static final String TEST_PATH = "/Users/jun/Downloads/cs/AL/W4-8puzzle/8puzzle-testing/";
    private static final String EXT = ".txt";
    
    private class SearchNode
    {
        private final Board board;
        private final int movesMade;
        private final SearchNode pred;
        private final int hamming;
        private final int manhattan;
        
        public SearchNode(Board board, int movesMade, SearchNode pred)
        {
            this.board = board;
            this.movesMade = movesMade;
            this.pred = pred;
            this.hamming = board.hamming() + movesMade;
            this.manhattan = board.manhattan() + movesMade;
        }
        
        public int moves()
        {   return movesMade;   }
        
        public boolean isGoal()
        {   return board.isGoal();  }
        
        public Iterable<Board> trace()
        {
            Stack<Board> iter = new Stack<>();
            SearchNode current = this;
            while (current != null)
            {
                iter.push(current.board);
                current = current.pred;
            }
            return iter;
        }
        
        public Iterable<Board> neighbors()
        {   return board.neighbors();   }
        
        public boolean equalToPred(Board b)
        {   
            if (pred == null)
                return false;
            else
                return pred.board.equals(b);
        }
        
        public String toString()
        {
            StringBuilder s = new StringBuilder();
            s.append(String.format("Moves: %d\t Hamming: %d\t Manhattan: %d",
                    movesMade, hamming, manhattan));
            s.append("\n");
            s.append(board.toString());
            s.append("\n");
            s.append("Predecessor:\n");
            if (pred == null)
                s.append("null");
            else
                s.append(pred.board.toString());
            return s.toString();
        }
        
        public int compareWith(SearchNode s, Comparator<SearchNode> c)
        {
            return c.compare(this, s);
        }
        
        public Comparator<SearchNode> comparatorHamming()
        {   return new HammingComparator();  }
        
        public Comparator<SearchNode> comparatorManhattan()
        {   return new ManhattanComparator();  }
        
        private class ManhattanComparator implements Comparator<SearchNode>
        {
            @Override
            public int compare(SearchNode o1, SearchNode o2) 
            {
                if (o1.manhattan > o2.manhattan)
                    return 1;
                else if (o1.manhattan < o2.manhattan)
                    return -1;
                else
                    return 0;
            }
        }
        
        private class HammingComparator implements Comparator<SearchNode>
        {
            @Override
            public int compare(SearchNode o1, SearchNode o2) 
            {
                if (o1.hamming > o2.hamming)
                    return 1;
                else if (o1.hamming < o2.hamming)
                    return -1;
                else
                    return 0;
            }
        }
    }
    
    private final SearchNode end;
    private final boolean solvable;
    
    /**
     * find a solution to the initial board (using the A* algorithm)
     * @param initial 
     */
    public Solver(Board initial)
    {
        if (initial == null)
            throw new java.lang.IllegalArgumentException();
        
        SearchNode node;
        node = new SearchNode(initial, 0, null);
        SearchNode twin;
        twin = new SearchNode(initial.twin(), 0, null);
        
        Comparator<SearchNode> c = node.comparatorManhattan(); 
        
        MinPQ<SearchNode> pq = new MinPQ<>(c);
        pq.insert(node);
        
        MinPQ<SearchNode> pqTwin = new MinPQ<>(c);
        pqTwin.insert(twin);
        
        SearchNode neighbor;
        SearchNode neighborTwin;
        while (true)
        {
            
            node = pq.delMin();
            twin = pqTwin.delMin();
//            StdOut.println("------Current------");
//            StdOut.println(node.toString());
//            StdOut.println("------Twin------");
//            StdOut.println(twin.toString());
            if (node.isGoal() || twin.isGoal())
                break;
            for (Board b: node.neighbors())
            {
                if (!node.equalToPred(b))
                {
                    neighbor = new SearchNode(b, node.moves() + 1, node);
                    int compare = node.compareWith(neighbor, c);
//                    if (compare >= 0)
//                    if (node.compareWith(neighbor, c) >= 0)
                        pq.insert(neighbor);
                }
            }
            for (Board b: twin.neighbors())
            {
                if (!twin.equalToPred(b))
                {
                    neighborTwin = new SearchNode(b, twin.moves() + 1, twin);
                    int compare = twin.compareWith(neighborTwin, c);
//                    if (compare >= 0)
//                    if (twin.compareWith(neighborTwin, c) >= 0)
                        pqTwin.insert(neighborTwin);
                }
            }
            int i = 0;
        }
        
        if (node.isGoal())
        {
            end = node;
            solvable = true;
        }
        else
        {
            end = twin;
            solvable = false;
        }
        
    }
    
    /**
     * is the initial board solvable?
     * @return 
     */
    public boolean isSolvable()
    {   return solvable;    }
    
    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * @return 
     */
    public int moves()
    {
        if (!solvable)
            return -1;
        else
            return end.moves();
    }
    
    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * @return 
     */
    public Iterable<Board> solution()
    {
        if (!solvable)
            return null;
        else
            return end.trace();
    }
    
    /**
     * solve a slider puzzle (given below)
     * @param args 
     */
    public static void main(String[] args) {
        
//        Solver solver = testFromScanner(TEST_PATH + TEST_FILE + EXT);
        Solver solver = testFromStdIn(args);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
//    private static Solver testFromScanner(String file)
//    {
//        
//        try
//        {
//            // create initial board from file
//            Scanner sc = new Scanner(new File(file));
//            int N = sc.nextInt();
//            int[][] blocks = new int[N][N];
//            for (int i = 0; i < N; i++)
//                for (int j = 0; j < N; j++)
//                    blocks[i][j] = sc.nextInt();
//            
//            Board initial = new Board(blocks);
//
//            // solve the puzzle
//            Solver solver = new Solver(initial);
//            
//            sc.close();
//            return solver;
//        }
//        catch (java.io.FileNotFoundException e)
//        {   
//            StdOut.println("Not such file.");
//            return null;
//        }
//    }
    
    private static Solver testFromStdIn(String[] args)
    {
                // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        return solver;
    }
}
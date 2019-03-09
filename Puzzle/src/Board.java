/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

/**
 *
 * @author jun
 */

public class Board{
    
    private final BoardAPI b;
    
    public Board(int[][] blocks)
    {
        if (blocks.length <= 4)
            b = new SmallBoard(blocks);
        else
            b = new BigBoard(blocks);
    }
    
    private Board(BoardAPI board)
    {   this.b = board; }
    
    public int dimension()
    {   return b.dimension();   }
    
    public int hamming()
    {   return b.hamming(); }
    
    public int manhattan()
    {   return b.manhattan();   }
    
    public boolean isGoal()
    {   return b.isGoal();  }
    
    public Board twin()
    {   return new Board((BoardAPI) b.twin());    }
    
    public boolean equals(Object y)
    {   
        if (y == null)
            return false;
        else
            return b.equals(((Board) y).b);
    }
            
    public Iterable<Board> neighbors()
    {   return new NeighborIterable(this);   }
    
    @Override
    public String toString()
    {   return b.toString();    }
    
    private class NeighborIterable implements Iterable<Board>
    {
        private Board current;
        
        NeighborIterable(Board board)
        {   current = board;    }
        
        public Iterator<Board> iterator() 
        {   return new NeighborIterator(current);  }
        
    }
    
    private class NeighborIterator implements Iterator<Board>
    {
        private Iterator<BoardAPI> iter;
        
        NeighborIterator(Board board)
        {   iter = board.b.neighbors().iterator();    }

        @Override
        public boolean hasNext()
        {   return iter.hasNext(); }

        @Override
        public Board next()
        {   return new Board(iter.next()); }
    }
}

abstract class BoardAPI<BoardType extends BoardAPI>
{
    private final int dim;
    private final int hamming;
    private final int manhattan;
    private final boolean isGoal;
    protected final int emptyRow;
    protected final int emptyCol;
    
    BoardAPI(int[][] blocks)
    {
        dim =  blocks.length;
        hamming = computeHamming(blocks);
        manhattan = computeManhattan(blocks);
        isGoal = computeIsGoal(blocks);
        
        int row = 0;
        int col = 0;
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (blocks[i][j] == 0)
                {
                    row = i;
                    col = j;
                }
            }
        }
        emptyRow = row;
        emptyCol = col;
    }
    
    BoardAPI(int[] blocks1D)
    {
        dim = (int) Math.sqrt(blocks1D.length);
        hamming = computeHamming(blocks1D);
        manhattan = computeManhattan(blocks1D);
        isGoal = computeIsGoal(blocks1D);
        
                int row = 0;
        int col = 0;
        for (int i = 0; i < dim * dim; i++)
        {
            if (blocks1D[i] == 0)
            {
                row = i / dim;
                col = i % dim;
            }
        }
        emptyRow = row;
        emptyCol = col;
    }
    
    /**
     * Returns board dimension N
     * @return board dimension N
     */ 
    public int dimension()
    {   return dim;   }
    
    private int computeHamming(int[][] blocks)
    {
        int numOutOfPlace = 0;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != (i * dim + j) + 1)
                    numOutOfPlace++;
        return numOutOfPlace;
    }
    
    private int computeHamming(int[] blocks1D)
    {
        int numOutOfPlace = 0;
        for (int i = 0; i < dim * dim; i++)
            if (blocks1D[i] != 0 && blocks1D[i] != i + 1)
                numOutOfPlace++;
        return numOutOfPlace;
    }
    
    /**
     * Hamming priority function. The number of blocks in the wrong position, 
     * plus the number of moves made so far to get to the search node. 
     * @return 
     */
    public int hamming()
    {   return hamming; }
    
    private int computeManhattan(int[][] blocks)
    {
        int expectedRow, expectedCol, block;
        int sum = 0;
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                block = blocks[i][j];
                if (block != 0)
                {
                    expectedRow = (block - 1) / dim;
                    expectedCol = (block - 1) % dim;
                    sum += Math.abs(expectedRow - i) + 
                           Math.abs(expectedCol - j);
                }
            }
        }
        return sum;
    }
    
    private int computeManhattan(int[] blocks1D)
    {
        int currentRow, currentCol, expectedRow, expectedCol, block;
        int sum = 0;
        for (int i = 0; i < dim * dim; i++)
        {
            block = blocks1D[i];
            if (block != 0)
            {
                currentRow = i / dim;
                currentCol = i % dim;
                expectedRow = (block - 1) / dim;
                expectedCol = (block - 1) % dim;
                sum += Math.abs(expectedRow - currentRow) + 
                       Math.abs(expectedCol - currentCol);
            }
        }
        return sum;
    }
    
    /**
     * Manhattan priority function.
     * The sum of the Manhattan distances (sum of the vertical and horizontal 
     * distance) from the blocks to their goal positions, plus the number of
     * moves made so far to get to the search node.
     * @return 
     */
    public int manhattan()
    {   return manhattan;  }
    
    private boolean computeIsGoal(int[][] blocks)
    {
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != i * dim + j + 1)
                    return false;
        return true;
    }
    
    private boolean computeIsGoal(int[] blocks1D)
    {
        for (int i = 0; i < dim * dim; i++)
            if (blocks1D[i] != 0 && blocks1D[i] != i + 1)
                return false;
        return true;
    }
    
    /**
     * Return whether this board the goal board
     * @return 
     */
    public boolean isGoal()
    {   return isGoal;  }
    
    public abstract BoardType computeTwin();
    
    public abstract BoardType twin();
    
    public abstract Iterable<BoardType> neighbors();
    
    public abstract boolean equals(Object y);
        
    protected void exch(int[][] arr, int i1, int j1, int i2, int j2)
    {
        int temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
    }
    
        /**
     *  Return a string representation of this board.
     * @return 
     */
    public abstract String toString();
    
    public String blocksToString(int[][] blocks)
    {
        StringBuilder s = new StringBuilder();
        int dim = this.dimension();
        s.append(dim);
        s.append("\n");
        
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                s.append(String.format("%2d ",  blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}

class SmallBoard extends BoardAPI<SmallBoard>{
    
    private long id;
    private SmallBoard twin;
    private Stack<SmallBoard> neighbors;
    
    /**
     * construct a board from an n-by-n array of blocks
     * (where blocks[i][j] = block in row i, column j)
     * @param blocks 
     */
    public SmallBoard(int[][] blocks)
    {
        super(blocks);
        
        id = getID(blocks);
        twin = null;
        neighbors = null;
    }
    
    private SmallBoard(int[] blocks1D)
    {
        super(blocks1D);
        
        id = getID(blocks1D);
        twin = null;
        neighbors = null;
    }
    
    private static void exch(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private long getID(int[] blocks1D)
    {
        int[] encoded = Shuffle.encode(blocks1D);
        long ID = Factoradic.permToSum(encoded);
        return ID;
    }
    
    private long getID(int[][] blocks)
    {
        int dim = dimension();
        int[] blocks1D = new int[dim * dim];
        for (int row = 0; row < dim; row++)
            System.arraycopy(blocks[row], 0, blocks1D, row * dim, dim);
        return getID(blocks1D);
    }
    
    private int[] getBlocks()
    {
        int dim = dimension();
        int[] perm = Factoradic.sumToPerm(id, dim * dim);
        int[] decoded = Shuffle.decode(perm);
        return decoded;
    }
    
    private int[][] getBlocks2D()
    {
        int dim = dimension();
        int[] decoded = getBlocks();
        int[][] decoded2D = new int[dim][dim];
        for (int row = 0; row < dim; row++)
            System.arraycopy(decoded, row * dim, decoded2D[row], 0, dim);
        return decoded2D;
    }
    
    public SmallBoard computeTwin()
    {
        int[] blocks1D = getBlocks();
        int p = blocks1D[0] == 0? 1: 0;
        int q;
        if (p == 0)
            q = blocks1D[1] == 0? 2: 1;
        else
            q = 2;
        exch(blocks1D, p, q);
        twin = new SmallBoard(blocks1D);
//        twin = null;
//        long twinID = getID(blocks1D);
        exch(blocks1D, p, q);
        return twin;
    }
        
    /**
     * a board that is obtained by exchanging any pair of blocks
     * @return 
     */
    public SmallBoard twin()
    {
        if (twin == null)
            twin = computeTwin();
        return twin;
    }
    
    /**
     * does this board equal y?
     */
    @Override
    public boolean equals(Object y)
    {   return id == ((SmallBoard) y).id;    }
    

    
    /**
     * all neighboring boards
     * @return 
     */
    protected Stack<SmallBoard> computeNeighbors(int[][] blocks2D, int emptyRow, int emptyCol)
    {
        int aboveRow = emptyRow - 1;
        int belowRow = emptyRow + 1;
        int leftCol = emptyCol - 1;
        int rightCol = emptyCol + 1;
        
        Stack<SmallBoard> stack = new Stack<>();
        
        int dim = dimension();
        
        if (aboveRow >= 0 && aboveRow < dim)
        {
            exch(blocks2D, aboveRow, emptyCol, emptyRow, emptyCol);
            SmallBoard aboveNeighbor = new SmallBoard(blocks2D);
            exch(blocks2D, aboveRow, emptyCol, emptyRow, emptyCol);
            stack.push(aboveNeighbor);
        }
        
        if (belowRow >= 0 && belowRow < dim)
        {
            exch(blocks2D, belowRow, emptyCol, emptyRow, emptyCol);
            SmallBoard belowNeighbor = new SmallBoard(blocks2D);
            exch(blocks2D, belowRow, emptyCol, emptyRow, emptyCol);
//            exch(belowNeighbor.blocks, empty, below);
            stack.push(belowNeighbor);
        }
        
        if (leftCol >= 0 && leftCol < dim)
        {
            exch(blocks2D, emptyRow, leftCol, emptyRow, emptyCol);
            SmallBoard leftNeighbor = new SmallBoard(blocks2D);
            exch(blocks2D, emptyRow, leftCol, emptyRow, emptyCol);
//            exch(leftNeighbor.blocks, empty, left);
            stack.push(leftNeighbor);
        }
        
        if (rightCol >= 0 && rightCol < dim)
        {
            exch(blocks2D, emptyRow, rightCol, emptyRow, emptyCol);
            SmallBoard righteighbor = new SmallBoard(blocks2D);
            exch(blocks2D, emptyRow, rightCol, emptyRow, emptyCol);
            stack.push(righteighbor);
        }
        
        return stack;
    }
    
    public Iterable<SmallBoard> neighbors()
    {
        if (neighbors == null)
            neighbors = computeNeighbors(getBlocks2D(), emptyRow, emptyCol);
        return neighbors;
    }

    @Override
    public String toString()
    {   return blocksToString(getBlocks2D());   }
}
    
class BigBoard extends BoardAPI<BigBoard>{
    
    private final int[][] blocks;

    private BigBoard twin;
    private Iterable<BigBoard> neighbors;

    /**
     * Construct a board from an N-by-N array of blocks
     *
     * @param blocks (where blocks[i][j] = block in row i, column j)
     */
    public BigBoard(int[][] blocks)
    {
        
        super(blocks);
        int d = dimension();
        this.blocks = blocks;
    }
    
//    private BigBoard computeTwin(int empty)
    @Override
    public BigBoard computeTwin()
    {
        int dim = dimension();
        int emptyIndex = emptyRow * dimension() + emptyCol;
        int p1 = (emptyIndex + 1) % (dim * dim);
        int p2 = (emptyIndex + 2) % (dim * dim);
        int i1 = p1 / dim;
        int j1 = p1 % dim;
        int i2 = p2 / dim;
        int j2 = p2 % dim;
        exch(this.blocks, i1, j1, i2, j2);
        BigBoard newBigBoard = new BigBoard(this.blocks);
        exch(this.blocks, i1, j1, i2, j2);
        return newBigBoard;
    }
    
    /**
     * Return a board that is obtained by exchanging any pair of blocks
     * @return 
     */
    @Override
    public BigBoard twin()
    {   
        if (twin == null)
            twin = computeTwin();
        return twin;
    }
            
    /**
     * Return whether the input board equals this board
     * @param y
     * @return 
     */
    @Override
    public boolean equals(Object y)
    {
        try 
        {
            BigBoard that = (BigBoard) y;

            if (that.dimension() != this.dimension())
                return false;
//                throw new java.lang.IllegalArgumentException(
//                "Two boards have different dimensions.");
            if (this.hamming() != that.hamming() || this.manhattan() != that.manhattan())
                return false;
            int dim = this.dimension();
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++)
                    if (this.blocks[i][j] != that.blocks[i][j])
                        return false;
            return true;
        }
        catch (java.lang.ClassCastException e)
        {   return false;  }
    }
    
//    private Stack<BigBoard> computeNeighbors(int empty)

    protected Stack<BigBoard>
        computeNeighbors(int[][] blocks2D, int emptyRow, int emptyCol)
    {
        int aboveRow = emptyRow - 1;
        int belowRow = emptyRow + 1;
        int leftCol = emptyCol - 1;
        int rightCol = emptyCol + 1;
        
        Stack<BigBoard> stack = new Stack<>();
        
        int dim = dimension();
        
        if (aboveRow >= 0 && aboveRow < dim)
        {
            exch(blocks2D, aboveRow, emptyCol, emptyRow, emptyCol);
            BigBoard aboveNeighbor = new BigBoard(blocks2D);
            exch(blocks2D, aboveRow, emptyCol, emptyRow, emptyCol);
            stack.push(aboveNeighbor);
        }
        
        if (belowRow >= 0 && belowRow < dim)
        {
            exch(blocks2D, belowRow, emptyCol, emptyRow, emptyCol);
            BigBoard belowNeighbor = new BigBoard(blocks2D);
            exch(blocks2D, belowRow, emptyCol, emptyRow, emptyCol);
//            exch(belowNeighbor.blocks, empty, below);
            stack.push(belowNeighbor);
        }
        
        if (leftCol >= 0 && leftCol < dim)
        {
            exch(blocks2D, emptyRow, leftCol, emptyRow, emptyCol);
            BigBoard leftNeighbor = new BigBoard(blocks2D);
            exch(blocks2D, emptyRow, leftCol, emptyRow, emptyCol);
//            exch(leftNeighbor.blocks, empty, left);
            stack.push(leftNeighbor);
        }
        
        if (rightCol >= 0 && rightCol < dim)
        {
            exch(blocks2D, emptyRow, rightCol, emptyRow, emptyCol);
            BigBoard righteighbor = new BigBoard(blocks2D);
            exch(blocks2D, emptyRow, rightCol, emptyRow, emptyCol);
            stack.push(righteighbor);
        }
        
        return stack;
    }
    
    /**
     * Return all neighboring boards
     * @return 
     */
    @Override
    public Iterable<BigBoard> neighbors()
    {
        if (neighbors == null)
            neighbors = computeNeighbors(this.blocks, emptyRow, emptyCol);
        Stack<BigBoard> stackCopy = new Stack<>();
        for (BigBoard neighbor: this.neighbors)
            stackCopy.push(neighbor);
        return stackCopy;
    }

    @Override
    public String toString()
    {   return blocksToString(this.blocks); }
}
class Shuffle {
    
    public static int[] decode(int[] key)
    {
        
        int n = key.length;
        int[] arr = sequence(n);
        int r;
        for (int i = 0; i < n; i++)
        {
            r = key[i] + i;
            exch(arr, i, r);
        }
        return arr;
    }
    
    private static void shuffle(int[] arr)
    {
        int n = arr.length;
        int r;
        for (int i = 0; i < n; i++)
        {
            r = StdRandom.uniform(i, n);
            exch(arr, i, r);
        }
    }
    
    private static int[] sequence(int n){
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = i;
        return arr;
    }
    
    /**
     * Compute the keys used to shuffle the initial array into the
     * permutation.
     * 
     * From https://github.com/fuzxxl/permcode/blob/master/shuffle.c
     * 
     * @param perm
     * @param initial
     * @return 
     */
    public static int[] encode(int[] perm)
    {
        
        int n = perm.length;
        int[] initial = sequence(n);
        
        int[] inverse = new int[n];
        int[] state = new int[n];
        System.arraycopy(initial, 0, inverse, 0, n);
        System.arraycopy(initial, 0, state, 0, n);
        
        int[] keys = new int[n];
        for (int i = 0; i < n; i++)
        {
            int j = inverse[perm[i]];
            exch(inverse, state[i], state[j]);
            exch(state, i, j);
            keys[i] = j - i;
        }
        return keys;
    }
    
    
    private static int[] shuffleAndReturnKeys(int[] arr)
    {
        int n = arr.length;
        int r;
        int[] keys = new int[n];
        for (int i = 0; i < n; i++)
        {
            r = StdRandom.uniform(i, n);
            exch(arr, i, r);
            keys[i] = r - i;
        }
        return keys;
    }
    
    private static void exch(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}

class Factoradic {
    
    private static long fac(int i)
    {
        assert i <= 20;
        long product = 1;
        while (i > 1)
            product *= i--;
        return product;
    }
    
    public static long permToSum(int[] perm)
    {
        int len = perm.length;
        int i = len;
        long sum = 0;
        while (i >= 2)
        {
            sum += perm[len-i] * fac(i-1);
            i--;
        }
        return sum;    
    }
    
    
    public static int[] sumToPerm(long sum, int n)
    {
        int radix = 2;
        int perm[] = new int[n];
        while (sum > 0)
        {
            int remainder = (int) (sum % radix);
            perm[n - radix] = remainder;
            sum /= radix;
            radix++;
        }
        
        perm[n-1] = 0;
        return perm;
    }
    
        public static int[] sumToPerm(long sum)
    {
        int radix = 2;
        int remainder;
        Stack<Integer> s = new Stack<>();
        while (sum > 0)
        {
            remainder = (int) (sum % radix);
            s.push(remainder);
            sum /= radix;
            radix++;
        }
        
        int n = s.size();
        int code[] = new int[n + 1];
//        int code[] = new int[n];
        int i = 0;
        while (!s.isEmpty())
        {
            code[i++] = s.pop();
        }
        code[n] = 0;
        return code;
    }
}

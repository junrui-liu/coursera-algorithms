/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author jun
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    /* Every non-virtual site has three states: blocked, unfilled, or full,
     * each assigned to a unique byte.
     * If a site is unfilled or full, it is said to be open.
     * Note that BLOCKED is assigned with the value 0, since all sites are 
     * initially blocked, and doing so takes advantage of the fact that 
     * byte arrays are initialized to be all zeros.
     */
    private static final byte BLOCKED = 0;
    private static final byte UNFILLED = 1;
    private static final byte FULL = 2;
    
    /* Every site has up to four neighbor sites. */
    private static final byte LEFT = 0;
    private static final byte RIGHT = 1;
    private static final byte BELOW = 2;
    private static final byte ABOVE = 3;
    private static final byte[] DIRECTIONS = {LEFT, RIGHT, BELOW, ABOVE};
    
    private byte[][] grid;  // the gird that holds the state of all sites
    
    private final WeightedQuickUnionUF UFGrid;  // UF object
    private final int virtualTop;   // index of virtual top in UFGrid
    private final int virtualBot;   // index of virtual bottom in UFGrid
    
    private final int size; // size of the grid
    private int numOpenSites;   // number of open sites

    /**
     * Constructs a Percolation object of size N. Initializes all sites to be
     * BLOCKED, and creates a UF object of size N*N+2, where the extra 2 slots
     * are for the virtual top and virtual bottom.
     * 
     * @param n The length of the grid
     * @throws IllegalArgumentException If N <= 0
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        
        /* create n-by-n UFGrid, with all sites initially BLOCKED = 0. */
        grid = new byte[n][n];
        
        /* create a (n * n + 2) UF object, recording the connected components.
         * The two extra slots are for virtual top with index N * N
         * and virtual bottom with index N * N + 1.
         */
        UFGrid = new WeightedQuickUnionUF(n * n + 2);
        virtualTop = n * n;
        virtualBot = virtualTop + 1;
        
        size = n;   // initialize size to be n
    }

    /**
     * Set the state of a site.
     * @param site The site whose state is to be changed
     * @param state The new state
     */
    private void setGrid(Site site, byte state) {
        grid[site.row() - 1][site.col() - 1] = state;
    }
    
    /**
     * Look up the state of a site.
     * @param site The site whose state is to be changed
     * @return The state of specified site
     */
    private byte getGrid(Site site) {
        return grid[site.row() - 1][site.col() - 1];
    }
    
    /**
     * Check whether the state of a site is the same as the input state.
     * @param site The site whose state is to be changed
     * @return true iff the input state matches the state of the site
     */
    private boolean checkGrid(Site site, byte state) {
        return grid[site.row() - 1][site.col() - 1] == state;
    }
    
    /**
     * Checks whether a given site is open. A site is open IFF its state is
     * either UNFILLED or FULL.
     * @param site A site in the state grid
     * @return true IFF the given site is open.
     */
    private boolean isOpen(Site site) {
        byte state = getGrid(site);
        return state == UNFILLED || state == FULL;
    }

    /** 
     * Checks whether a site is open given its row and column.
     * A site is open IFF is its state is either UNFILLED or FULL.
     * @param row Row of the site
     * @param col Column of the site
     * @return 
     */
    public boolean isOpen(int row, int col) {
        return isOpen(new Site(row, col, size));
    }
    
    /**
     * Checks if a given site is full. A site is full IFF its state is FULL.
     * @param site A site in the state grid
     * @return true IFF the given site is FULL.
     */
    private boolean isFull(Site site) { return checkGrid(site, FULL); }

    /**
     * Checks if a site is full, given its row and column.
     * A site is full IFF its state is FULL.
     * @param row Row of the site
     * @param col Column of the site
     * @return true IFF the given site is FULL.
     */
    public boolean isFull(int row, int col) {
        return isFull(new Site(row, col, size));
    }
    
    /**
     * Union two sites on the UF grid.
     * @param s1 The first site
     * @param s2 The second site
     */
    private void union(Site s1, Site s2) {
        UFGrid.union(s1.oneDim(), s2.oneDim());
    }
    
    /**
     * Union two sites on the UF grid. The first site is a Site object, while
     * the second is located by its index on the UF grid.
     * @param site The first site
     * @param index The index of the second site on the UF grid
     */
    private void union(Site site, int index) {
        UFGrid.union(site.oneDim(), index);
    }
    
    /**
     * Opens a site if it is not already opened, i.e. if it is blocked.
     * When the site is opened, union it with its valid neighbors (up to four),
     * and flood-fill all neighboring sites of itself and its neighbors if the
     * current site is filled by its neighbor(s).
     * @param row
     * @param col 
     * @throws IllegalArgumentException If the site to open is not valid.
     */
    public void open(int row, int col) {
        Site site = new Site(row, col, size); // Note: may not be a valid site
        if (checkGrid(site, BLOCKED)) {
            setGrid(site, UNFILLED); // open the site if it is not open already
            numOpenSites++;
            /* connect left & right neighbors, if any. */
            unionOpenNeighbor(site, LEFT);
            unionOpenNeighbor(site, RIGHT);
            /* If the site is in the top row, connect it with virtual top 
             * and with the open site below it, if any. */
            if (row == 1) {
                union(site, virtualTop);
                unionOpenNeighbor(site, BELOW);
            }
            /* If the site is in the bottom row, connect it with virtual bottom 
             * and with the open site above it, if any. */
            if (row == size) {
                union(site, virtualBot);
                unionOpenNeighbor(site, ABOVE);
            }
            /* If is between the top/bottom row, then connect it with the 
             * open sites above and below it, if any. */
            else {
                unionOpenNeighbor(site, BELOW);
                unionOpenNeighbor(site, ABOVE);
            }
            floodFromSite(site);
        }
    }
    
    /**
     * Union the given site with its open neighbor, specified by direction the
     * neighbor is at relative to the given site.
     * Do nothing if either the site has no such neighbor, i.e. if the neighbor
     * in the specified direction is an invalid site, or if that neighbor is
     * blocked.
     * @param site
     * @param direction 
     */
    private void unionOpenNeighbor(Site site, byte direction) {
        try {
            Site neighbor = getNeighbor(site, direction);
            if (isOpen(neighbor)) union(site, neighbor);
        }
        catch (IllegalArgumentException ex) { }
    }
    
    /**
     * Manufacture a Site object, representing the neighbor of the given site
     * in the given direction.
     * @param site
     * @param direction
     * @return A new Site object representing the neighbor
     * @throws IllegalArgumentException If the site has no neighbor in the
     * specified direction
     */
    private Site getNeighbor(Site site, byte direction) { 
        int nRow, nCol; // row and col of the neighbor
        int row = site.row();
        int col = site.col();
        switch (direction) {
            case LEFT:
                nRow = row;
                nCol = col - 1;
                break;
            case RIGHT:
                nRow = row;
                nCol = col + 1;
                break;
            case BELOW:
                nRow = row + 1;
                nCol = col;
                break;
            case ABOVE:    
                nRow = row - 1;
                nCol = col;
                break;
            default: throw new IllegalArgumentException();
        }
        return new Site(nRow, nCol, size); // might be an invalid site
    }
    
    /**
     * Given a site, checks if its neighbor in the specified direction is in
     * certain state.
     * @param site
     * @param direction
     * @param queryState
     * @param identity true or false, depending on the outer 2-place boolean
     * function, e.g. identity=false if the caller is logical OR; true if AND
     * @return true if the neighbor's state and the query state are the same;
     * return IDENTITY if the neighbor's state does not match the query.
     */
    private boolean isNeighbor(Site site, byte direction, byte queryState,
                               boolean identity) {
        try {
            return checkGrid(getNeighbor(site, direction), queryState);
        }
        catch (IllegalArgumentException e){ return identity; }
    }
    
    /**
     * Given a site, checks if any of its neighbors is in a certain state.
     * @param site
     * @param queryState
     * @return true IFF at least one neighbor is in the query state.
     */
    private boolean isAnyNeighbor(Site site, byte queryState) {
        for (byte direction: DIRECTIONS) {
            if (isNeighbor(site, direction, queryState, false)) return true;
        }
        return false;
    }
    
    /**
     * Helper function of floodNeighborOf
     * @param site The origin site
     * @return nothing
     * @throws StackOverflowError If the size of the grid is too large (>1000)
     */
//    private void floodFromSiteRec(Site site) {
//        if (site.row() == 1 || isAnyNeighbor(site, FULL)){
//            setGrid(site, FULL);
//            if (checkGrid(site, FULL)) {
//                floodNeighborOf(site, LEFT);
//                floodNeighborOf(site, RIGHT);
//                floodNeighborOf(site, BELOW);
//                floodNeighborOf(site, ABOVE);
//            }
//        }
//    }
    
    /**
     * Recursively flood-fill any possible sites from the specified origin.
     * Unused because it causes stack overflow if the recursion is too deep.
     * @param site
     * @param direction 
     * @throws StackOverflowError If the size of the grid is too large (>1000)
     */
//    private void floodNeighborOf(Site site, byte direction) {
//        try {
//            Site neighbor = getNeighbor(site, direction);
//            if (checkGrid(neighbor, UNFILLED)) {
//                setGrid(neighbor, FULL);
//                switch (direction) {
//                    case LEFT:  floodNeighborOf(neighbor, LEFT);
//                                floodNeighborOf(neighbor, BELOW);
//                                floodNeighborOf(neighbor, ABOVE);
//                                break;
//                    case RIGHT: floodNeighborOf(neighbor, RIGHT);
//                                floodNeighborOf(neighbor, BELOW);
//                                floodNeighborOf(neighbor, ABOVE);
//                                break;
//                    case BELOW: floodNeighborOf(neighbor, LEFT);
//                                floodNeighborOf(neighbor, RIGHT);
//                                floodNeighborOf(neighbor, BELOW);
//                                break;
//                    case ABOVE: floodNeighborOf(neighbor, LEFT);
//                                floodNeighborOf(neighbor, RIGHT);
//                                floodNeighborOf(neighbor, ABOVE);
//                                break;
//                    default: throw new IllegalArgumentException();
//                }
//            }
//        } catch (IllegalArgumentException ex) { }
//    }
    
    /**
     * Iterative version of floodFromSiteRec. Uses a stack instead of the 
     * program stack. Slightly less efficient because when flooding a neighbor
     * of site X, the direction from which the flood came from is lost.
     * Hence, need to flood back to X again from its flooded neighbor.
     * @param site
     */
    private void floodFromSite(Site site) {
        /* A site is full if it is in the first row, or if any of its neighbors
         * is full. */
        if (site.row() == 1 || isAnyNeighbor(site, FULL)) {
            setGrid(site, FULL);
            StackLL<Site> stack = new StackLL<>();
            pushNeighbors(site, stack); // push the neighbors onto the stack

            while (!stack.isEmpty()) {
                Site stackTop = stack.pop();
                /* flood the stack top if it's unfilled, and push the neighbors
                 * onto the stack */
                if (checkGrid(stackTop, UNFILLED)) {
                    setGrid(stackTop, FULL);
                    pushNeighbors(stackTop, stack);
                }
            }
        }
    }
    
    /**
     * Push the neighbors, if any, of the given site onto the stack.
     * Ignore any invalid neighbor.
     * @param site
     * @param stack 
     */
    private void pushNeighbors(Site site, StackLL<Site> stack){
        try { Site left = getNeighbor(site, LEFT); stack.push(left); } 
        catch (IllegalArgumentException e) { }

        try { Site right = getNeighbor(site, RIGHT); stack.push(right); } 
        catch (IllegalArgumentException e) { }

        try { Site above = getNeighbor(site, ABOVE); stack.push(above); } 
        catch (IllegalArgumentException e) { }

        try { Site below = getNeighbor(site, BELOW); stack.push(below); } 
        catch (IllegalArgumentException e) { }
    }
    
    /**
     * Return the number of open sites in the system.
     * @return number of open sites
     */
    public int numberOfOpenSites() { return numOpenSites; }

    /**
     * Checks if the system percolates, i.e. if the virtual top is connected
     * to the virtual bottom in the UF grid.
     * @return true IFF the system percolates
     */
    public boolean percolates() {
        return UFGrid.connected(virtualTop, virtualBot);
    }
}

class Site {
    private final int row;
    private final int col;
    private final int oneDim;

    Site(int row, int col, int size) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("Illegal site: " + 
                    row + "," + col);
        }
        this.row = row;
        this.col = col;
        oneDim = (row - 1) * size + col - 1;
    }

    int row() {
        return this.row;
    }

    int col() {
        return this.col;
    }
    
    int oneDim(){
        return oneDim;
    }
}

class LL<D> {
    private class Node<D> {
        final D data;
        Node<D> next;
        
        Node(D data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<D> head;
    
    LL() {
        this.head = null;
    }
    
    LL(D data) {
        this.head = new Node<>(data);
    }
    
    boolean isEmpty() { return head == null; }
    
    void addToHead(D data) {
        Node<D> newHead = new Node<>(data);
        newHead.next = head;
        head = newHead;
    }
    
    D removeFromHead() {
        D headData = head.data;
        head = head.next;
        return headData;
    }
    
    D head() { return head.data; }
}

/* Abstract class is not allowed by the course autograder */
//    interface Stack<D> {
//        abstract public boolean isEmpty();
//        abstract public D peek();
//        abstract public D pop();
//        abstract public void push(D data);
//    }

/* Abstract class is not allowed by the course autograder */
// class StackLL<D> implements Stack<D> {
class StackLL<D> {
    
    private final LL<D> linkedList;
    
    public StackLL() { linkedList = new LL<>(); }
    
    public boolean isEmpty() { return linkedList.isEmpty(); }

    public D peek() { return linkedList.head(); }

    public D pop() { return linkedList.removeFromHead(); }

    public void push(D data) { linkedList.addToHead(data); }
    
}

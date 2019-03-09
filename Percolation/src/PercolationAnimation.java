/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */

import acm.graphics.*;
import acm.program.*;
import java.util.concurrent.TimeUnit;
import java.awt.Color;

public class PercolationAnimation extends GCompound{
    
    private static final int CANVAS_SIZE = 500;
    
    private final int size;
    private final Percolation p;
    private final double blockSize;
    private final GRect[] blocks;
    private int currentFilledRow;
    
    public PercolationAnimation(int n){
        size = n;
        p = new Percolation(n);
        blockSize = CANVAS_SIZE / n;  
        currentFilledRow = 1;
        
        GRect canvas = new GRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
        canvas.setFilled(false);
        canvas.setColor(Color.white);
        add(canvas);
        
        blocks = new GRect[n * n];
        
        for (int i = 0; i < n * n; i++){
            int row = i / n, col = i % n;
            blocks[i] = new GRect(col * blockSize, row * blockSize, blockSize, blockSize);
            blocks[i].setFilled(true);
            blocks[i].setFillColor(Color.black);
            add(blocks[i]);
        }
    }

    private int map2Dto1D(int row, int col) { return (row - 1) * size + col - 1; }

    public void open(int row, int col) {
        if ( !p.isOpen(row, col) ){
            p.open(row, col);
            blocks[map2Dto1D(row, col)].setFillColor(Color.white);
        }   
    }
    
    public boolean isOpen(int row, int col) { return p.isOpen(row, col); }
    
    public boolean isFull(int row, int col) { return p.isFull(row, col); }
    
    public void fillGridFast(){
        for (int row = 1; row <= size; row++){
            for (int col = 1; col <= size; col++){
                if ( isFull(row, col) )
                    blocks[map2Dto1D(row, col)].setFillColor(Color.CYAN);
            }
        }
    }
    
    public void fillGrid() {
        for (int row = 1; row <= size; row++){
            try {
                Thread.sleep( (int) (Math.pow(size - row, 1.1)) );
            }
            catch (InterruptedException ex){
                Thread.currentThread().interrupt();
            }
            for (int col = 1; col <= size; col++){
                if ( isFull(row, col))
                    blocks[map2Dto1D(row, col)].setFillColor(Color.CYAN);
            }
        }
    }
    
    public boolean hasNextRowToFill(){
        return currentFilledRow <= size;
    }
    
    public void fillNextRow() {
        int row = currentFilledRow;
        for (int col = 1; col <= size; col++){
            if ( isFull(row, col) && isOpen(row, col) )
                blocks[map2Dto1D(row, col)].setFillColor(Color.CYAN);
        }
        currentFilledRow++;
    }
    
    public boolean percolates(){
        return p.percolates();
    }

}

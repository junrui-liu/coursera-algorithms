/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */

import acm.program.GraphicsProgram;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class PercolationApp extends GraphicsProgram {

    private final static Random r = new Random();
    private final static Scanner in = new Scanner(System.in);

    public void run() {
        // testDrawingWithRandomOpening(50);
        // testCornerCase2();
        //String filename = "wayne98";
        String filename = "snake101";
        //String filename = "input50";
        testFile("/Users/jun/Downloads/percolation/" + filename + ".txt");
    }

    private void testCornerCase1(){
        PercolationAnimation a = new PercolationAnimation(1);
        add(a);
        a.open(1,1);
        a.fillGrid();
        checkState(a);
    }

    private void testCornerCase2(){
        PercolationAnimation a = new PercolationAnimation(2);
        add(a);
        a.open(1,1);
        a.open(2,2);
        a.fillGrid();
        checkState(a);
    }
    
    private void parseLine(String line, int[] coordinates){
        int row = -1, col = -1;
        final int len = line.length();
        int number = -1;    // -1 for stack bottom symbol
        for (int i = 0; i < len; i++){
            char c = line.charAt(i);
            boolean isDigit = Character.isDigit(c);
            if ( number < 0 && isDigit ) {
                number = Character.getNumericValue(c);
            }
            else if ( number >= 0 && isDigit ) {
                number = number * 10 + Character.getNumericValue(c);
            }
            else if ( number >= 0 && !isDigit) {
                if (row < 0) {
                    row = number;
                    number = -1;
                } else {
                    col = number;
                }
            } else {}
        }
        col = number;
        coordinates[0] = row;
        coordinates[1] = col;      
    }

    private void testFile(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int size = Integer.parseInt(reader.readLine().trim());
            PercolationAnimation a = new PercolationAnimation(size);
            String line;
            while ( (line = reader.readLine()) != null ) {
                int[] coordinates = new int[2];
                parseLine(line, coordinates);
                int row = coordinates[0], col = coordinates[1];
                // System.out.println("Reading site: " + row + ", " + col);
                a.open(row, col);
            }
            add(a);
            //fillRowInteractive(a);
            a.percolates();
            a.fillGridFast();
            checkState(a);
            reader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
    }
    
    private void fillRowInteractive(PercolationAnimation a){
        System.out.println("Type 'q' to quit.");
        while ( !"q".equals(in.nextLine()) && a.hasNextRowToFill() ){
            a.fillNextRow();
        }
    }

    private void checkState(PercolationAnimation a){
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            if (x < 0 || y < 0) break;
            if (x == 0 && y == 0)
                System.out.println("Percolates? " + a.percolates());
            else {
                String s = String.format(
                        "%d,%d: Open? %b Full? %b",
                        x, y,
                        a.isOpen(x, y),
                        a.isFull(x, y));
                System.out.println(s);
            }
        }
    }

    private void testDrawingWithRandomOpening(int gridSize) {
        int n = gridSize;
        int numOpen = (int) (n*n * 0.9);

        PercolationAnimation a = new PercolationAnimation(n);
        add(a);
        for (int i = 0; i < numOpen; i++) {
            int x = r.nextInt(n) + 1, y = r.nextInt(n) + 1;
            a.open(x, y);
        }
        a.fillGrid();
        checkState(a);
    }
    
    public static void main(String[] args) {
        new PercolationApp().start(args);
    }
}

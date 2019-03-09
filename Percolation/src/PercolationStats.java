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
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private double meanCache;
    private double stddevCache;
    private double confidenceLoCache;
    private double confidenceHiCache;
    

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        // perform trials independent experiments on an n-by-n grid
        double[] results = new double[trials];
        int totalSites = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while ( !p.percolates() ) {
                p.open(StdRandom.uniform(n)+1, StdRandom.uniform(n)+1);
            }
            results[i] = 1.0 * p.numberOfOpenSites() / totalSites;
        }
        meanCache = StdStats.mean(results);
        stddevCache = StdStats.stddev(results);
        confidenceLoCache = meanCache - 1.96 * stddevCache / Math.sqrt(trials);
        confidenceHiCache = meanCache + 1.96 * stddevCache / Math.sqrt(trials);
    }

    public double mean() {
        // sample mean of percolation threshold
        return meanCache;
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        return stddevCache;
    }

    public double confidenceLo() {
        // low  endpoint of 95% confidence interval
        return confidenceLoCache;
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        return confidenceHiCache;
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        double mean = stats.mean();
        double stddev = stats.stddev();
        double confidenceLo = stats.confidenceLo();
        double confidenceHi = stats.confidenceHi();

        System.out.println("mean\t = " + mean);
        System.out.println("stddev\t = " + stddev);
        System.out.println("95% confidence interval\t = ["
                + confidenceLo + ", " + confidenceHi + "]");

    }

}

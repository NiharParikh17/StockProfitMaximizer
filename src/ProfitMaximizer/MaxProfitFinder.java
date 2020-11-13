/*
    @author Nihar Parikh
	Objective: Write a program that solves a problem in two different ways
		(1) using brute force and 
		(2) using divide-and-conquer
		and compare the theoretical and actual running times for 
		the two methods of solving the prob
 */

package ProfitMaximizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Responsible to calculate maximum profit buy date and sell date
 */
public class MaxProfitFinder {
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "test.txt";
        StockValue[] data = getStockData(fileName);
        //Brute Force
        long startBruteForce = System.nanoTime();
        MPDays bruteForceValues = findProfitBruteForce(data);
        long endBruteForce = System.nanoTime();
        System.out.println("---------- Brute Force ----------");
        System.out.println("Time Taken: " +
                (endBruteForce-startBruteForce) + " nano seconds");
        System.out.println(bruteForceValues.toString());
        //Divide and Conquer
        long startDivideConquer = System.nanoTime();
        MPDays divideConquerValues = findProfitDivideConquer(data);
        long endDivideConquer = System.nanoTime();
        System.out.println("---------- Divide & Conquer ----------");
        System.out.println("Time Taken: " +
                (endDivideConquer-startDivideConquer) + " nano seconds");
        System.out.println(divideConquerValues.toString());

    }

    /**
     * Read the file and generate an array
        with stock value at given days
     * @param fileName file containing data
     * @return array of stock value on given day
     * @throws FileNotFoundException file not found
     */
    static StockValue[] getStockData(String fileName) throws FileNotFoundException {
        Scanner file = new Scanner(new File(fileName));
        //We assume the file contains correct data
        int totalDays = Integer.parseInt(file.next());
        StockValue[] data = new StockValue[totalDays];
        for (int day=0; day<totalDays; day++)
            data[day] = new StockValue(day, Integer.parseInt(file.next()));
        return data;
    }

    /**
     * Uses brute force to find maximum profit
     * @param data array with all the stock values
     * @return the best day to buy and sell stock for max profit
     */
    static MPDays findProfitBruteForce(StockValue[] data) {
        //Initially, buy date is Day 0 and sell date is Day 1
        // and Profit you make might be negative
        MPDays profitDays = new MPDays(data[0].day, data[1].day,
                data[1].value-data[0].value);
        for (int buy=0; buy<data.length; buy++) //For all buy dates
            for (int sell=buy+1; sell<data.length; sell++) //For all sell dates
                if (data[sell].value-data[buy].value //If you find better profit
                        > profitDays.profit)
                    profitDays.update //If yes, update it to better profit
                            (data[buy].day, data[sell].day, data[sell].value-data[buy].value);
        return profitDays;
    }

    /**
     * Uses divide and conquer technique to find maximum profit
     * @param data array with all the stock values
     * @return the best day to buy and sell stock for max profit
     */
    static MPDays findProfitDivideConquer(StockValue[] data) {
        int n = data.length; //total days
        if (n<=3) { //BASE CASE: If only three days are left
            return findProfitBruteForce(data);
        }

        //Splitting the array into two halves: left and right
        StockValue[] leftData = new StockValue[n/2];
        StockValue[] rightData = new StockValue[n-(n/2)];
        for (int indexL=0; indexL<(n/2); indexL++)
            leftData[indexL] = data[indexL];
        for (int indexR=0; indexR<(n-(n/2)); indexR++)
            rightData[indexR] = data[indexR+(n/2)];

        //Getting maximum profit days from left and right
        MPDays profitDaysLeft = findProfitDivideConquer(leftData);
        MPDays profitDaysRight = findProfitDivideConquer(rightData);

        //Getting profit by (min on left) & (max on right)
        StockValue minStockValL = getMinimum(leftData); //Minimum Stock Value on left
        StockValue maxStockValR = getMaximum(rightData); //Maximum Stock Value on right
        MPDays profitDaysBoth = new MPDays(minStockValL.day, maxStockValR.day,
                (maxStockValR.value-minStockValL.value));

        //Getting best: left or right ?
        MPDays maxProfitEither = (profitDaysLeft.profit >= profitDaysRight.profit) ?
                profitDaysLeft : profitDaysRight;
        //Returning best: (from both sides) or (the one from (left) or (right))
        return (profitDaysBoth.profit > (maxProfitEither.profit)) ?
                profitDaysBoth : maxProfitEither;
    }

    /**
     * Gives StockValue of the minimum value in the array
     * @param data array with stock values
     * @return the minimum value in the given array
     */
    static StockValue getMinimum(StockValue[] data) {
        StockValue minimum = data[0];
        for (StockValue sv: data)
            if (sv.value < minimum.value)
                minimum = sv;
        return minimum;
    }

    /**
     * Gives StockValue of the maximum value in the array
     * @param data array with stock values
     * @return the maximum value in the given array
     */
    static StockValue getMaximum(StockValue[] data) {
        StockValue maximum = data[0];
        for (StockValue sv: data)
            if (sv.value > maximum.value)
                maximum = sv;
        return maximum;
    }

    /**
      * The class is responsible to hold the stock value
        for the given day.
     */
    private static class StockValue {
        private int day, value;

        public StockValue(int day, int value) {
            this.day = day;
            this.value = value;
        }

        public String toString() {
            return ""+ this.day +": " + this.value;
        }
    }

    /**
     * The class is responsible to hold
        the buy date, sell date, and profit made.
     * MP means Maximum Profit
     */
    private static class MPDays {
        private int buyDate, sellDate;
        private int profit;

        public MPDays() {
            this.buyDate = 0;
            this.sellDate = 0;
            this.profit = 0;
        }

        public MPDays(int buyDate, int sellDate,
                      int profit) {
            this.buyDate = buyDate;
            this.sellDate = sellDate;
            this.profit = profit;
        }

        public void update(int buyDate, int sellDate,
                           int profit) {
            this.buyDate = buyDate;
            this.sellDate = sellDate;
            this.profit = profit;
        }

        public String toString() {
            return "Buy Date:   " + this.buyDate +
                 "\nSell Date:  " + this.sellDate +
                 "\nProfit:     " + this.profit;
        }
    }
}
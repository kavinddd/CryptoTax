package com.shopme;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Queue<Transaction> transactions = readTransactionFileToList("crypto_tax.txt");

        Port portfolio = new Port();

        portfolio.setTransactions(transactions);

        Float netProfit = portfolio.calculateRealizedProfit();
        System.out.println(netProfit);

//        // A. buy 2 btc at 100, 000.00
//        Transaction newTransaction;
//        newTransaction = new Transaction('B', "BTC", 100000.00F, 2.0F);
//        transactions.add(newTransaction);
//
//        // B. buy 3 btc at 300, 000.00
//        newTransaction = new Transaction('B', "BTC", 200000.00F, 3.0F);
//        transactions.add(newTransaction);
//
//        // C. sell 3 btc at 180,000.00
//        newTransaction = new Transaction('S', "BTC", 180000.00F, 3.0F);
//        transactions.add(newTransaction);
//



    }
    public static Queue<Transaction> readTransactionFileToList(String file) throws IOException {
        File theFile = new File(file);
        Scanner reader = new Scanner(theFile);

        Queue<Transaction> transactions = new LinkedList<>();

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            String[] dataSeparatedByWhiteSpace = data.split(" ");
            transactions.add(new Transaction(
                                            dataSeparatedByWhiteSpace[0].charAt(0),
                                            dataSeparatedByWhiteSpace[1],
                                            Float.parseFloat(dataSeparatedByWhiteSpace[2]),
                                            Float.parseFloat(dataSeparatedByWhiteSpace[3]))
            );
        }

        return transactions;
    }


}
package com.shopme;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Port {
    Queue<Transaction> transactions;
    public Port() {
        this.transactions = new LinkedList<>();
    }

    public void printTransactions (){
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
    }
    public List<String> getUniqueAsset() {
        List<String> uniqueAssets = transactions.stream()
                .map(Transaction::asset)
                .distinct()
                .collect(Collectors.toList());
        return uniqueAssets;
    }

    private Queue<Transaction> getTransactionsByAssetName(String assetName) {
        return transactions.stream()
                .filter( transaction -> transaction.asset().equals(assetName))
                .collect(Collectors.toCollection(LinkedList::new));
    }
    private Queue<Transaction> getTransactionsByAssetNameAndPosition(String assetName, Character position) {
        return transactions.stream()
                .filter( transaction -> transaction.asset().equals(assetName))
                .filter( transaction -> transaction.position() == position)
                .collect(Collectors.toCollection(LinkedList::new));

    }
    private void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void buy(String asset, float price, float quantity) {
        addTransaction(new Transaction('B', asset, price, quantity));
    }

    public void sell(String asset, float price, float quantity) {

        Queue<Transaction> transactionsCopy = new LinkedList<>(getTransactionsByAssetName(asset));

        Transaction currentTransaction;
        float remainingInventory = 0F;
        float tempQuantity;

        while (!transactionsCopy.isEmpty() || !remainingInventory < 0) {
            currentTransaction = transactionsCopy.poll();
            tempQuantity = currentTransaction.quantity();

            if (currentTransaction.position() == 'S'){
                tempQuantity *= -1;
            }

            remainingInventory += tempQuantity;
        }

        if (remainingInventory < 0) {
            System.out.println("You have not enough inventory");
        }
        else {
            addTransaction(new Transaction('S', asset, price, quantity));
        }
    }

    public float calculateRealizedProfit() {

        List<String> assets = getUniqueAsset();
        Queue<Transaction> transactionsCopy = new LinkedList<>(transactions);

        float netProfit = 0f;
        Queue<Transaction> buyPositionTransactions;
        Queue<Transaction> sellPositionTransactions;
        for (String asset: assets) {

            sellPositionTransactions = getTransactionsByAssetNameAndPosition(asset, 'S');


            // calculate total quantities need to sell
            float quantitiesToSell = sellPositionTransactions.stream()
                    .map(Transaction::quantity)
                    .reduce(0F, (a, b) -> a + b);
            float totalSellValue =  sellPositionTransactions.stream()
                    .map(transaction -> transaction.quantity() * transaction.price())
                    .reduce(0F, (a, b) -> a + b);

            // calculate average cost per 1 qty
            float averageSellCost = totalSellValue / quantitiesToSell  ;

            buyPositionTransactions = getTransactionsByAssetNameAndPosition(asset, 'B');
            Transaction currentTransaction;
            float quantitiesCanSell = 0F;

            while (quantitiesToSell != 0) {

                currentTransaction = buyPositionTransactions.poll();
                System.out.println(currentTransaction);

                quantitiesCanSell = currentTransaction.quantity();

                if ( quantitiesCanSell > quantitiesToSell) {
                    quantitiesCanSell = quantitiesToSell;
                }

                float buyPrice = currentTransaction.price();
                float buyValue = buyPrice * quantitiesCanSell;

                float sellValue = averageSellCost * quantitiesCanSell;

                netProfit = netProfit + sellValue - buyValue;

                quantitiesToSell -= quantitiesCanSell;

            }
        }
        return netProfit;
    }

    public Queue<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Queue<Transaction> transactions) {
        this.transactions = transactions;
    }
}

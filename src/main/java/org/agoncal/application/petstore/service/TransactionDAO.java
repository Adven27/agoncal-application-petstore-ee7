package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.view.shopping.Transaction;

import java.util.List;

public interface TransactionDAO {
    List<Transaction> getTransactions();
}
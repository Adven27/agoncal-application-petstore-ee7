package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.view.shopping.Transaction;

import javax.enterprise.inject.Default;
import java.util.List;

@Default
public class TransactionDAOImpl implements TransactionDAO {
    @Override
    public List<Transaction> getTransactions() {
        throw new UnsupportedOperationException();
    }
}
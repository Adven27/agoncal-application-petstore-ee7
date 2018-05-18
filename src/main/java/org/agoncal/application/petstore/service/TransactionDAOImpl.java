package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.view.shopping.Transaction;

import javax.enterprise.inject.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

@Default
public class TransactionDAOImpl implements TransactionDAO {
    @Override
    public List<Transaction> getTransactions() {
        return asList(
                new Transaction(BigDecimal.valueOf(1000), new Date(), "Category 1"),
                new Transaction(BigDecimal.valueOf(200), new Date(), "Category 2"),
                new Transaction(BigDecimal.valueOf(220), new Date(), "Category 2")
        );
    }
}
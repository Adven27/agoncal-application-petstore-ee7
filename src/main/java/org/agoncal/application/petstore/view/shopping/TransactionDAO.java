package org.agoncal.application.petstore.view.shopping;

import java.util.List;

public interface TransactionDAO {
    List<Transaction> getTransactions();
}
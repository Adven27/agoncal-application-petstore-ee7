package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.view.shopping.Transaction;

import java.io.Serializable;
import java.util.List;

public interface TransactionDAO extends Serializable {
    List<Transaction> findAll();
}
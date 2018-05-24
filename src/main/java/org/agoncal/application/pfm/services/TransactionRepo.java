package org.agoncal.application.pfm.services;

import org.agoncal.application.pfm.model.Transaction;

import java.io.Serializable;
import java.util.List;

public interface TransactionRepo extends Serializable {
    List<Transaction> findAll();
}
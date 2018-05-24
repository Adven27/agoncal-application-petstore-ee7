package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.shopping.Transaction;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Loggable
public class DBTransactionDAO implements TransactionDAO {
    private final EntityManager em;

    @Inject
    public DBTransactionDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Transaction> findAll() {
        return em.createQuery("select e from Transaction e", Transaction.class).getResultList();
    }
}
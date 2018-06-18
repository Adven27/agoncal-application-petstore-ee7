package org.agoncal.application.pfm.services.impl;

import org.agoncal.application.cfg.Loggable;
import org.agoncal.application.pfm.Transaction;
import org.agoncal.application.pfm.services.TransactionRepo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
@Loggable
public class DBTransactionRepo implements TransactionRepo {
    private final EntityManager em;

    @Inject
    public DBTransactionRepo(EntityManager em) {
        this.em = em;
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @Override
    public List<Transaction> findAll() {
        return em.createQuery("select e from Transaction e", Transaction.class).getResultList();
    }
}
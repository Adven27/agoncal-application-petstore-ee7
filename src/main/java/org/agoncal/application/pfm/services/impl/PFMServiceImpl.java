package org.agoncal.application.pfm.services.impl;

import org.agoncal.application.pfm.Origin;
import org.agoncal.application.pfm.model.Category;
import org.agoncal.application.pfm.model.Transaction;
import org.agoncal.application.pfm.services.CategoryRepo;
import org.agoncal.application.pfm.services.PFMService;
import org.agoncal.application.pfm.services.TransactionRepo;

import javax.inject.Inject;
import java.util.List;

@Origin
public class PFMServiceImpl implements PFMService {
    private final TransactionRepo transactionRepo;
    private final CategoryRepo categoryRepo;

    @Inject
    public PFMServiceImpl(TransactionRepo transactionRepo, CategoryRepo categoryRepo) {
        this.transactionRepo = transactionRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Transaction> transactions() {
        return transactionRepo.findAll();
    }

    @Override
    public List<Category> categories() {
        return categoryRepo.findAll();
    }
}
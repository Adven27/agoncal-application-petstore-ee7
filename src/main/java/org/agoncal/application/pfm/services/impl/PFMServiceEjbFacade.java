package org.agoncal.application.pfm.services.impl;

import org.agoncal.application.pfm.Origin;
import org.agoncal.application.pfm.model.Category;
import org.agoncal.application.pfm.model.Transaction;
import org.agoncal.application.pfm.services.PFMService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PFMServiceEjbFacade implements PFMService {
    private final PFMService original;

    @Inject
    public PFMServiceEjbFacade(@Origin PFMService original) {
        this.original = original;
    }

    @Override
    public List<Transaction> transactions() {
        return original.transactions();
    }

    @Override
    public List<Category> categories() {
        return original.categories();
    }
}
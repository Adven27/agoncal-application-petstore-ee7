package org.agoncal.application.pfm.services.impl;

import org.agoncal.application.pfm.model.Category;
import org.agoncal.application.pfm.services.CategoryRepo;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class DBCategoryRepo implements CategoryRepo {
    private final EntityManager em;

    @Inject
    public DBCategoryRepo(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Category> findAll() {
        return null;
    }
}

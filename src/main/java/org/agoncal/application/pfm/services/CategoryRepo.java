package org.agoncal.application.pfm.services;

import org.agoncal.application.pfm.model.Category;

import java.io.Serializable;
import java.util.List;

public interface CategoryRepo extends Serializable {
    List<Category> findAll();
}
package org.agoncal.application.pfm.services;

import org.agoncal.application.pfm.model.Category;
import org.agoncal.application.pfm.model.Transaction;

import java.io.Serializable;
import java.util.List;

public interface PFMService extends Serializable {
    List<Transaction> transactions();

    List<Category> categories();
}
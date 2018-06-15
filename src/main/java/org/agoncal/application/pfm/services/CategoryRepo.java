package org.agoncal.application.pfm.services;

import java.io.Serializable;
import java.util.List;

public interface CategoryRepo extends Serializable {
    List<String> findAll();
}
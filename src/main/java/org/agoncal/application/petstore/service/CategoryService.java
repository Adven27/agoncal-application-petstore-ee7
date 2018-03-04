package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.util.Loggable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
@Loggable
public class CategoryService extends AbstractService<Category> implements Serializable {

    public CategoryService() {
        super(Category.class);
    }

    @Override
    protected Predicate[] getSearchPredicates(Root<Category> root, Category example) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String name = example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }
        String description = example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
package com.training.akarpach.helpDesk.dao.impl;

import com.training.akarpach.helpDesk.dao.AbstractDao;
import com.training.akarpach.helpDesk.model.Category;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategoryDao extends AbstractDao<Category> {

    public CategoryDao() {
        super();
        setClazz(Category.class);
    }

    public Optional<Category> getCategoryByName(String name) {

        return Optional.of(getEntityManager()
                .createQuery("FROM Category c where c.name = :name", Category.class)
                .setParameter("name", name)
                .getSingleResult());

    }

}

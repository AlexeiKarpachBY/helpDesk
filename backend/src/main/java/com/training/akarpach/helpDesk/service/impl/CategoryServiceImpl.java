package com.training.akarpach.helpDesk.service.impl;

import com.training.akarpach.helpDesk.dao.impl.CategoryDao;
import com.training.akarpach.helpDesk.model.Category;
import com.training.akarpach.helpDesk.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public List<Category> getAllCategory() {

        return categoryDao.findAll();

    }

    @Override
    @Transactional
    public Category findCategoryByName(String name) {

        return categoryDao.getCategoryByName(name).orElseThrow(EntityNotFoundException::new);

    }
}

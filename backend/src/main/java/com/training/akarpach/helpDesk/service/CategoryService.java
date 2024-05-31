package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();

    Category findCategoryByName(String name);

}

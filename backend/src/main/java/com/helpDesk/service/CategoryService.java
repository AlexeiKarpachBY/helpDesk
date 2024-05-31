package com.helpDesk.service;

import com.helpDesk.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();

    Category findCategoryByName(String name);

}

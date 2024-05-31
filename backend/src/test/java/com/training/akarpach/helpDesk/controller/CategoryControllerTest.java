package com.training.akarpach.helpDesk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.akarpach.helpDesk.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    private List<Category> categoryList = new ArrayList<>();
    private final Category categoryOne = new Category();
    private final Category categoryTwo = new Category();
    private final Category categoryThree = new Category();
    private final Category categoryFour = new Category();
    private final Category categoryFive = new Category();
    private final Category categorySix = new Category();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        categoryOne.setId(1L);
        categoryOne.setName("Application & Services");
        categoryTwo.setId(2L);
        categoryTwo.setName("Benefits & Paper Work");
        categoryThree.setId(3L);
        categoryThree.setName("Hardware & Software");
        categoryFour.setId(4L);
        categoryFour.setName("People Management");
        categoryFive.setId(5L);
        categoryFive.setName("Security & Access");
        categorySix.setId(6L);
        categorySix.setName("Workplaces & Facilities");

        categoryList = List.of(categoryOne, categoryTwo, categoryThree, categoryFour, categoryFive, categorySix);

    }

    @Test
    @WithUserDetails(value = "user1_mogilev@yopmail.com")
    void getAllCategory() throws Exception {

        MvcResult result = mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andReturn();

        String actual = result.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(categoryList);

        Assertions.assertEquals(expected, actual);

    }
}
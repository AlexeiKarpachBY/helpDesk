package com.training.akarpach.helpDesk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.akarpach.helpDesk.dto.FeedbackDto;
import com.training.akarpach.helpDesk.dto.TicketDto;
import com.training.akarpach.helpDesk.util.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.training.akarpach.helpDesk.enums.State.NEW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    private final TicketDto ticketDto = new TicketDto();
    private final TicketDto ticket = new TicketDto();
    private final FeedbackDto feedbackDto = new FeedbackDto();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DateFormatter dateFormatter;

    @BeforeEach
    void setUp() {

        feedbackDto.setRate("BAD");
        feedbackDto.setText("test");

        ticket.setOwner("Naruto Uzumaki");

        ticketDto.setName("test");
        ticketDto.setDescription("test");
        ticketDto.setDesiredResolutionDate("2099-10-13");
        ticketDto.setState("DRAFT");
        ticketDto.setUrgency("LOW");
        ticketDto.setCategory("Application & Services");

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void createTicketPositiveCase() throws Exception {

        mockMvc.perform(post("/ticket")
                        .contentType("application/json;charset=utf-8")
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isCreated());

    }

    @Test
    void createTicketUnauthorizedCase() throws Exception {

        mockMvc.perform(post("/ticket")
                        .contentType("application/json;charset=utf-8")
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void createTicketWrongDtoFieldCase() throws Exception {

        ticketDto.setName("TEST");

        mockMvc.perform(post("/ticket")
                        .contentType("application/json;charset=utf-8")
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void updateTicketPositiveCase() throws Exception {

        mockMvc.perform(put("/ticket")
                        .contentType("application/json;charset=utf-8")
                        .param("ticketId", String.valueOf(1L))
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isCreated());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void updateTicketWrongDtoFieldCase() throws Exception {

        ticketDto.setName("TEST");

        mockMvc.perform(put("/ticket")
                        .contentType("application/json;charset=utf-8")
                        .param("ticketId", String.valueOf(1L))
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateTicketUnauthorizedCase() throws Exception {

        ticketDto.setName("TEST");

        mockMvc.perform(put("/ticket")
                        .contentType("application/json;charset=utf-8")
                        .param("ticketId", String.valueOf(1L))
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void getAllUserTicketsByRolePositiveCase() throws Exception {

        mockMvc.perform(get("/ticket/role")
                        .contentType("application/json")
                        .param("size", String.valueOf(1))
                        .param("pageNumber", String.valueOf(0))
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());

    }

    @Test
    void getAllUserTicketsByRoleUnauthorizedCase() throws Exception {

        mockMvc.perform(get("/ticket/role")
                        .contentType("application/json")
                        .param("size", String.valueOf(1))
                        .param("pageNumber", String.valueOf(0))
                        .param("sort", "id,asc"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void getAllUserTicketsPositiveCase() throws Exception {

        mockMvc.perform(get("/ticket/all")
                        .contentType("application/json")
                        .param("size", String.valueOf(1))
                        .param("pageNumber", String.valueOf(0))
                        .param("sort", "id,asc"))
                .andExpect(status().isOk());

    }

    @Test
    void getAllUserTicketsUnauthorizedCase() throws Exception {

        mockMvc.perform(get("/ticket/all")
                        .contentType("application/json")
                        .param("size", String.valueOf(1))
                        .param("pageNumber", String.valueOf(0))
                        .param("sort", "id,asc"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void doTicketActionPositiveCase() throws Exception {

        mockMvc.perform(put("/ticket/{ticketId}/{action}", "1", "submit")
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @Test
    void doTicketActionUnauthorizedCase() throws Exception {

        mockMvc.perform(put("/ticket/{ticketId}/{action}", "1", "submit")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void updateTicketStatePositiveCase() throws Exception {

        mockMvc.perform(put("/ticket/state")
                        .contentType("application/json")
                        .param("state", String.valueOf(NEW))
                        .param("ticketId", String.valueOf(1L)))
                .andExpect(status().isOk());

    }

    @Test
    void updateTicketStateUnauthorizedCase() throws Exception {

        mockMvc.perform(put("/ticket/state")
                        .contentType("application/json")
                        .param("state", String.valueOf(NEW))
                        .param("ticketId", String.valueOf(1L)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithUserDetails("user1_mogilev@yopmail.com")
    void getTicketByIdPositiveCase() throws Exception {

        mockMvc.perform(get("/ticket/{id}", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @Test
    void getTicketByIdUnauthorizedCase() throws Exception {

        mockMvc.perform(get("/ticket/{id}", "1")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

    }

}
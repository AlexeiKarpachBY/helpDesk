package com.helpDesk.service.impl;

import com.helpDesk.model.Ticket;
import com.helpDesk.service.*;
import com.helpDesk.enums.Action;
import com.helpDesk.enums.State;
import com.helpDesk.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.helpDesk.enums.Action.*;
import static com.helpDesk.enums.Role.ENGINEER;
import static com.helpDesk.enums.Role.MANAGER;

@Service
public class ActionServiceImpl implements ActionService {

    private final Logger logger = LoggerFactory.getLogger(ActionServiceImpl.class);

    private final UserService userService;
    private final EmailService emailService;
    private final TicketService ticketService;
    private final HistoryService historyService;

    @Autowired
    public ActionServiceImpl(UserService userService, TicketService ticketService, HistoryService historyService, EmailService emailService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.historyService = historyService;
        this.emailService = emailService;
    }

    @Override
    public void doAction(String ticketAction, Ticket ticket) {

        Action action = Action.valueOf(ticketAction.toUpperCase());
        User user = userService.getCurrentUser();
        State oldState = ticket.getState();

        ticket.setState(action.getNextState());
        setApproverOrAssignee(ticket, action, user);

        ticketService.updateTicket(ticket);
        historyService.commitChangeTicketStatus(user, ticket, oldState);
        emailService.sentNotification(ticket, oldState);

        logger.info("Do " + action + " action for ticket with ID: " + ticket.getId() + " and sent notification");

    }

    private void setApproverOrAssignee(Ticket ticket, Action action, User user) {

        if (action == APPROVE) {
            ticket.setApprover(user);
        }
        if (action == ASSIGN_TO_ME || action == CANCEL && user.getRole() == ENGINEER) {
            ticket.setAssignee(user);
        }
        if (action == DECLINE || action == CANCEL && user.getRole() == MANAGER) {
            ticket.setApprover(user);
        }

    }
}

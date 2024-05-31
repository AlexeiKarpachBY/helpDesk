package com.helpDesk.service.impl;

import com.helpDesk.model.Attachment;
import com.helpDesk.model.Feedback;
import com.helpDesk.model.Ticket;
import com.helpDesk.service.*;
import com.helpDesk.dao.impl.TicketDao;
import com.helpDesk.enums.State;
import com.helpDesk.model.User;
import com.training.akarpach.helpDesk.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.helpDesk.enums.Role.EMPLOYEE;
import static com.helpDesk.enums.State.APPROVED;
import static com.helpDesk.enums.State.NEW;
import static java.util.Optional.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TicketServiceImplTest {

    private final State oldState = NEW;
    private final User user = new User();
    private final State newState = APPROVED;
    private final Ticket ticket = new Ticket();
    private final Feedback feedback = new Feedback();
    private final Attachment attachment = new Attachment();
    private final List<Ticket> ticketList = new ArrayList<>();
    private final List<Attachment> attachmentList = new ArrayList<>();

    @Mock
    EmailService emailService;

    @Mock
    private TicketDao ticketDao;

    @Mock
    HistoryService historyService;

    @Mock
    private UserService userService;

    @Mock
    FeedBackService feedBackService;

    @Mock
    AttachmentService attachmentService;

    @InjectMocks
    TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {

        ticket.setState(NEW);

        user.setRole(EMPLOYEE);

        attachmentList.add(attachment);

    }

    @Test
    void saveNewTicket() {

        when(userService.getCurrentUser()).thenReturn(user);
        doNothing().when(ticketDao).save(ticket);
        doNothing().when(historyService).commitTicketCreation(user, ticket);

        ticketService.saveNewTicket(ticket);

        verify(userService, times(1)).getCurrentUser();
        verify(ticketDao, times(1)).save(ticket);
        verify(historyService, times(1)).commitTicketCreation(user, ticket);

    }

    @Test
    void addAttachmentToTicket() {

        when(userService.getCurrentUser()).thenReturn(user);
        when(ticketDao.findOne(1L)).thenReturn(of(ticket));
        doNothing().when(ticketDao).save(ticket);
        doNothing().when(historyService).commitAddingAttachment(user, ticket, attachment);

        ticketService.addAttachmentToTicket(1L, attachmentList);

        verify(userService, times(1)).getCurrentUser();
        verify(ticketDao, times(1)).findOne(1L);
        verify(ticketDao, times(1)).save(ticket);
        verify(historyService, times(1)).commitAddingAttachment(user, ticket, attachment);

    }

    @Test
    void removeAttachmentFromTicket() {

        when(userService.getCurrentUser()).thenReturn(user);
        when(ticketDao.findOne(1L)).thenReturn(of(ticket));
        when(attachmentService.findAttachmentById(1L)).thenReturn(attachment);
        doNothing().when(ticketDao).save(ticket);
        doNothing().when(historyService).commitDeleteAttachment(user, ticket, attachment);
        doNothing().when(attachmentService).deleteAttachment(1L);

        ticketService.removeAttachmentFromTicket(1L, 1L);

        verify(userService, times(1)).getCurrentUser();
        verify(ticketDao, times(1)).findOne(1L);
        verify(attachmentService, times(1)).findAttachmentById(1L);
        verify(ticketDao, times(1)).save(ticket);
        verify(historyService, times(1)).commitDeleteAttachment(user, ticket, attachment);
        verify(attachmentService, times(1)).deleteAttachment(1L);

    }

    @Test
    void updateTicket() {

        doNothing().when(ticketDao).update(ticket);

        ticketService.updateTicket(ticket);

        verify(ticketDao, times(1)).update(ticket);

    }

    @Test
    void getTicketsByUserRole() {

        when(userService.getCurrentUser()).thenReturn(user);
        when(ticketDao.getTicketByUserRole("allUserTicket", user)).thenReturn(ticketList);

        ticketService.getTicketsByUserRole();

        verify(userService, times(1)).getCurrentUser();
        verify(ticketDao, times(1)).getTicketByUserRole("allUserTicket", user);

    }

    @Test
    void getAllUserTicketByRole() {

        when(userService.getCurrentUser()).thenReturn(user);
        when(ticketDao.getTicketByUserRole("allUserTicket", user)).thenReturn(ticketList);

        ticketService.getAllUserTicketByRole();

        verify(userService, times(1)).getCurrentUser();
        verify(ticketDao, times(1)).getTicketByUserRole("allUserTicket", user);

    }

    @Test
    void updateTicketState() {

        when(userService.getCurrentUser()).thenReturn(user);
        doNothing().when(ticketDao).update(ticket);
        doNothing().when(historyService).commitChangeTicketStatus(user, ticket, oldState);
        doNothing().when(emailService).sentNotification(ticket, oldState);

        ticketService.updateTicketState(ticket, newState);

        verify(userService, times(1)).getCurrentUser();
        verify(ticketDao, times(1)).update(ticket);
        verify(historyService, times(1)).commitChangeTicketStatus(user, ticket, oldState);
        verify(emailService, times(1)).sentNotification(ticket, oldState);

    }

    @Test
    void setFeedBack() {

        when(userService.getCurrentUser()).thenReturn(user);
        doNothing().when(feedBackService).saveFeedback(feedback);
        doNothing().when(emailService).sentFeedBackNotification(ticket);

        ticketService.setFeedBack(ticket, feedback);

        verify(userService, times(1)).getCurrentUser();
        verify(feedBackService, times(1)).saveFeedback(feedback);
        verify(emailService, times(1)).sentFeedBackNotification(ticket);

    }

    @Test
    void getTicketById() {

        when(ticketDao.findOne(1L)).thenReturn(of(ticket));

        ticketService.getTicketById(1L);

        verify(ticketDao, times(1)).findOne(1L);

    }

}
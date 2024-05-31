package com.helpDesk.service.impl;

import com.helpDesk.model.Attachment;
import com.helpDesk.model.Feedback;
import com.helpDesk.model.Ticket;
import com.helpDesk.service.*;
import com.helpDesk.converter.TicketConverter;
import com.helpDesk.dao.impl.TicketDao;
import com.helpDesk.dto.TicketDto;
import com.helpDesk.enums.State;
import com.helpDesk.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.helpDesk.enums.Role.*;

@Service
public class TicketServiceImpl implements TicketService {

    private final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketDao ticketDao;
    private final UserService userService;
    private final EmailService emailService;
    private final HistoryService historyService;
    private final FeedBackService feedBackService;
    private final TicketConverter ticketConverter;
    private final AttachmentService attachmentService;

    @Autowired
    public TicketServiceImpl(TicketDao ticketDao, EmailService emailService,
                             HistoryService historyService, UserService userService,
                             AttachmentService attachmentService, FeedBackService feedBackService, TicketConverter ticketConverter) {
        this.ticketDao = ticketDao;
        this.userService = userService;
        this.emailService = emailService;
        this.historyService = historyService;
        this.feedBackService = feedBackService;
        this.ticketConverter = ticketConverter;
        this.attachmentService = attachmentService;
    }

    @Override
    @Transactional
    public void saveNewTicket(Ticket ticket) {

        User user = userService.getCurrentUser();
        ticket.setOwner(user);
        ticket.setCreatedOn(LocalDate.now());

        ticketDao.save(ticket);
        historyService.commitTicketCreation(ticket.getOwner(), ticket);

        logger.info("Save ticket with ID: " + ticket.getId());

    }

    @Override
    @Transactional
    public void addAttachmentToTicket(Long ticketId, List<Attachment> attachmentList) {

        User user = userService.getCurrentUser();
        Ticket ticket = ticketDao.findOne(ticketId).orElseThrow(EntityNotFoundException::new);

        if (!attachmentList.isEmpty()) {
            for (Attachment attachment : attachmentList) {

                ticket.addAttachment(attachment);
                ticketDao.save(ticket);
                historyService.commitAddingAttachment(user, ticket, attachment);

                logger.info("Adding attachment with ID: " + attachment.getId());
            }
        }

    }

    @Override
    @Transactional
    public void removeAttachmentFromTicket(Long ticketId, Long attachmentId) {

        User user = userService.getCurrentUser();
        Ticket ticket = ticketDao.findOne(ticketId).orElseThrow(EntityNotFoundException::new);
        Attachment attachment = attachmentService.findAttachmentById(attachmentId);
        ticket.removeAttachment(attachment);

        ticketDao.save(ticket);
        historyService.commitDeleteAttachment(user, ticket, attachment);
        attachmentService.deleteAttachment(attachmentId);

        logger.info("Deleting attachment with ID: " + attachment.getId());

    }

    @Override
    @Transactional
    public void updateTicket(Ticket ticket) {
        ticketDao.update(ticket);
        logger.info("Update ticket with ID: " + ticket.getId());
    }

    @Override
    @Transactional
    public List<Ticket> getTicketsByUserRole() {
        User user = userService.getCurrentUser();
        List<Ticket> ticketList = new ArrayList<>();

        if (user.getRole() == EMPLOYEE) {
            ticketList = ticketDao.getTicketByUserRole("allUserTicket", user);
        }
        if (user.getRole() == ENGINEER) {
            ticketList = ticketDao.getTicketByUserRole("engineerRole", user);
        }
        if (user.getRole() == MANAGER) {
            ticketList = ticketDao.getTicketByUserRole("managerRole", user);
        }
        logger.info("Return tickets list for role: " + user.getRole());
        return ticketList;
    }

    @Override
    @Transactional
    public List<Ticket> getAllUserTicketByRole() {

        User user = userService.getCurrentUser();
        List<Ticket> ticketList = new ArrayList<>();

        if (user.getRole() == EMPLOYEE) {
            ticketList = ticketDao.getTicketByUserRole("allUserTicket", user);
        }
        if (user.getRole() == ENGINEER) {
            ticketList = ticketDao.getTicketByUserRole("allEngineerTicket", user);
        }
        if (user.getRole() == MANAGER) {
            ticketList = ticketDao.getTicketByUserRole("allManagerTicket", user);
        }

        logger.info("Return all tickets list for role: " + user.getRole());

        return ticketList;

    }

    @Override
    @Transactional
    public void updateTicketState(Ticket ticket, State newState) {

        User user = userService.getCurrentUser();
        State oldState = ticket.getState();
        ticket.setState(newState);

        ticketDao.update(ticket);
        historyService.commitChangeTicketStatus(user, ticket, oldState);
        emailService.sentNotification(ticket, oldState);

        logger.info("Update state for ticket with ID: " + ticket.getId() + " and sent notification");

    }

    @Override
    @Transactional
    public void setFeedBack(Ticket ticket, Feedback feedback) {

        User user = userService.getCurrentUser();
        feedback.setDate(LocalDate.now());
        feedback.setUser(user);
        feedback.setTicket(ticket);

        feedBackService.saveFeedback(feedback);
        emailService.sentFeedBackNotification(ticket);

        logger.info("Set feedback for ticket with ID: " + ticket.getId() + " and sent notification");

    }

    @Override
    @Transactional
    public Ticket getTicketById(Long id) {

        return ticketDao.findOne(id).orElseThrow(EntityNotFoundException::new);

    }

    @Override
    public Page<TicketDto> doPagination(List<Ticket> list, int pageSize, int pageNumber, String sort) {

        list = doSort(list, sort);
        List<TicketDto> listDto = ticketConverter.toDtoList(list);
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        int start = Math.min((int) paging.getOffset(), listDto.size());
        int end = Math.min((start + paging.getPageSize()), listDto.size());

        return new PageImpl<>(listDto.subList(start, end), paging, listDto.size());

    }

    private List<Ticket> doSort(List<Ticket> ticketList, String sort) {

        String[] res = sort.split("[,]");
        String orderBy = res[0];
        String order = res[1];

        switch (orderBy) {
            case "id":
                if (order.equals("asc")) {
                    ticketList.sort(Comparator.comparingLong(Ticket::getId));
                } else if (order.equals("desc")) {
                    ticketList.sort(Comparator.comparingLong(Ticket::getId).reversed());
                }
                break;
            case "name":
                if (order.equals("asc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getName));
                } else if (order.equals("desc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getName).reversed());
                }
                break;
            case "desiredResolutionDate":
                if (order.equals("asc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getDesiredResolutionDate));
                } else if (order.equals("desc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getDesiredResolutionDate).reversed());
                }
                break;
            case "urgency":
                if (order.equals("asc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getUrgency));
                } else if (order.equals("desc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getUrgency).reversed());
                }
                break;
            case "state":
                if (order.equals("asc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getState));
                } else if (order.equals("desc")) {
                    ticketList.sort(Comparator.comparing(Ticket::getState).reversed());
                }
                break;
        }

        return ticketList;
    }
}



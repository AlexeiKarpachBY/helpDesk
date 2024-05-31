package com.training.akarpach.helpDesk.service.impl;

import com.training.akarpach.helpDesk.dao.impl.HistoryDao;
import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.model.Attachment;
import com.training.akarpach.helpDesk.model.History;
import com.training.akarpach.helpDesk.model.Ticket;
import com.training.akarpach.helpDesk.model.User;
import com.training.akarpach.helpDesk.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final Logger logger = LoggerFactory.getLogger(HistoryService.class);

    private final HistoryDao historyDao;

    @Autowired
    public HistoryServiceImpl(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    @Override
    @Transactional
    public List<History> findAllHistoriesByTicket(Long ticketId) {
        return historyDao.findAllByTicketId(ticketId);
    }

    @Override
    @Transactional
    public void commitTicketCreation(User user, Ticket ticket) {

        History history = new History();

        history.setDate(LocalDate.now());
        history.setAction("Ticket is created");
        history.setDescription("Ticket is created");
        user.addHistory(history);
        ticket.addHistory(history);

        historyDao.save(history);

        logger.info("Save history with ID: " + history.getId());

    }

    @Override
    @Transactional
    public void commitChangeTicketStatus(User user, Ticket ticket, State oldState) {

        History history = new History();

        history.setDate(LocalDate.now());
        history.setAction("Ticket Status is changed");
        history.setDescription("Ticket Status is changed from: " + oldState + " to " + ticket.getState());
        user.addHistory(history);
        history.setTicket(ticket);

        historyDao.save(history);

        logger.info("Save history with ID: " + history.getId());

    }

    @Override
    @Transactional
    public void commitAddingAttachment(User user, Ticket ticket, Attachment attachment) {

        History history = new History();

        history.setDate(LocalDate.now());
        history.setAction("File is attached");
        history.setDescription("File is attached: " + attachment.getName());
        user.addHistory(history);
        ticket.addHistory(history);

        historyDao.save(history);

        logger.info("Save history with ID: " + history.getId());

    }

    @Override
    @Transactional
    public void commitDeleteAttachment(User user, Ticket ticket, Attachment attachment) {

        History history = new History();

        history.setDate(LocalDate.now());
        history.setAction("File is removed");
        history.setDescription("File is removed: " + attachment.getName());
        user.addHistory(history);
        ticket.addHistory(history);

        historyDao.save(history);

        logger.info("Save history with ID: " + history.getId());

    }

}

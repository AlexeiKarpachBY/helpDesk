package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.model.Attachment;
import com.training.akarpach.helpDesk.model.History;
import com.training.akarpach.helpDesk.model.Ticket;
import com.training.akarpach.helpDesk.model.User;

import java.util.List;

public interface HistoryService {

    void commitTicketCreation(User user, Ticket ticket);

    List<History> findAllHistoriesByTicket(Long ticketId);

    void commitChangeTicketStatus(User user, Ticket ticket, State oldState);

    void commitAddingAttachment(User user, Ticket ticket, Attachment attachment);

    void commitDeleteAttachment(User user, Ticket ticket, Attachment attachment);

}

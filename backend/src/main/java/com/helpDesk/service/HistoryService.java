package com.helpDesk.service;

import com.helpDesk.model.Attachment;
import com.helpDesk.model.History;
import com.helpDesk.model.Ticket;
import com.helpDesk.enums.State;
import com.helpDesk.model.User;

import java.util.List;

public interface HistoryService {

    void commitTicketCreation(User user, Ticket ticket);

    List<History> findAllHistoriesByTicket(Long ticketId);

    void commitChangeTicketStatus(User user, Ticket ticket, State oldState);

    void commitAddingAttachment(User user, Ticket ticket, Attachment attachment);

    void commitDeleteAttachment(User user, Ticket ticket, Attachment attachment);

}

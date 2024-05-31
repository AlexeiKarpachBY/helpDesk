package com.helpDesk.service;

import com.helpDesk.model.Attachment;
import com.helpDesk.model.Feedback;
import com.helpDesk.model.Ticket;
import com.helpDesk.dto.TicketDto;
import com.helpDesk.enums.State;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {

    Ticket getTicketById(Long id);

    void updateTicket(Ticket ticket);

    void saveNewTicket(Ticket ticket);

    List<Ticket> getTicketsByUserRole();

    List<Ticket> getAllUserTicketByRole();

    void setFeedBack(Ticket ticket, Feedback feedback);

    void updateTicketState(Ticket ticket, State newState);

    void removeAttachmentFromTicket(Long ticketId, Long attachmentId);

    void addAttachmentToTicket(Long ticketId, List<Attachment> attachmentList);

    Page<TicketDto> doPagination(List<Ticket> list, int pageSize, int pageNumber, String sort);

}

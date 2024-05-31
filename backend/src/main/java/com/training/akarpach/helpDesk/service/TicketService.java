package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.dto.TicketDto;
import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.model.Attachment;
import com.training.akarpach.helpDesk.model.Feedback;
import com.training.akarpach.helpDesk.model.Ticket;
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

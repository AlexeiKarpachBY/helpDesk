package com.training.akarpach.helpDesk.converter;

import com.training.akarpach.helpDesk.dto.AttachmentDto;
import com.training.akarpach.helpDesk.dto.TicketDto;
import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.enums.Urgency;
import com.training.akarpach.helpDesk.exception.WrongDesiredDateException;
import com.training.akarpach.helpDesk.model.Category;
import com.training.akarpach.helpDesk.model.Ticket;
import com.training.akarpach.helpDesk.service.AttachmentService;
import com.training.akarpach.helpDesk.service.CategoryService;
import com.training.akarpach.helpDesk.service.UserService;
import com.training.akarpach.helpDesk.util.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.valueOf;

@Component
public class TicketConverter {

    private final UserService userService;
    private final DateFormatter dateFormatter;
    private final CategoryService categoryService;
    private final AttachmentService attachmentService;
    private final FeedbackConverter feedbackConverter;
    private final AttachmentConverter attachmentConverter;


    @Autowired
    public TicketConverter(DateFormatter dateFormatter, CategoryService categoryService, AttachmentService attachmentService,
                           AttachmentConverter attachmentConverter, UserService userService, FeedbackConverter feedbackConverter) {
        this.dateFormatter = dateFormatter;
        this.categoryService = categoryService;
        this.attachmentService = attachmentService;
        this.attachmentConverter = attachmentConverter;
        this.userService = userService;
        this.feedbackConverter = feedbackConverter;
    }

    public Ticket toEntity(TicketDto dto) {
        Ticket ticket = new Ticket();

        LocalDate desiredDate = dateFormatter.getDateFromDto(dto.getDesiredResolutionDate());
        Category category = categoryService.findCategoryByName(dto.getCategory());

        if (dto.getId() != null) ticket.setId(valueOf(dto.getId()));
        ticket.setName(dto.getName());
        ticket.setDescription(dto.getDescription());
        ticket.setState(State.valueOf(dto.getState()));
        ticket.setUrgency(Urgency.valueOf(dto.getUrgency()));
        ticket.setCategory(category);
        checkAndSetDesiredResolutionDate(ticket, desiredDate, LocalDate.now());

        return ticket;
    }

    public void updateEntityFromDto(Ticket ticket, TicketDto ticketDto) {

        Category category = categoryService.findCategoryByName(ticketDto.getCategory());
        LocalDate desiredDate = dateFormatter.getDateFromDto(ticketDto.getDesiredResolutionDate());

        ticket.setName(ticketDto.getName());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setCategory(category);
        ticket.setUrgency(Urgency.valueOf(ticketDto.getUrgency()));
        ticket.setState(State.valueOf(ticketDto.getState()));
        checkAndSetDesiredResolutionDate(ticket, desiredDate, LocalDate.now());
    }


    public TicketDto toDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();

        List<AttachmentDto> attachmentList = attachmentConverter.toDtoList(attachmentService.findAttachmentByTicketId(ticket.getId()));
        String desiredDate = dateFormatter.getDateForDto(ticket.getDesiredResolutionDate());
        String createdOn = dateFormatter.getDateForDto(ticket.getCreatedOn());

        ticketDto.setId(String.valueOf(ticket.getId()));
        ticketDto.setName(ticket.getName());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setDesiredResolutionDate(desiredDate);
        ticketDto.setCreatedOn(createdOn);
        ticketDto.setState(ticket.getState().name());
        ticketDto.setUrgency(ticket.getUrgency().name());
        ticketDto.setCategory(ticket.getCategory().getName());
        ticketDto.setOwner(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName());
        ticketDto.setAttachment(attachmentList);
        if (ticket.getAssignee() != null) ticketDto.setAssignee(ticket.getAssignee().getFirstName() + " " + ticket.getAssignee().getLastName());
        if (ticket.getApprover() != null) ticketDto.setApprover(ticket.getApprover().getFirstName() + " " + ticket.getApprover().getLastName());
        ticketDto.setAction(ticket.getState().getActionForState(ticket, userService.getCurrentUser()));
        ticketDto.setFeedbackDto(feedbackConverter.toDto(ticket.getFeedback()));

        return ticketDto;
    }

    public List<TicketDto> toDtoList(List<Ticket> ticketList) {
        return ticketList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private void checkAndSetDesiredResolutionDate(Ticket ticket, LocalDate desiredDate, LocalDate currentDate) {
        if (desiredDate.compareTo(currentDate) > -1) {
            ticket.setDesiredResolutionDate(desiredDate);
        } else throw new WrongDesiredDateException("Wrong desired resolution date.");
    }

}

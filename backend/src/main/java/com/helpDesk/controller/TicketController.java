package com.helpDesk.controller;

import com.helpDesk.converter.FeedbackConverter;
import com.helpDesk.converter.TicketConverter;
import com.helpDesk.model.Feedback;
import com.helpDesk.model.Ticket;
import com.helpDesk.service.ActionService;
import com.helpDesk.service.TicketService;
import com.helpDesk.dto.FeedbackDto;
import com.helpDesk.dto.TicketDto;
import com.helpDesk.enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final ActionService actionService;
    private final TicketConverter ticketConverter;
    private final FeedbackConverter feedbackConverter;

    @Autowired
    public TicketController(TicketConverter ticketConverter, TicketService ticketService,
                            ActionService actionService, FeedbackConverter feedbackConverter) {
        this.ticketService = ticketService;
        this.actionService = actionService;
        this.ticketConverter = ticketConverter;
        this.feedbackConverter = feedbackConverter;
    }

    @PostMapping
    public ResponseEntity<Long> createTicket(@Valid @RequestBody TicketDto ticketDto) {

        Ticket ticket = ticketConverter.toEntity(ticketDto);
        ticketService.saveNewTicket(ticket);

        return ResponseEntity.created(URI.create("ticket/" + ticket.getId())).body(ticket.getId());
    }

    @PutMapping
    public ResponseEntity<Long> updateTicket(@Valid @RequestBody TicketDto ticketDto,
                                             @RequestParam(value = "ticketId") Long ticketId) {

        Ticket ticket = ticketService.getTicketById(ticketId);
        ticketConverter.updateEntityFromDto(ticket, ticketDto);
        ticketService.updateTicket(ticket);

        return ResponseEntity.created(URI.create("ticket/" + ticket.getId())).body(ticket.getId());
    }

    @GetMapping("/role")
    public ResponseEntity<Page<TicketDto>> getAllUserTicketsByRole(@RequestParam("size") Integer size,
                                                                   @RequestParam("pageNumber") Integer pageNumber,
                                                                   @RequestParam("sort") String sort) {

        List<Ticket> ticketList = ticketService.getTicketsByUserRole();
        Page<TicketDto> page = ticketService.doPagination(ticketList, size, pageNumber, sort);

        return ResponseEntity.status(OK).body(page);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TicketDto>> getAllUserTickets(@RequestParam("size") Integer size,
                                                             @RequestParam("pageNumber") Integer pageNumber,
                                                             @RequestParam("sort") String sort) {

        List<Ticket> ticketList = ticketService.getAllUserTicketByRole();
        Page<TicketDto> page = ticketService.doPagination(ticketList, size, pageNumber, sort);

        return ResponseEntity.status(OK).body(page);
    }

    @PutMapping("/{ticketId}/{action}")
    public ResponseEntity<Void> doTicketAction(@PathVariable(value = "action") String action,
                                               @PathVariable(value = "ticketId") Long ticketId) {

        Ticket ticket = ticketService.getTicketById(ticketId);
        actionService.doAction(action, ticket);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/state")
    public ResponseEntity<Void> updateTicketState(@RequestParam(value = "state") State state,
                                                  @RequestParam(value = "ticketId") Long ticketId) {

        Ticket ticket = ticketService.getTicketById(ticketId);
        ticketService.updateTicketState(ticket, state);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/feedback/{ticketId}")
    public ResponseEntity<Long> setFeedback(@PathVariable(value = "ticketId") Long ticketId,
                                            @RequestBody FeedbackDto feedbackDto) {

        Ticket ticket = ticketService.getTicketById(ticketId);
        Feedback feedback = feedbackConverter.toEntity(feedbackDto);
        ticketService.setFeedBack(ticket, feedback);

        return ResponseEntity.created(URI.create("feedback/" + feedback.getId())).body(feedback.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable("id") Long id) {

        Ticket ticket = ticketService.getTicketById(id);
        TicketDto dto = ticketConverter.toDto(ticket);

        return ResponseEntity.status(OK).body(dto);
    }

}

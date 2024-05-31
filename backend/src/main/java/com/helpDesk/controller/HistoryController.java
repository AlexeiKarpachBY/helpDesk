package com.helpDesk.controller;

import com.helpDesk.converter.HistoryConverter;
import com.helpDesk.model.History;
import com.helpDesk.service.HistoryService;
import com.helpDesk.dto.HistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;
    private final HistoryConverter historyConverter;

    @Autowired
    public HistoryController(HistoryService historyService, HistoryConverter historyConverter) {
        this.historyService = historyService;
        this.historyConverter = historyConverter;
    }

    @GetMapping
    public ResponseEntity<List<HistoryDto>> getTicketHistory(@RequestParam(value = "ticketId") Long ticketId) {

        List<History> historyList = historyService.findAllHistoriesByTicket(ticketId);
        List<HistoryDto> historyDtoList = historyConverter.toDtoList(historyList);

        return ResponseEntity.status(OK).body(historyDtoList);
    }
}

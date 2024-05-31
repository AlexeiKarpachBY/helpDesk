package com.training.akarpach.helpDesk.controller;

import com.training.akarpach.helpDesk.converter.HistoryConverter;
import com.training.akarpach.helpDesk.dto.HistoryDto;
import com.training.akarpach.helpDesk.model.History;
import com.training.akarpach.helpDesk.service.HistoryService;
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

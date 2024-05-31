package com.training.akarpach.helpDesk.converter;

import com.training.akarpach.helpDesk.dto.HistoryDto;
import com.training.akarpach.helpDesk.model.History;
import com.training.akarpach.helpDesk.util.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryConverter {

    private final DateFormatter dateFormatter;

    @Autowired
    public HistoryConverter(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public HistoryDto toDto(History history) {

        HistoryDto historyDto = new HistoryDto();

        String date = dateFormatter.getDateForDto(history.getDate());
        historyDto.setAction(history.getAction());
        historyDto.setDate(date);
        historyDto.setDescription(history.getDescription());
        historyDto.setUser(history.getUser().getFirstName() + " " + history.getUser().getLastName());

        return historyDto;
    }

    public List<HistoryDto> toDtoList(List<History> historyList) {
        return historyList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}

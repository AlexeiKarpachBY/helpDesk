package com.helpDesk.converter;

import com.helpDesk.model.Feedback;
import com.helpDesk.dto.FeedbackDto;
import com.helpDesk.enums.Rate;
import org.springframework.stereotype.Component;

@Component
public class FeedbackConverter {

    public Feedback toEntity(FeedbackDto feedbackDto) {

        Feedback feedback = new Feedback();
        feedback.setText(feedbackDto.getText());
        feedback.setRate(Rate.valueOf(feedbackDto.getRate()));

        return feedback;
    }

    public FeedbackDto toDto(Feedback feedback) {

        FeedbackDto feedbackDto = new FeedbackDto();

        if (feedback != null) {
            feedbackDto.setId(feedback.getId());
            feedbackDto.setText(feedback.getText());
            feedbackDto.setRate(feedback.getRate().toString());
        }

        return feedbackDto;
    }

}

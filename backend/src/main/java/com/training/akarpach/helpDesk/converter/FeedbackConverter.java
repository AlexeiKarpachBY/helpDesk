package com.training.akarpach.helpDesk.converter;

import com.training.akarpach.helpDesk.dto.FeedbackDto;
import com.training.akarpach.helpDesk.enums.Rate;
import com.training.akarpach.helpDesk.model.Feedback;
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

package com.training.akarpach.helpDesk.service.impl;

import com.training.akarpach.helpDesk.dao.impl.FeedbackDao;
import com.training.akarpach.helpDesk.model.Feedback;
import com.training.akarpach.helpDesk.service.FeedBackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedbackDao feedbackDao;

    public FeedBackServiceImpl(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    @Override
    @Transactional
    public void saveFeedback(Feedback feedback) {

        feedbackDao.save(feedback);

    }

}

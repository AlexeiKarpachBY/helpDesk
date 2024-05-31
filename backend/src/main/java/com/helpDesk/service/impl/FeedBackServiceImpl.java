package com.helpDesk.service.impl;

import com.helpDesk.model.Feedback;
import com.helpDesk.service.FeedBackService;
import com.helpDesk.dao.impl.FeedbackDao;
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

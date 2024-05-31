package com.training.akarpach.helpDesk.dao.impl;

import com.training.akarpach.helpDesk.dao.AbstractDao;
import com.training.akarpach.helpDesk.model.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDao extends AbstractDao<Feedback> {

    public FeedbackDao() {
        super();
        setClazz(Feedback.class);
    }

}

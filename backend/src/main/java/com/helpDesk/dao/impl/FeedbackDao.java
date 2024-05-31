package com.helpDesk.dao.impl;

import com.helpDesk.model.Feedback;
import com.helpDesk.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDao extends AbstractDao<Feedback> {

    public FeedbackDao() {
        super();
        setClazz(Feedback.class);
    }

}

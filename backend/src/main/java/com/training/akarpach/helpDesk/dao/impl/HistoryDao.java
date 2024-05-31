package com.training.akarpach.helpDesk.dao.impl;

import com.training.akarpach.helpDesk.dao.AbstractDao;
import com.training.akarpach.helpDesk.model.History;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class HistoryDao extends AbstractDao<History> {

    public HistoryDao() {
        super();
        setClazz(History.class);
    }

    public List<History> findAllByTicketId(Long ticketId) {

        TypedQuery<History> query = getEntityManager().createNamedQuery("allHistoriesByTicketId", History.class);
        query.setParameter("id", ticketId);

        return query.getResultList();
    }

}

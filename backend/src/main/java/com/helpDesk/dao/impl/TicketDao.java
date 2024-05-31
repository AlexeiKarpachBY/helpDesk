package com.helpDesk.dao.impl;

import com.helpDesk.dao.AbstractDao;
import com.helpDesk.model.Ticket;
import com.helpDesk.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TicketDao extends AbstractDao<Ticket> {

    public TicketDao() {
        super();
        setClazz(Ticket.class);
    }

    public List<Ticket> getTicketByUserRole(String s, User user) {

        TypedQuery<Ticket> query = getEntityManager().createNamedQuery(s, Ticket.class);
        query.setParameter("id", user.getId());

        return  query.getResultList();
    }

    public List<Ticket> getAllUserTicket(String s, User user) {

        TypedQuery<Ticket> query = getEntityManager().createNamedQuery(s, Ticket.class);
        query.setParameter("id", user.getId());

        return  query.getResultList();
    }

}

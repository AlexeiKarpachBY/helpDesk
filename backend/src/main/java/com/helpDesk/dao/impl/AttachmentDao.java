package com.helpDesk.dao.impl;

import com.helpDesk.dao.AbstractDao;
import com.helpDesk.model.Attachment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttachmentDao extends AbstractDao<Attachment> {

    public AttachmentDao() {
        super();
        setClazz(Attachment.class);
    }

    public List<Attachment> getAttachmentsByTicketId(Long ticketId) {

           return getEntityManager()
                    .createQuery("FROM Attachment a WHERE a.ticket.id =:id", Attachment.class)
                    .setParameter("id", ticketId)
                    .getResultList();

    }

}

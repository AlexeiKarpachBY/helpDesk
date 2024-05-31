package com.helpDesk.dao.impl;

import com.helpDesk.model.Comment;
import com.helpDesk.dao.AbstractDao;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentDao extends AbstractDao<Comment> {

    public CommentDao() {
        super();
        setClazz(Comment.class);
    }

    public List<Comment> findAllCommentByTicketId(Long ticketId) {

        TypedQuery<Comment> query = getEntityManager().createNamedQuery("allCommentsByTicketId", Comment.class);
        query.setParameter("id", ticketId);

        return query.getResultList();
    }

}

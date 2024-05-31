package com.helpDesk.service.impl;

import com.helpDesk.converter.CommentConverter;
import com.helpDesk.model.Comment;
import com.helpDesk.model.Ticket;
import com.helpDesk.service.CommentServices;
import com.helpDesk.service.TicketService;
import com.helpDesk.service.UserService;
import com.helpDesk.dao.impl.CommentDao;
import com.helpDesk.dto.CommentDto;
import com.helpDesk.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentServicesImpl implements CommentServices {

    private final Logger logger = LoggerFactory.getLogger(CommentServicesImpl.class);

    private final CommentDao commentDao;
    private final UserService userService;
    private final TicketService ticketService;
    private final CommentConverter commentConverter;

    @Autowired
    public CommentServicesImpl(CommentDao commentDao, UserService userService, TicketService ticketService, CommentConverter commentConverter) {
        this.commentDao = commentDao;
        this.userService = userService;
        this.ticketService = ticketService;
        this.commentConverter = commentConverter;
    }

    @Override
    @Transactional
    public Comment addCommentToTicket(Long ticketId, CommentDto commentDto) {

        User user = userService.getCurrentUser();
        Ticket ticket = ticketService.getTicketById(ticketId);
        Comment comment = commentConverter.toEntity(commentDto);
        comment.setUser(user);
        comment.setTicket(ticket);
        comment.setDate(LocalDate.now());

        commentDao.save(comment);

        logger.info("Save comment with ID: " + comment.getId());

        return comment;
    }

    @Override
    @Transactional
    public List<Comment> getAllCommentsByTicketId(Long ticketId) {

        return commentDao.findAllCommentByTicketId(ticketId);

    }
}

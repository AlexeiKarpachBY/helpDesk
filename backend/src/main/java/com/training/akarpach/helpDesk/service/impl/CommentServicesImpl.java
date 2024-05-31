package com.training.akarpach.helpDesk.service.impl;

import com.training.akarpach.helpDesk.converter.CommentConverter;
import com.training.akarpach.helpDesk.dao.impl.CommentDao;
import com.training.akarpach.helpDesk.dto.CommentDto;
import com.training.akarpach.helpDesk.model.Comment;
import com.training.akarpach.helpDesk.model.Ticket;
import com.training.akarpach.helpDesk.model.User;
import com.training.akarpach.helpDesk.service.CommentServices;
import com.training.akarpach.helpDesk.service.TicketService;
import com.training.akarpach.helpDesk.service.UserService;
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

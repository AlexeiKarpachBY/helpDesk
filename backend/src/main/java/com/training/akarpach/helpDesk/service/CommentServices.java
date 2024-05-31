package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.dto.CommentDto;
import com.training.akarpach.helpDesk.model.Comment;

import java.util.List;

public interface CommentServices {

    Comment addCommentToTicket(Long ticketId, CommentDto commentDto);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

}

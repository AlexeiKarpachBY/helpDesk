package com.helpDesk.service;

import com.helpDesk.model.Comment;
import com.helpDesk.dto.CommentDto;

import java.util.List;

public interface CommentServices {

    Comment addCommentToTicket(Long ticketId, CommentDto commentDto);

    List<Comment> getAllCommentsByTicketId(Long ticketId);

}

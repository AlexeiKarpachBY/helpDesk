package com.training.akarpach.helpDesk.controller;

import com.training.akarpach.helpDesk.converter.CommentConverter;
import com.training.akarpach.helpDesk.dto.CommentDto;
import com.training.akarpach.helpDesk.model.Comment;
import com.training.akarpach.helpDesk.service.CommentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentServices commentServices;
    private final CommentConverter commentConverter;

    @Autowired
    public CommentController(CommentServices commentServices, CommentConverter commentConverter) {
        this.commentServices = commentServices;
        this.commentConverter = commentConverter;
    }

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestParam(value = "ticketId") Long ticketId,
                                           @Valid @RequestBody CommentDto commentDto) {

        Comment comment = commentServices.addCommentToTicket(ticketId, commentDto);

        return ResponseEntity.created(URI.create("comment/" + comment.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllCommentByTicketId(@RequestParam(value = "ticketId") Long ticketId) {

        List<Comment> commentList = commentServices.getAllCommentsByTicketId(ticketId);
        List<CommentDto> commentDtoList = commentConverter.toDtoList(commentList);

        return ResponseEntity.status(OK).body(commentDtoList);
    }

}

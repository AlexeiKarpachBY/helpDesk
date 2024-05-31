package com.helpDesk.converter;

import com.helpDesk.model.Comment;
import com.helpDesk.dto.CommentDto;
import com.helpDesk.util.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {

    private final DateFormatter dateFormatter;

    @Autowired
    public CommentConverter(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public Comment toEntity(CommentDto commentDto) {

        Comment comment = new Comment();
        comment.setText(commentDto.getText());

        return comment;
    }

    public CommentDto toDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        String date = dateFormatter.getDateForDto(comment.getDate());
        commentDto.setText(comment.getText());
        commentDto.setDate(date);
        commentDto.setUser(comment.getUser().getFirstName() + " " + comment.getUser().getLastName());

        return commentDto;
    }

    public List<CommentDto> toDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}

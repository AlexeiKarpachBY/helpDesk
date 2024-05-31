package com.helpDesk.converter;

import com.helpDesk.model.Attachment;
import com.helpDesk.dto.AttachmentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Component
public class AttachmentConverter {

    public AttachmentDto toDto(Attachment attachment) {

        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(valueOf(attachment.getId()));
        attachmentDto.setName(attachment.getName());

        return attachmentDto;
    }

    public List<AttachmentDto> toDtoList(List<Attachment> attachmentList) {
        return attachmentList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

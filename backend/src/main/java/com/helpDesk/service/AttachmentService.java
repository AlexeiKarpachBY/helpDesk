package com.helpDesk.service;

import com.helpDesk.model.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    void deleteAttachment(Long id);

    Attachment findAttachmentById(Long id);

    List<Attachment> findAttachmentByTicketId(Long ticketId);

    List<Attachment> uploadAttachment(MultipartFile[] multipartFiles);

}

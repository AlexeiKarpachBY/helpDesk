package com.training.akarpach.helpDesk.controller;

import com.training.akarpach.helpDesk.model.Attachment;
import com.training.akarpach.helpDesk.service.AttachmentService;
import com.training.akarpach.helpDesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    private final TicketService ticketService;
    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService, TicketService ticketService) {
        this.ticketService = ticketService;
        this.attachmentService = attachmentService;
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<Void> uploadAttachment(@PathVariable("ticketId") Long ticketId,
                                                 @RequestParam("file") MultipartFile[] multipartFiles) {

        List<Attachment> attachmentList = attachmentService.uploadAttachment(multipartFiles);
        ticketService.addAttachmentToTicket(ticketId, attachmentList);

        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadAttachment(@PathVariable("id") Long id) {

        Attachment attachment = attachmentService.findAttachmentById(id);
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String mimeType = fileTypeMap.getContentType(attachment.getName());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .body(new ByteArrayResource(attachment.getContent()));
    }

    @DeleteMapping("/{ticketId}/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable("ticketId") Long ticketId,
                                                 @PathVariable("attachmentId") Long attachmentId) {

        ticketService.removeAttachmentFromTicket(ticketId, attachmentId);

        return ResponseEntity.status(OK).build();
    }

}

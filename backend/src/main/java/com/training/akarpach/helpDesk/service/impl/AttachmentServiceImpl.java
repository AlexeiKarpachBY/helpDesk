package com.training.akarpach.helpDesk.service.impl;

import com.training.akarpach.helpDesk.dao.impl.AttachmentDao;
import com.training.akarpach.helpDesk.exception.WrongFileFormatException;
import com.training.akarpach.helpDesk.model.Attachment;
import com.training.akarpach.helpDesk.service.AttachmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final Set<String> fileFormat = new HashSet<>();

    private final AttachmentDao attachmentDao;

    @Autowired
    public AttachmentServiceImpl(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    @Override
    public List<Attachment> uploadAttachment(MultipartFile[] multipartFiles) {

        List<Attachment> temp = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            checkFileFormat(file);
            Attachment attachment = new Attachment();
            try {
                attachment.setName(file.getOriginalFilename());
                attachment.setContent(file.getBytes());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            temp.add(attachment);
        }

        return temp;
    }

    @Override
    @Transactional
    public List<Attachment> findAttachmentByTicketId(Long ticketId) {

        return attachmentDao.getAttachmentsByTicketId(ticketId);

    }

    @Override
    @Transactional
    public Attachment findAttachmentById(Long id) {

        return attachmentDao.findOne(id).orElseThrow(EntityNotFoundException::new);

    }

    @Override
    @Transactional
    public void deleteAttachment(Long id) {

        Attachment attachment = attachmentDao.findOne(id).orElseThrow();

        attachmentDao.delete(attachment);

    }

    private void checkFileFormat(MultipartFile file) {

        String[] temp = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

        if (!this.fileFormat.contains(temp[temp.length - 1].toLowerCase())) {
            throw new WrongFileFormatException("“The selected file type is not allowed. " +
                    "Please select a file of one of the following types: pdf, png, doc, docx, jpg, jpeg.”");
        }

        if (file.getSize() > 5000000) {
            throw new WrongFileFormatException("The size of the attached file should not be greater than 5 Mb. Please select another file.");
        }

    }

    @PostConstruct
    private void initFileFormat() {
        this.fileFormat.add("pdf");
        this.fileFormat.add("doc");
        this.fileFormat.add("docx");
        this.fileFormat.add("png");
        this.fileFormat.add("jpeg");
        this.fileFormat.add("jpg");
    }


}

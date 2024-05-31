package com.training.akarpach.helpDesk.service.impl;

import com.training.akarpach.helpDesk.enums.Role;
import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.model.Ticket;
import com.training.akarpach.helpDesk.model.User;
import com.training.akarpach.helpDesk.service.EmailService;
import com.training.akarpach.helpDesk.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

import static com.training.akarpach.helpDesk.enums.Role.ENGINEER;
import static com.training.akarpach.helpDesk.enums.State.*;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final UserService userService;

    private final JavaMailSender mailSender;

    private final TemplateEngine htmlTemplateEngine;

    @Autowired
    public EmailServiceImpl(UserService userService, JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Async
    @Override
    public void sentNotification(Ticket ticket, State oldState) {

        List<User> users = new ArrayList<>();
        String template = "";
        String subject = "";

        if (oldState.equals(DRAFT) || oldState.equals(DECLINED) && ticket.getState().equals(NEW)) {
            users = userService.getAllUserByRole(Role.MANAGER);
            template = "draftDeclinedToNew";
            subject = "New ticket for approval";
        }
        if (oldState.equals(NEW) && ticket.getState().equals(APPROVED)) {
            users = userService.getAllUserByRole(ENGINEER);
            users.add(ticket.getOwner());
            template = "newToApproved";
            subject = "Ticket was approved";
        }
        if (oldState.equals(NEW) && ticket.getState().equals(DECLINED)) {
            users.add(ticket.getOwner());
            template = "newToDeclined";
            subject = "Ticket was declined";
        }
        if (oldState.equals(NEW) && ticket.getState().equals(CANCELED)) {
            users.add(ticket.getOwner());
            users.add(ticket.getApprover());
            template = "newToCanceled";
            subject = "Ticket was cancelled";
        }
        if (oldState.equals(APPROVED) && ticket.getState().equals(CANCELED)) {
            users.add(ticket.getOwner());
            users.add(ticket.getApprover());
            template = "approvedToCanceled";
            subject = "Ticket was cancelled";
        }
        if (oldState.equals(IN_PROGRESS) && ticket.getState().equals(DONE)) {
            users.add(ticket.getOwner());
            template = "inProgressToDone";
            subject = "Ticket was done";
        }

        sendMessage(users, subject, template, ticket);

    }

    @Override
    public void sentFeedBackNotification(Ticket ticket) {

        String template = "feedback";
        String subject = "Feedback was provided";
        User user = ticket.getAssignee();
        MimeMessage mimeMessage = createMessage(user, subject, template, ticket);

        mailSender.send(mimeMessage);

    }

    private MimeMessage createMessage(User user, String subject, String template, Ticket ticket) {

        final Context context = new Context();
        String email = user.getEmail();

        context.setVariable("user", user);
        context.setVariable("ticket", ticket);

        final MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessage = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessage.setSubject(subject);
            mimeMessage.setTo(email);
            final String htmlContent = htmlTemplateEngine.process(template, context);
            mimeMessage.setText(htmlContent, true);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }

        return message;

    }

    private void sendMessage(List<User> list, String subject, String template, Ticket ticket) {

        for (User user : list) {
            MimeMessage mimeMessage = createMessage(user, subject, template, ticket);
            mailSender.send(mimeMessage);
        }

    }

}

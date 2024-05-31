package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.enums.State;
import com.training.akarpach.helpDesk.model.Ticket;

public interface EmailService {

    void sentNotification(Ticket ticket, State previousState);

    void sentFeedBackNotification(Ticket ticket);

}

package com.helpDesk.service;

import com.helpDesk.model.Ticket;
import com.helpDesk.enums.State;

public interface EmailService {

    void sentNotification(Ticket ticket, State previousState);

    void sentFeedBackNotification(Ticket ticket);

}

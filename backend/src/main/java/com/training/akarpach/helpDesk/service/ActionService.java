package com.training.akarpach.helpDesk.service;

import com.training.akarpach.helpDesk.model.Ticket;

public interface ActionService {

    void doAction(String action, Ticket ticket);

}

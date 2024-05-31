package com.helpDesk.service;

import com.helpDesk.model.Ticket;

public interface ActionService {

    void doAction(String action, Ticket ticket);

}

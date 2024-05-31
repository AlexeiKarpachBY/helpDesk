package com.helpDesk.enums;

import com.helpDesk.model.Ticket;
import com.helpDesk.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.helpDesk.enums.Action.*;
import static com.helpDesk.enums.Role.ENGINEER;
import static com.helpDesk.enums.Role.MANAGER;

public enum State {

    APPROVED {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            List<Action> emptyAction = new ArrayList<>();
            if (user.getRole() == ENGINEER) {
                return new ArrayList<>(List.of(ASSIGN_TO_ME, CANCEL));
            } else return emptyAction;
        }

    },

    DECLINED() {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            List<Action> emptyAction = new ArrayList<>();
            if (user.equals(ticket.getOwner())) {
                return new ArrayList<>(List.of(SUBMIT, CANCEL));
            } else return emptyAction;
        }

    },

    DONE {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            List<Action> emptyAction = new ArrayList<>();
            if (user.equals(ticket.getOwner()) && ticket.getFeedback() == null) {
                return new ArrayList<>(Collections.singleton(LEAVE_FEEDBACK));
            } else return emptyAction;
        }

    },
    DRAFT {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            List<Action> emptyAction = new ArrayList<>();
            if (user.equals(ticket.getOwner())) {
                return new ArrayList<>(List.of(SUBMIT, CANCEL));
            } else return emptyAction;
        }

    },

    CANCELED {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            return new ArrayList<>();

        }

    },

    IN_PROGRESS {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            List<Action> emptyAction = new ArrayList<>();
            if (user.getRole() == ENGINEER) {
                return new ArrayList<>(List.of(Action.DONE));
            } else return emptyAction;
        }

    },

    NEW {
        @Override
        public List<Action> getActionForState(Ticket ticket, User user) {

            List<Action> emptyAction = new ArrayList<>();
            if (user.getRole() == MANAGER) {
                return new ArrayList<>(List.of(APPROVE, DECLINE, CANCEL));
            } else return emptyAction;
        }

    };

    public abstract List<Action> getActionForState(Ticket ticket, User user);

}

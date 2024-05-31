package com.helpDesk.enums;

public enum Action {

    SUBMIT(State.NEW),

    APPROVE(State.APPROVED),

    DECLINE(State.DECLINED),

    CANCEL(State.CANCELED),

    ASSIGN_TO_ME(State.IN_PROGRESS),

    DONE(State.DONE),

    LEAVE_FEEDBACK(null);

    private final State nextState;

    Action(State nextState) {
        this.nextState = nextState;
    }

    public State getNextState() {
        return nextState;
    }
}



package com.training.akarpach.helpDesk.enums;

import static com.training.akarpach.helpDesk.enums.State.*;

public enum Action {

    SUBMIT(NEW),

    APPROVE(APPROVED),

    DECLINE(DECLINED),

    CANCEL(CANCELED),

    ASSIGN_TO_ME(IN_PROGRESS),

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



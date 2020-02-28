package com.official.aptoon.event;

/**
 * Created by Tamim on 28/09/2019.

 */

public class CastSessionEndedEvent {

    private long sessionRemainingTime;

    public CastSessionEndedEvent() {

    }

    public CastSessionEndedEvent(long sessionRemainingTime) {
        this.sessionRemainingTime = sessionRemainingTime;
    }

    public long getSessionRemainingTime() {
        return sessionRemainingTime;
    }

    public void setSessionRemainingTime(long sessionRemainingTime) {
        this.sessionRemainingTime = sessionRemainingTime;
    }
}

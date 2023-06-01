package com.vishal.practice.apiratelimiter;

import java.time.Instant;

/**
 * @author Vishal Joshi
 */
public class TimeStamp {

    private final Instant timeStamp;

    public TimeStamp(Instant timeStamp) {

        this.timeStamp = timeStamp;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }
}

package com.vishal.practice.apiratelimiter;

import java.time.Instant;

/**
 * @author Vishal Joshi
 */
public class TimeStampCreator {

    public TimeStamp create(){
        return new TimeStamp(Instant.now());
    }
}

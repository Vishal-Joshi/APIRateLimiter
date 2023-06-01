package com.vishal.practice.apiratelimiter;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Vishal Joshi
 */
public class DurationService {

    public boolean isWithinDuration(TimeStamp compare, int limitInSeconds){
        return Duration.between(compare.getTimeStamp(), Instant.now()).toSeconds() <= limitInSeconds;
    }
}

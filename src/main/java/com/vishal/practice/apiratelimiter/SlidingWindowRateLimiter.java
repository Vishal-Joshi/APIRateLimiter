package com.vishal.practice.apiratelimiter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Vishal Joshi
 */
public class SlidingWindowRateLimiter implements APIRateLimiter {

    private final DurationService durationService;
    private final TimeStampCreator timeStampCreator;
    private final Map<Integer, LinkedList<TimeStamp>> requestVsTimeStamps = new HashMap<>();
    private final int rateLimit;
    private final int timeLimitInSeconds;

    public SlidingWindowRateLimiter(DurationService durationService, TimeStampCreator timeStampCreator, int rateLimit, int timeLimitInSeconds) {
        this.durationService = durationService;
        this.timeStampCreator = timeStampCreator;
        this.rateLimit = rateLimit;
        this.timeLimitInSeconds = timeLimitInSeconds;
    }

    @Override
    public boolean allowed(int customerId) {

        LinkedList<TimeStamp> timeStampsForCustomer = requestVsTimeStamps.get(customerId);
        if (timeStampsForCustomer != null) {
            if (timeStampsForCustomer.size() >= rateLimit) {
                for (int i= 0; i<timeStampsForCustomer.size(); i++) {
                    TimeStamp timeStamp = timeStampsForCustomer.get(i);
                    if (!durationService.isWithinDuration(timeStamp, timeLimitInSeconds)) {
                        timeStampsForCustomer.remove(i);
                    } else {
                        break;
                    }
                }
            }

            if (timeStampsForCustomer.size() < rateLimit) {
                timeStampsForCustomer.addLast(timeStampCreator.create());
                return true;
            }
            return false;

        } else {
            LinkedList<TimeStamp> timestamps = new LinkedList<>();
            timestamps.add(timeStampCreator.create());
            requestVsTimeStamps.put(customerId, timestamps);
            return true;
        }
    }
}

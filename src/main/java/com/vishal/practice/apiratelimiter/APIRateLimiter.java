package com.vishal.practice.apiratelimiter;

/**
 * @author Vishal Joshi
 */
public interface APIRateLimiter {

    boolean allowed(int customerId);
}

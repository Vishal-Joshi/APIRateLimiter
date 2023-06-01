package com.vishal.practice.apiratelimiter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vishal Joshi
 */
class SlidingWindowRateLimiterTest {

    private final DurationService mockDurationService = mock(DurationService.class);
    private final TimeStampCreator mockTimeStampCreator = mock(TimeStampCreator.class);

    private final int rateLimit = 3;
    private final int timeLimitInSeconds = 60;
    private final SlidingWindowRateLimiter limiter = new SlidingWindowRateLimiter(mockDurationService, mockTimeStampCreator, rateLimit, timeLimitInSeconds);

    @Test
    void testShouldVerifyThatIfRequestComesFromNewCustomerItShouldBeAllowed() {
        //given
        int customerId = 1;

        //when
        boolean allowed = limiter.allowed(customerId);

        //then
        assertTrue(allowed);

    }

    @Test
    void testShouldVerifyThatIfRequestComesFromAlreadyCustomerMoreThanLimitItShouldNOTBeAllowed() {
        //given
        int customerId = 1;
        TimeStamp timestampForRequest1 = mock(TimeStamp.class);
        TimeStamp timestampForRequest2 = mock(TimeStamp.class);
        TimeStamp timestampForRequest3 = mock(TimeStamp.class);
        TimeStamp timestampForRequest4 = mock(TimeStamp.class);
        when(mockTimeStampCreator.create())
                .thenReturn(timestampForRequest1)
                .thenReturn(timestampForRequest2)
                .thenReturn(timestampForRequest3)
                .thenReturn(timestampForRequest4);

        when(mockDurationService.isWithinDuration(timestampForRequest1, timeLimitInSeconds))
                .thenReturn(true);

        when(mockDurationService.isWithinDuration(timestampForRequest2, timeLimitInSeconds))
                .thenReturn(true);
        when(mockDurationService.isWithinDuration(timestampForRequest3, timeLimitInSeconds))
                .thenReturn(true);
        when(mockDurationService.isWithinDuration(timestampForRequest4, timeLimitInSeconds))
                .thenReturn(true);


        //when
        limiter.allowed(customerId);
        limiter.allowed(customerId);
        limiter.allowed(customerId);
        boolean allowed = limiter.allowed(customerId);

        //then
        assertFalse(allowed);

    }

    @Test
    void testShouldVerifyThatIfMultipleRequestWithinLimitShouldBeAllowed() {
        //given
        int customerId = 1;
        TimeStamp timestampForRequest1 = mock(TimeStamp.class);
        TimeStamp timestampForRequest2 = mock(TimeStamp.class);
        TimeStamp timestampForRequest3 = mock(TimeStamp.class);
        when(mockTimeStampCreator.create())
                .thenReturn(timestampForRequest1)
                .thenReturn(timestampForRequest2)
                .thenReturn(timestampForRequest3);


        //when
        limiter.allowed(customerId);
        limiter.allowed(customerId);
        boolean allowed = limiter.allowed(customerId);

        //then
        assertTrue(allowed);

    }

    @Test
    void testShouldVerifyThatIfForMultipleRequestTheOldestDurationHasLapsedThenRequestShouldBeAllowedEvenNumberOfRequestAreMoreThanRateLimit() {
        //given
        int customerId = 1;
        TimeStamp timestampForRequest1 = mock(TimeStamp.class);
        TimeStamp timestampForRequest2 = mock(TimeStamp.class);
        TimeStamp timestampForRequest3 = mock(TimeStamp.class);
        TimeStamp timestampForRequest4 = mock(TimeStamp.class);
        when(mockTimeStampCreator.create())
                .thenReturn(timestampForRequest1)
                .thenReturn(timestampForRequest2)
                .thenReturn(timestampForRequest3)
                .thenReturn(timestampForRequest4);

        when(mockDurationService.isWithinDuration(timestampForRequest1, timeLimitInSeconds))
                .thenReturn(false);

        when(mockDurationService.isWithinDuration(timestampForRequest2, timeLimitInSeconds))
                .thenReturn(true);
        when(mockDurationService.isWithinDuration(timestampForRequest3, timeLimitInSeconds))
                .thenReturn(true);
        when(mockDurationService.isWithinDuration(timestampForRequest4, timeLimitInSeconds))
                .thenReturn(true);

        //when
        limiter.allowed(customerId);
        limiter.allowed(customerId);
        limiter.allowed(customerId);
        boolean allowed = limiter.allowed(customerId);

        //then
        assertTrue(allowed);

    }



}
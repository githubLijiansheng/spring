package com.example.demo.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author jiansheng.li
 * @date 2020/12/29 14:09
 */
@Service("service")
public class RetryService {
    @Retryable(value = IllegalAccessException.class, maxAttempts = 5,
            backoff= @Backoff(value = 1500, maxDelay = 100000, multiplier = 1.2))
    public void service() throws IllegalAccessException {
        System.out.println("service method...");
        throw new IllegalAccessException("manual exception");
    }

    @Recover
    public void recover(IllegalAccessException e){
        System.out.println("service retry after Recover => " + e.getMessage());
    }
}


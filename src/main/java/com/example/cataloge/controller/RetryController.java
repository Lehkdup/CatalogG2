package com.example.cataloge.controller;

import com.example.cataloge.service.RetryService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {
    @Autowired
    private RetryService retryService;
    @GetMapping("/retry")
    @Retry(name = "retryApi", fallbackMethod = "fallbackAfterRetry")
    public String retryApi(){
        return retryService.callRetryApi();
    }

    private String fallbackAfterRetry(Exception exception){
        return "retries didn't work";
    }
}

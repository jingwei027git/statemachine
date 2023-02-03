package net.jingwei027.ssm.sample.config;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderScheduler {
    
    @Scheduled(initialDelay = 5L, fixedDelay = 10L, timeUnit = TimeUnit.SECONDS)
    public void heartbeat() {
        
    }

}

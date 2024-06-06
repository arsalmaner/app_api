package com.app_api.service.schedular;

import com.app_api.service.BaseService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SchedularService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    private void postConstruct() {
        startBackgroundService();
    }

    public void startBackgroundService() {
        logger.warn("startBackgroundService");

        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(1);

        Runnable exampleRunnableMethod = exampleRunnableMethod();

        threadPool.scheduleWithFixedDelay(exampleRunnableMethod, 10, 10, TimeUnit.DAYS);
    }

    private Runnable exampleRunnableMethod() {
        return () -> {
            logger.warn("exampleRunnableMethod");
            //functionality here
        };
    }

}


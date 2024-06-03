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
public class SchedularService extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    private void postConstruct() {
        startBackgroundService();
    }

    public void startBackgroundService() {
        logger.warn("startBackgroundService");

        ScheduledThreadPoolExecutor threadPool = new ScheduledThreadPoolExecutor(3);

        Runnable exampleRunnableMethod = exampleRunnableMethod();

        threadPool.scheduleWithFixedDelay(exampleRunnableMethod, 10, 10, TimeUnit.DAYS);
        threadPool.scheduleWithFixedDelay(exampleRunnableMethod, calculateInitialDelayExample(), 168, TimeUnit.HOURS); // 24 * 7
    }

    private Runnable exampleRunnableMethod() {
        return () -> {
            logger.warn("exampleRunnableMethod");
            //functionality here
        };
    }

    private int calculateInitialDelayExample() {
        HashMap<Integer, Integer> dayToDelay = new HashMap<>();
        dayToDelay.put(Calendar.THURSDAY, 6);
        dayToDelay.put(Calendar.FRIDAY, 5);
        dayToDelay.put(Calendar.SATURDAY, 4);
        dayToDelay.put(Calendar.SUNDAY, 3);
        dayToDelay.put(Calendar.MONDAY, 2);
        dayToDelay.put(Calendar.TUESDAY, 1);
        dayToDelay.put(Calendar.WEDNESDAY, 0);

        ZoneId zoneId = ZoneId.of("America/New_York");
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        int dayOfWeek = now.getDayOfWeek().getValue();
        int hour = now.getHour();

        int delayInDays = dayToDelay.get(dayOfWeek);
        int delayInHours = 0;

        int targetHour = 20; // 8 pm
        if (delayInDays == 6 && hour < targetHour) {
            delayInHours = targetHour - hour;
        } else {
            delayInHours = delayInDays * 24 + ((24 - hour) + targetHour);
        }

        return delayInHours;
    }
}


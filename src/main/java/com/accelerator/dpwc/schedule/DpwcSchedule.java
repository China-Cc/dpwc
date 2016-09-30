package com.accelerator.dpwc.schedule;

import com.accelerator.dpwc.service.DpwcService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration @EnableScheduling
public class DpwcSchedule {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Resource
    private DpwcService dpwcService;

    @Scheduled(cron = "0 30 7 * * ? ")
    public void clockInScheduler() {
        executorService.schedule(new clockRunner(true), // 20分钟内随机挑时间执行
                RandomUtils.nextInt(0, 20 * 60 + 1), TimeUnit.SECONDS);
    }

    @Scheduled(cron = "0 30 21 * * ? ")
    public void clockOutScheduler() {
        executorService.schedule(new clockRunner(false), // 20分钟内随机挑时间执行
                RandomUtils.nextInt(0, 20 * 60 + 1), TimeUnit.SECONDS);
    }

    private class clockRunner implements Runnable {

        private boolean isClockIn;

        public clockRunner(boolean isClockIn) {
            this.isClockIn = isClockIn;
        }

        @Override
        public void run() {
            String clockDisplay = isClockIn ? "上班" : "下班";
            logger.info("开始执行{}打卡", clockDisplay);
            dpwcService.schedule(isClockIn);
            logger.info("结束执行{}打卡", clockDisplay);
        }
    }

}

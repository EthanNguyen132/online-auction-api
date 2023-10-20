package edu.miu.waa.onlineauctionapi.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledBidService {
  private static final Logger log = LoggerFactory.getLogger(ScheduledBidService.class);

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Scheduled(
      fixedRateString = "${app.scheduling.task.interval}",
      initialDelayString = "${app.scheduling.task.initialDelay}")
  public void settleBid() {
    log.info("Settling all completed bid at {}", dateFormat.format(new Date()));
  }
}

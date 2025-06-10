package ru.delivery.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PackingOrderScheduler {

  private final DispatcherService dispatcherService;

//  @Scheduled(fixedRate = 5 * 60 * 1000)
@Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10)
  public void assignPackingOrdersToCouriers() {
    log.info("PackingOrderScheduler started");
    dispatcherService.assignOrdersToCouriers();
    log.info("PackingOrderScheduler finished");
  }

}

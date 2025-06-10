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

  @Scheduled(
      initialDelayString = "${scheduler.assign-initial-delay-ms}",
      fixedDelayString = "${scheduler.assign-delay-ms}")
  public void assignPackingOrdersToCouriers() {
    log.info("PackingOrderScheduler started");
    dispatcherService.assignOrdersToCouriers();
    log.info("PackingOrderScheduler finished");
  }

}

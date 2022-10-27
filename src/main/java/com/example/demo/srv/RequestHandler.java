package com.example.demo.srv;

import com.example.demo.domain.request.VolunteerRequestDetails;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestHandler {

  public void run(VolunteerRequestDetails jobRequest) {
    log.info("Processing request ({}) instantly...", jobRequest);
  }

  public void schedule(LocalDateTime delayUntil, VolunteerRequestDetails jobRequest) {
    log.info("Will process request ({}) on {}", jobRequest, delayUntil);
    log.info("Waiting....");
  }
}

package com.example.demo.controller;

import com.example.demo.domain.request.Request;
import com.example.demo.domain.request.VolunteerRequestDetails;
import com.example.demo.srv.RequestCenter;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

  private final RequestCenter requestCenter;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Request performRequest(
      @RequestBody VolunteerRequestDetails jobRequest,
      @RequestParam String username,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      @RequestParam(required = false) LocalDateTime delayUntil
  ) {

    return requestCenter.process(username, jobRequest, delayUntil);
  }

  @PostMapping("/instant-batch")
  @ResponseStatus(HttpStatus.CREATED)
  public void performRequest(
      @RequestBody VolunteerRequestDetails jobRequest,
      @RequestParam String username,
      @RequestParam int count,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      @RequestParam(required = false) LocalDateTime delayUntil
  ) {
    IntStream.range(0, count)
        .forEach(__ -> requestCenter.process(username, jobRequest, delayUntil));
  }
}

package com.example.demo.domain.request;

import java.time.LocalDate;
import java.util.UUID;

public record VolunteerRequestDetails(
    UUID uniqueId,
    LocalDate deadline,
    RequestType requestType,
    String comment
) {

  public VolunteerRequestDetails {
    if (uniqueId == null) {
      uniqueId = UUID.randomUUID();
    }
  }

  public enum RequestType {
    MEDICINE,
    WEAPON,
    VEHICLE
  }
}

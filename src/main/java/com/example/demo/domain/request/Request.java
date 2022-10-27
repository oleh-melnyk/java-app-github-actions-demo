package com.example.demo.domain.request;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Request {

  @Id
  private String id;
  private VolunteerRequestDetails details;

  public static Request build(VolunteerRequestDetails details) {
    var request = new Request();
    request.setDetails(details);
    return request;
  }
}

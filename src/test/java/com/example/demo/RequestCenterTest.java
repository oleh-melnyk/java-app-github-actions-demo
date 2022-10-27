package com.example.demo;

import static org.mockito.ArgumentMatchers.any;

import com.example.demo.domain.request.Request;
import com.example.demo.domain.request.RequestRepository;
import com.example.demo.domain.request.VolunteerRequestDetails;
import com.example.demo.domain.request.VolunteerRequestDetails.RequestType;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.srv.RequestCenter;
import com.example.demo.srv.RequestHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RequestCenterTest {

  @InjectMocks
  private RequestCenter requestCenter;
  @Mock
  private UserRepository userRepository;
  @Mock
  private RequestRepository requestRepository;
  @Mock
  private RequestHandler requestHandler;

  @Test
  public void testInstantRequest() {
    UUID id = UUID.randomUUID();
    var requestDetails = new VolunteerRequestDetails(id, LocalDate.of(2022, 10, 30), RequestType.WEAPON, "HIMARS");

    requestCenter.process("user1@test.com", requestDetails, null);

    Mockito.verify(userRepository, Mockito.times(1)).save(User.from("user1@test.com"));
    Mockito.verify(requestRepository, Mockito.times(1)).save(Request.build(requestDetails));
    Mockito.verify(requestHandler, Mockito.times(1)).run(requestDetails);
    Mockito.verify(requestHandler, Mockito.never()).schedule(any(), any());
  }

  @Test
  public void testScheduledRequest() {

    UUID id = UUID.randomUUID();
    var requestDetails = new VolunteerRequestDetails(id, LocalDate.of(2022, 10, 30), RequestType.WEAPON, "HIMARS");

    LocalDateTime delayUntil = LocalDateTime.now().plusDays(3);
    requestCenter.process("user1@test.com", requestDetails, delayUntil);

    Mockito.verify(userRepository, Mockito.times(1)).save(User.from("user1@test.com"));
    Mockito.verify(requestRepository, Mockito.times(1)).save(Request.build(requestDetails));
    Mockito.verify(requestHandler, Mockito.times(1)).schedule(delayUntil, requestDetails);
    Mockito.verify(requestHandler, Mockito.never()).run(any());
  }
}

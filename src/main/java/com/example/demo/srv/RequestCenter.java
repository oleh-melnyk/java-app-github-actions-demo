package com.example.demo.srv;

import com.example.demo.domain.request.Request;
import com.example.demo.domain.request.RequestRepository;
import com.example.demo.domain.request.VolunteerRequestDetails;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestCenter {

  private final UserRepository userRepository;
  private final RequestRepository requestRepository;
  private final RequestHandler requestHandler;

  public Request process(String username, @NonNull VolunteerRequestDetails jobRequest, @Nullable LocalDateTime delayUntil) {
    if (!userRepository.existsByUsername(username)) {
      userRepository.save(User.from(username));
    }

    Request savedRequest = requestRepository.save(Request.build(jobRequest));

    if (delayUntil == null) {
      requestHandler.run(jobRequest);
      return savedRequest;
    }

    requestHandler.schedule(delayUntil, jobRequest);

    return savedRequest;
  }
}

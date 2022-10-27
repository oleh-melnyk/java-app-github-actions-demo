package com.example.demo.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  private String id;
  private String username;

  public static User from(String username) {
    User user = new User();
    user.setUsername(username);
    return user;
  }
}

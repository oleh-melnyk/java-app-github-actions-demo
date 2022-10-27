package com.example.demo.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  boolean existsByUsername(String username);
}

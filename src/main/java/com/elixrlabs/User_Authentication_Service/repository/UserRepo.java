package com.elixrlabs.User_Authentication_Service.repository;

import com.elixrlabs.User_Authentication_Service.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;


public interface UserRepo extends MongoRepository<Users, UUID> {
    Users findByUsername(String username);
}

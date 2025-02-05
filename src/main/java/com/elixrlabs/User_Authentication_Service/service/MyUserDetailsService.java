package com.elixrlabs.User_Authentication_Service.service;

import com.elixrlabs.User_Authentication_Service.constants.UserConstant;
import com.elixrlabs.User_Authentication_Service.model.Users;
import com.elixrlabs.User_Authentication_Service.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            logger.info("Loading user details for username: {}", username);
            Users user = userRepo.findByUsername(username);
            if (user == null) {
                logger.warn("User not found: {}", username);
                throw new UsernameNotFoundException(UserConstant.USER_NOT_FOUND);
            }
            logger.debug("User details found for username: {}", username);
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        } catch (Exception e) {
            logger.error("Error loading user by username: {}", e.getMessage());
            throw new RuntimeException("Error fetching user details");
        }
    }

}
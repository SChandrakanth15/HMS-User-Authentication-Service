package com.elixrlabs.User_Authentication_Service.controller;

import com.elixrlabs.User_Authentication_Service.constants.ApiPathsConstant;
import com.elixrlabs.User_Authentication_Service.constants.UserConstant;
import com.elixrlabs.User_Authentication_Service.dto.LoginResponse;
import com.elixrlabs.User_Authentication_Service.model.Users;
import com.elixrlabs.User_Authentication_Service.service.JwtService;
import com.elixrlabs.User_Authentication_Service.service.MyUserDetailsService;
import com.elixrlabs.User_Authentication_Service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathsConstant.USERS_BASE)
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final MyUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService, MyUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(ApiPathsConstant.LOGIN_URL)
    public ResponseEntity<Object> login(@RequestBody Users users) {
        String response = userService.verify(users);
        if (response.equals(UserConstant.USER_NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserConstant.USER_IS_NOT_REGISTERED);
        } else if (response.equals(UserConstant.INVALID_PASSWORD)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserConstant.THE_PASSWORD_IS_INCORRECT);
        } else {
            LoginResponse loginResponse = LoginResponse.builder()
                    .username(users.getUsername())
                    .token(response)
                    .build();
            // Return 200 OK with the JWT token
            return ResponseEntity.ok(loginResponse); // response contains the JWT token
        }
    }

    @PostMapping(ApiPathsConstant.VERIFY_URL)
    public ResponseEntity<String> verifyToken(@RequestHeader(UserConstant.AUTHORIZATION) String token) {
        try {
            String jwtToken = token.substring(7);  // Extract the token (removing "Bearer " prefix)
            String username = jwtService.extractUserName(jwtToken); // Extract username from JWT

            if (username != null) {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                boolean isValid = jwtService.validateToken(jwtToken, user);
                return isValid ? ResponseEntity.ok(username) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserConstant.TOKEN_IS_INVALID);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserConstant.TOKEN_IS_INVALID);
            }
        } catch (Exception e) {
            logger.error("Error verifying token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserConstant.TOKEN_IS_INVALID);
        }
    }
}
package com.elixrlabs.User_Authentication_Service.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

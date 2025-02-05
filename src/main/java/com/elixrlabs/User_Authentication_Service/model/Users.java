package com.elixrlabs.User_Authentication_Service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private String username;
    private String password;
    private boolean active;
}

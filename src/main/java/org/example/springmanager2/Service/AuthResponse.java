package org.example.springmanager2.Service;

import lombok.Data;
import org.example.springmanager2.Entity.Enums.ROLES;

@Data
public class AuthResponse {

    private String token ;
    private ROLES userRole ;

    public AuthResponse(String token, ROLES role)
    {
        this.token = token ;
        userRole =role;
    }
}

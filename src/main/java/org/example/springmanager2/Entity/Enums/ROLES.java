package org.example.springmanager2.Entity.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.security.core.GrantedAuthority;

public enum ROLES implements GrantedAuthority {
    ADMIN ,
    COMPTABLE ,
    CLIENT;

    @Override
    public String getAuthority(){
        return name();
    }



}

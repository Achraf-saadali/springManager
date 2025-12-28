package org.example.springmanager2.CredentialsSchema;

import lombok.*;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@Setter
@Getter

public class ExtraCredentials {
    private String userPassword ;
    private String additionalCode ;
    private ROLES userRole ;


    public boolean equals(Object ob ,BCryptPasswordEncoder encoder)
    {
        if (this == ob) return true ;
        if (ob == null) return false ;
        ExtraCredentials credentials = (ExtraCredentials) ob ;

        return  additionalCode.equals(credentials.getAdditionalCode()) && (encoder.matches(userPassword , credentials.getUserPassword()));
    }



}

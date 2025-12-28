package org.example.springmanager2.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.EnumSet;
import java.util.Set;


@Entity
@Table(name = "admin")
@NoArgsConstructor
@Getter
@Setter


public class Admin extends Personne {

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private  ROLES userRole = ROLES.ADMIN ;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return EnumSet.of(ROLES.ADMIN);
    }


    public Object getCredentials()
    {
            return this.getUserPassword() ;
    }



    @Override
    public String toString()
    {
        return "UserName =="+getUserName() +"\n"+
                " UserEmail =="+getUsername() +"\n"+
                " UserPassword =="+getPassword()+"\n"+
                " UserRole == "+getUserRole();

    }



    }




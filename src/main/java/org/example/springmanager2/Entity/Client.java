package org.example.springmanager2.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.springmanager2.CredentialsSchema.ExtraCredentials;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.EnumSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="client")
public class Client extends Personne {

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private  ROLES userRole = ROLES.CLIENT  ;


    @Column(name="client_code")
    private String clientCode ;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return EnumSet.of(

                ROLES.CLIENT
                         );
    }
        @Override
        public Object getCredentials()
        {
            return new ExtraCredentials
                    (this.getUserPassword(),
                            this.getClientCode(),
                            ROLES.CLIENT
                    );



        }


    }






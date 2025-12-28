package org.example.springmanager2.Entity;
import jakarta.persistence.*;
import lombok.* ;
import org.example.springmanager2.CredentialsSchema.ExtraCredentials;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "comptable")


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comptable extends Personne {

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private  ROLES userRole = ROLES.COMPTABLE  ;

    @Column(name="comptable_code")
    private String comptableCode ;


    public String toString(){
        return   super.toString() +"\n"+
                "comptableClient == "+comptableCode +"\n"+
                "userRole == "+userRole+"\n";


    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return EnumSet.of(

                ROLES.COMPTABLE
        );
    }


        @Override
        public Object getCredentials()
        {
            return new ExtraCredentials
                    (this.getUserPassword(),
                            this.getComptableCode(),
                            ROLES.COMPTABLE
                    );



        }





}

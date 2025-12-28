package org.example.springmanager2.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.springmanager2.Entity.Annotations.ComplexIntId;
import jakarta.persistence.*;
import lombok.* ;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.EnumSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "userRole")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = Comptable.class, name = "COMPTABLE"),
        @JsonSubTypes.Type(value = Admin.class , name = "ADMIN")
})
public  class Personne  implements UserDetails {
    @Id
    @GeneratedValue(generator = "complex-int-id")
    @ComplexIntId(start = 1000)
    @GenericGenerator(name = "complex-int-id"
                , strategy = "org.example.springmanager2.Entity.Annotations.ComplexIntIdGenerator")

    @Column(name = "user_id")

    private Integer userId ;

    @Column(name = "user_email")
    private String userEmail ;

    @Column(name = "user_password")
    private String userPassword ;

    @Column(name = "user_name")
    private String userName ;







    @Override
    public String getUsername()
    {
        return userEmail ;
    }

    public String getUserName()
    {
        return userName;
    }

    @Override
    public String getPassword()
    {
        return userPassword ;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;

    }

    public Object getCredentials()
    {
        return userPassword ;
    }










}

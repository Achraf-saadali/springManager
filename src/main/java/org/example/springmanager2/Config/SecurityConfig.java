package org.example.springmanager2.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.example.springmanager2.Service.CommonService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("i am in security filter chain");
        http
                .csrf(csrf->csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                                        .requestMatchers(
                                                "/api/auth/**",
                                                "/password/**",
                                                "/api/email/**"
                                        ).permitAll()
                        .requestMatchers("/admin/**","produits/delete-product").hasAuthority("ADMIN")
                        .requestMatchers("/comptable/**").hasAnyAuthority("ADMIN","COMPTABLE")
                        .requestMatchers("/client/**").hasAnyAuthority("ADMIN" ,"CLIENT","COMPTABLE")
                        .requestMatchers("/produits/some","produits/all").hasAnyAuthority("ADMIN" ,"CLIENT","COMPTABLE")
                        .requestMatchers("/factures/create").hasAnyAuthority("ADMIN","COMPTABLE")
                        .requestMatchers("/password/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("in Security config");

        return http.build();
    }
 @Bean
  public BCryptPasswordEncoder encoder()
 {
     return new BCryptPasswordEncoder() ;
 }

}


package org.example.springmanager2.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springmanager2.CredentialsSchema.*;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Service.AdminService;
import org.example.springmanager2.Service.ClientService;
import org.example.springmanager2.Service.ComptableService;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RolesRouter rolesRouter;




    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   RolesRouter rolesRouter) {
        System.out.println("JwtAuthenticationFilter" +
                " constructor called. rolesRouter = " + rolesRouter);


        this.jwtUtil = jwtUtil;
        this.rolesRouter = rolesRouter ;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("i am in  filter internal");
        // Skip JWT check for login and public endpoints
        String path = request.getServletPath();
        if (path.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("header is == "+authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);
            String userEmail = jwtUtil.extractUsername(token);
            ROLES role = jwtUtil.extractRole(token);
            Personne userDetails = null;
            Object credentials = null;
            System.out.println("email is == "+userEmail);
            if (userEmail != null &&
                    SecurityContextHolder.getContext().getAuthentication()
                            == null &&
                    jwtUtil.isTokenValid(token)) {

                userDetails = (Personne) rolesRouter.load(role, userEmail);
                credentials = userDetails.getCredentials();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                userDetails.getCredentials(),
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response); // always continue the chain
    }



    }



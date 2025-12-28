package org.example.springmanager2.Controller;


import lombok.Data;
import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.CredentialsSchema.*;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.NotFoundException;
import org.example.springmanager2.Service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RolesRouter rolesRouter;

    private final JwtUtil jwtUtil ;
    private AuthenticationManager authenticationManager;









    public AuthController(RolesRouter rolesRouter,

                          JwtUtil jwtUtil,
                          AuthenticationManager authManager)
    {

        this.jwtUtil = jwtUtil ;

        this.rolesRouter = rolesRouter;

        this.authenticationManager =authManager ;
    }





    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Personne P) throws Exception
    {
       return switch(P)
        {
            case Admin A -> login(A) ;
            case Client C ->login(C);
            case Comptable C ->login(C);
            default-> throw new NotFoundException("Invalid Role");

        };
    }


    public  void logout(@RequestBody Personne P)
    {

    }












    @PostMapping("/Posttest")
    public String POSTTEST(@RequestBody Admin admin )
    {

        return null;
    }



    private ResponseEntity<?> login( Admin admin)
    {

        System.out.println("our admin is =="+admin);

        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken
                        (admin.getUserEmail(),admin.getUserPassword() )
                );

        // If authentication succeeds → generate JWT
        String token = jwtUtil.generateToken
                (admin.getUserEmail(), ROLES.ADMIN
                );
        return ResponseEntity.ok(new AuthResponse(token,ROLES.ADMIN));



    }
    @PostMapping("/send-link")
    public ResponseEntity<Status> sendLink
            (@RequestParam("email") String email ,
             @RequestParam("role") ROLES role)
    {

      return null ;
    }
    private ResponseEntity<?> login(Client client )
    {

        ExtraCredentials clientpasswords
                = new ExtraCredentials
                (client.getPassword(),
                        client.getClientCode(),
                        ROLES.CLIENT
                );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (client.getUserEmail(),clientpasswords )
        );


        String token = jwtUtil.generateToken
                (client.getUserEmail(),ROLES.CLIENT);
        return ResponseEntity.ok(new AuthResponse(token,ROLES.CLIENT));


    }
    private ResponseEntity<?> login
            (Comptable comptable )
    {



        ExtraCredentials comptablepasswords
                = new ExtraCredentials
                (comptable.getPassword(),
                        comptable.getComptableCode(),
                        ROLES.COMPTABLE);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (comptable.getUserEmail(),
                                comptablepasswords )
        );

        // If authentication succeeds → generate JWT
        String token = jwtUtil.generateToken
                (comptable.getUserEmail(),
                        ROLES.COMPTABLE
                );
        return ResponseEntity.ok(new AuthResponse(token,ROLES.COMPTABLE));
    }








}

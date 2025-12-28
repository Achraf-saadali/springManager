package org.example.springmanager2.Service;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Controller.AuthController;
import org.example.springmanager2.CredentialsSchema.*;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.WrongCredentialsException;
import org.example.springmanager2.Repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService implements CommonService  {

   private final BCryptPasswordEncoder encoder ;


    private final ClientRepo clientRepo;



     @Autowired
     public ClientService(ClientRepo clientRepo
             , @Lazy BCryptPasswordEncoder encoder ) {
        this.encoder = encoder ;

        this.clientRepo = clientRepo;





    }



    @Override
    public ROLES supports() {
        return ROLES.CLIENT;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        return clientRepo.findByUserEmail(userEmail);

    }
    @Override
    public void create(Personne client) {
        System.out.println("i am creating a client....");
        Client client2 = (Client) client ;
        client2.setUserPassword(encoder.encode(client2.getUserPassword()));
        clientRepo.save(client2);
    }
    @Override
    public void delete(String code) {
        code = code.replace("\"", "");
        clientRepo.deleteByClientCode(code);
        System.out.println("deletion was succesfull");
    }
    @Override
    public  void modify(Personne person )  {

         Client client = clientRepo.findByClientCode(
                 ((Client) person).getClientCode()
         );

         clientRepo.save(client);


    }

    @Override
    public Authentication authentication(Authentication auth)  throws AuthenticationException {
        String userEmail = auth.getName();
        Client client = (Client) loadUserByUsername(userEmail);
        if (client == null)
            throw new WrongCredentialsException("Client Not found");

        ExtraCredentials extraCredentials = (ExtraCredentials) auth.getCredentials();
        if (
                !(extraCredentials.equals(
                        new ExtraCredentials(
                                client.getUserPassword()
                                , client.getClientCode()
                                ,ROLES.CLIENT
                        )
                        , encoder)
                )
        )
             throw new WrongCredentialsException("Wrong Credentials");

        return new UsernamePasswordAuthenticationToken
                (
                        client,
                        extraCredentials,
                        client.getAuthorities()
                );


    }
    public List<Client> getAll()
    {
        return clientRepo.findAll();
    }

    public Page<? extends Personne> getSome(String name , Pageable page)
    {
        return clientRepo.findByUserNameLike(name,page);
    }

}


package org.example.springmanager2.Service;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Controller.AuthController;
import org.example.springmanager2.CredentialsSchema.*;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.WrongCredentialsException;
import org.example.springmanager2.Repository.ComptableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ComptableService implements CommonService {

    private final ComptableRepo comptableRepo;

    private final BCryptPasswordEncoder encoder ;




    public ComptableService(ComptableRepo comptableRepo
            ,@Lazy BCryptPasswordEncoder encoder

                          ) {
        this.encoder =encoder ;

        this.comptableRepo = comptableRepo;






    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        return comptableRepo.findByUserEmail(userEmail);
    }

    @Override
    public ROLES supports() {
        return ROLES.COMPTABLE;
    }

    public void create(Personne personne) {
        Comptable comptable = (Comptable) personne ;
        comptable.setUserPassword(encoder.
                encode(comptable.getUserPassword()
                      )
                                 );

         comptableRepo.save(comptable);
    }

    @Override
    @Transactional
    public void delete(String code) {

   code = code.replace("\"", "");
        comptableRepo.deleteByComptableCode(code);
        System.out.println("deletion was succesfull");
    }
    @Override
    public  void modify(Personne person )  {

        Comptable comptable = comptableRepo.
                findByComptableCode(
                        ((Comptable) person).getComptableCode()
                                   );

        comptableRepo.save(comptable);


    }

    @Override
    public Authentication authentication(Authentication auth)
            throws AuthenticationException {

        String userEmail = auth.getName();

        Comptable comptable = (Comptable) loadUserByUsername(userEmail);

        if (comptable == null)
            throw new WrongCredentialsException("Comptable Not found");
        ExtraCredentials extraCredentials =
                    (ExtraCredentials) auth.getCredentials();
        if (
                !(extraCredentials.equals(
                        new ExtraCredentials(comptable.getUserPassword()
                                            , comptable.getComptableCode()
                                             ,ROLES.COMPTABLE)
                                         , encoder)
                                          )
        )
            throw new WrongCredentialsException("Wrong Credentials");



        return new UsernamePasswordAuthenticationToken
                (
                        comptable,
                        extraCredentials,
                        comptable.getAuthorities()
                );



    }
    public List<Comptable> getAll()
    {
        return comptableRepo.findAll();
    }

    public Page<? extends Personne> getSome(String name , Pageable page)
    {
        return comptableRepo.findByUserNameLike(name, page);
    }

}

















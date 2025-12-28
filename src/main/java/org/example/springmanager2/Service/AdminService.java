package org.example.springmanager2.Service;

import lombok.Getter;
import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.WrongCredentialsException;
import org.example.springmanager2.Repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Getter
@Service
public class AdminService implements CommonService
{

    private final BCryptPasswordEncoder encoder;
    private final AdminRepo adminRepo;




    @Autowired
    public AdminService(AdminRepo adminRepo
            , @Lazy BCryptPasswordEncoder encoder

                        ) {
        this.encoder = encoder ;


        this.adminRepo = adminRepo;


    }
    public Page<? extends Personne> getSome(String name , Pageable page)
    {
        return null ;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        return adminRepo.findByUserEmail(userEmail);
    }

    @Override
    public  ROLES supports()
    {
        return ROLES.ADMIN ;
    }

    @Override
    public void create(Personne admin){
        System.out.println("Can't create this role");
        return ;
    }
    @Override
    public void delete(String code){


        System.out.println("Can't delete this role");
        return ;
    }
    @Override
    public  void modify(Personne person)
    {
        System.out.println("Can't modify this role !! ");
        return ;

    }
    @Override
    public Authentication authentication(Authentication auth) throws AuthenticationException
    {
         String userEmail = auth.getName() ;

        System.out.println("just before loading here ...");

         Admin admin = (Admin) loadUserByUsername(userEmail) ;

        System.out.println("admin is == "+ admin);

         if(admin == null)
             throw new WrongCredentialsException("Admin Not Found");
        System.out.println("Admin was loaded here he is =="+admin);

        System.out.println("credentails from  authentication "+auth.getCredentials());

         String password = (String) auth.getCredentials() ;

        System.out.println("Password is encoded myabe"+ admin.getPassword());
         if(!(encoder.matches(password , admin.getPassword())))
             throw new WrongCredentialsException("Wrong Password");

         return new UsernamePasswordAuthenticationToken
                 (
                         admin ,
                         admin.getUserPassword()  ,
                         admin.getAuthorities()
                 );






    }

    public List<Admin> getAll()
    {
        return adminRepo.findAll();
    }






}

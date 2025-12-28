package org.example.springmanager2.Controller;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.InvalidTokenException;
import org.example.springmanager2.Exception.RolesException;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import org.example.springmanager2.Entity.Client;

import java.util.List;


@RestController
@RequestMapping("/admin")

public class AdminController {

    private RolesRouter rolesRouter;
    private JwtUtil  jwtUtil ;

    private Admin currentAdmin ;
    public AdminController(RolesRouter rolesRouter , JwtUtil jwtUtil)
    {
        this.rolesRouter=rolesRouter;
        this.jwtUtil  = jwtUtil ;
    }


    @ModelAttribute
    public void verifyHeader(@RequestHeader("Authorization") String bearerKey) throws Exception {
        String token;
        if
        (bearerKey == null ||
                ! bearerKey.startsWith("Bearer ") ||
                (token = bearerKey.substring(7)).isBlank()
        )
            throw new InvalidTokenException("Authentication required");

         this.currentAdmin
                = (Admin) rolesRouter
                .load(
                        ROLES.ADMIN,jwtUtil.extractUsername(token)
                      );
        System.out.println("VALID TOKEN and admin "+currentAdmin);


    }

    @GetMapping("/profile")
    public Admin getProfile()
    {
        System.out.println("this admin "+currentAdmin);
        return currentAdmin ;
    }


    @PostMapping("/create-role")
    public Status create(@RequestBody Personne P) throws Exception {
        System.out.println(P.toString());
        System.out.println("I am in create");

        switch (P) {
            case Client C ->rolesRouter.create(C.getUserRole(), C);
            case Comptable C ->rolesRouter.create(C.getUserRole(), C);
            default -> throw
                    new RolesException("Action Impossible " +
                            ":Can't create  this role");
        };

        return new Status(200, "Successful creation");
    }

    @PostMapping("/delete-role")
    public Status delete(@RequestBody String code ,ROLES userRole ) throws Exception {

        System.out.println("code "+code+" role "+userRole);
        switch (userRole) {
            case ROLES.CLIENT ->rolesRouter.delete(ROLES.CLIENT, code);
            case ROLES.COMPTABLE ->rolesRouter.delete(ROLES.COMPTABLE, code);
            default -> throw
                    new RolesException("Action Impossible " +
                            ":Can't delete  this role");
        };

        return new Status(200, "Successful deletion");


    }
    @PostMapping("/modify-role")
    public Status modify(@RequestBody Personne P) throws Exception
    {
        switch (P) {
            case Client C ->rolesRouter.modify(C.getUserRole(), C);
            case Comptable C ->rolesRouter.modify(C.getUserRole(), C);
            case Admin A ->rolesRouter.modify(A.getUserRole() , A);
            default -> throw
                    new RolesException("Action Impossible " +
                    ":Can't modify  this role");
        };
        return new Status(200 , "Successful modification");
    }

    @GetMapping("/comptables")
    public List<? extends Personne> getComptable(){

       return  rolesRouter.getAll(ROLES.COMPTABLE);

    };
    @GetMapping("/clients")
    public List<? extends Personne> getClients(){

        return  rolesRouter.getAll(ROLES.CLIENT);

    };



}

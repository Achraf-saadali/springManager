package org.example.springmanager2.Controller;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.InvalidTokenException;
import org.example.springmanager2.Exception.RolesException;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comptable")
public class ComptableController {

    private RolesRouter rolesRouter;
    private JwtUtil jwtUtil;
    private Comptable currentComptable;

    public ComptableController(RolesRouter rolesRouter , JwtUtil jwtUtil)
    {
        this.rolesRouter=rolesRouter;
        this.jwtUtil  = jwtUtil ;
    }
    @ModelAttribute
    public void verifyHeader(@RequestHeader("Authorization") String bearerKey) throws Exception
    {
        String token ;
        if (bearerKey == null || ! bearerKey.startsWith("Bearer ") || (token = bearerKey.substring(7)).isBlank() || ! jwtUtil.isTokenValid(token))
            throw new InvalidTokenException("Authentication required");

        currentComptable = (Comptable) rolesRouter.load(ROLES.COMPTABLE ,jwtUtil.extractUsername(token)) ;
        System.out.println("VALID TOKEN ");


    }




    @PostMapping("/create-role")
    public Status create(@RequestBody Personne P) throws Exception {



        switch (P) {
            case Client C -> rolesRouter.create(C.getUserRole(), C);

            default -> throw new RolesException("Action Impossible :Can't create  this role");
        }
        ;
        return new Status(200, "Successful creation");


    }

    @PostMapping("/delete-role")
    public Status delete(@RequestParam String code, String role) throws Exception {
        ROLES userRole = ROLES.valueOf(role);
        System.out.println("code "+code+" role "+userRole);

        switch (userRole) {
            case ROLES.CLIENT -> rolesRouter.delete(ROLES.CLIENT, code);



            default -> throw new RolesException("Action Impossible :Can't delete  this role");
        }
        ;

        return new Status(200, "Successful deletion");


    }

    @PostMapping("/modify-role")
    public Status modify(@RequestBody Personne P) throws Exception {
        switch (P) {
            case Client C -> rolesRouter.modify(C.getUserRole(), C);
            case Comptable C -> {
                if(!currentComptable.getComptableCode().equals( C.getComptableCode()))
                {
                    return new Status(403,"Can 't change Antoher Comptable information ");
                }
                rolesRouter.modify(ROLES.CLIENT , C);
                return new Status(200, "Successful modification");


            }

            default -> throw new RolesException("Action Impossible :Can't modify  this role");
        }
        ;
        return new Status(200, "Successful modification");
    }

    @GetMapping("/all")
    public List<? extends Personne> getAll() {
        System.out.println("passed by getAll");
        return rolesRouter.getAll(ROLES.COMPTABLE);

    }

    @GetMapping("/some")
    public Page<? extends Personne> getSome(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "userName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction

    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        direction.equalsIgnoreCase("desc")
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC,
                        sortBy
                )
        );
       return rolesRouter.getSome(ROLES.COMPTABLE, query, pageable);


    }



}
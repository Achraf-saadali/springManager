package org.example.springmanager2.Controller;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Exception.InvalidTokenException;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@SessionAttributes("currentUser")
public class ClientController {
    private RolesRouter rolesRouter;
    private JwtUtil jwtUtil ;
    private Client currentClient;
    public ClientController(RolesRouter rolesRouter , JwtUtil jwtUtil)
    {
        this.rolesRouter=rolesRouter;
        this.jwtUtil  = jwtUtil ;
    }


    @ModelAttribute("currentUser")
    public Object verifyHeader(@RequestHeader("Authorization") String bearerKey) throws Exception {
        if (bearerKey == null || !bearerKey.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authentication required");
        }
        //{status : 401 , 200 ,345   message : ex.toString}

        String token = bearerKey.substring(7).trim();
        if (token.isBlank() || !jwtUtil.isTokenValid(token)) {
            throw new InvalidTokenException("Authentication required");
        }

        // Vérifier le rôle et charger l’utilisateur correspondant
        Object currentUser = null;
        ROLES role = jwtUtil.extractRole(token);
        if (role == ROLES.ADMIN) {
            currentUser = rolesRouter.load(ROLES.ADMIN, jwtUtil.extractUsername(token));
        } else if (role == ROLES.ADMIN) {
            currentUser = rolesRouter.load(ROLES.COMPTABLE, jwtUtil.extractUsername(token));
        } else {
            throw new InvalidTokenException("User does not have required role");
        }

        System.out.println("VALID TOKEN for user: " + currentUser);
        return currentUser; // Spring stockera automatiquement dans la session sous "currentUser"
    }

    @GetMapping("/profile")
    public Client getProfile()
    {
        System.out.println("this client "+currentClient);
        return currentClient ;
    }

    @GetMapping("/all")
    public List<? extends Personne> getAll()
    {
       return  rolesRouter.getAll(ROLES.CLIENT);

    }
    @GetMapping("/some")
    public Page<? extends Personne> getSome(
            @RequestParam(defaultValue = "") String query ,
            @RequestParam(defaultValue = "0") int page ,
            @RequestParam(defaultValue = "50") int size ,
            @RequestParam(defaultValue = "userName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction

    )
    {
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
       return rolesRouter.getSome(ROLES.CLIENT,query,pageable);
    }
    @PostMapping("/modify-me")
    public Status modify(@RequestBody Personne P)
    {
        switch(P)
        {
            case Client C -> {
                if(!currentClient.getClientCode().equals( C.getClientCode()))
                {
                    return new Status(403,"Can 't change Antoher Client information ");
                }
                rolesRouter.modify(ROLES.CLIENT , C);
                return new Status(200, "Successful modification");


            }
            default -> {
                return new Status(403,"Can't procced !!!");
            }
        }
    }
}

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
public class ClientController {
    private RolesRouter rolesRouter;
    private JwtUtil jwtUtil ;
    private Client currentClient;
    public ClientController(RolesRouter rolesRouter , JwtUtil jwtUtil)
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

        currentClient = (Client) rolesRouter.load(ROLES.CLIENT ,jwtUtil.extractUsername(token)) ;
        System.out.println("VALID TOKEN ");


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
}

package org.example.springmanager2.Controller;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Entity.Produit;
import org.example.springmanager2.Exception.InvalidTokenException;
import org.example.springmanager2.Service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/produits")
public class ProduitController {

    private ProduitService produitServive ;
    private JwtUtil jwtUtil;


    public ProduitController(ProduitService produitService,
                             JwtUtil jwtUtil)
    {
        this.produitServive=produitService ;
        this.jwtUtil=jwtUtil;
    }

    @ModelAttribute
    public void verifyHeader(@RequestHeader("Authorization") String bearerKey) throws Exception
    {
        String token ;
        if (bearerKey == null || ! bearerKey.startsWith("Bearer ") || (token = bearerKey.substring(7)).isBlank() || ! jwtUtil.isTokenValid(token))
            throw new InvalidTokenException("Authentication required");


           System.out.println("VALID TOKEN  from product");


    }

    @GetMapping("/all")
    public List<Produit> getProduits()
    {
        return produitServive.findAll();
    }

    @PostMapping("/some")
    public Page<Produit> searchProduits(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
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

        Page<Produit> produit = produitServive.search(query, pageable);
        System.out.println("produit sont...."+produit.stream().toList());
        return produit;
    }
    @PostMapping("/delete-product")
    public Status delete(@RequestParam("idProduit") String id)
    {     int idProduit = Integer.valueOf(id);
        produitServive.deleteProduct(idProduit);

        return new Status(200 , "Product deletion was successfull");
    }

    @PostMapping("/create-product")
    public Status create(
            @ModelAttribute Produit P
    ) {
        System.out.println("Produit reçu === " + P);
        produitServive.saveProduct(P);
        return new Status(200, "Product created successfully");
    }
    @PostMapping("/modify-product")
    public Status modify(@ModelAttribute Produit P)
    {
        produitServive.saveProduct(P);

        return new Status(200 , "Product modified Successufully");

    }



}

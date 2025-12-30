package org.example.springmanager2.Controller;


import lombok.Data;
import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Facture;
import org.example.springmanager2.Entity.LigneFacture;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Entity.Produit;
import org.example.springmanager2.Exception.InvalidTokenException;
import org.example.springmanager2.Service.FactureService;
import org.example.springmanager2.Service.ProduitService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController

@RequestMapping("/facture")
public class FactureController {

    private JwtUtil jwtUtil ;
    private FactureService factureService;
    private ProduitService produitService ;
    private Personne P;



    public FactureController(JwtUtil jwtUtil ,FactureService factureService,ProduitService produitService)
    {
        this.factureService=factureService ;
        this.jwtUtil=jwtUtil ;
        this.produitService=produitService ;

    }


    private  Produit createProduct(ProduitResponse P)
    {

      Produit produit= (Produit) produitService.findById(P.getIdProduit());
        System.out.println("creating  product "+produit);
        return produit ;
    }

    @PostMapping("/create")
    public void createFacture(@RequestBody FactureReponse factureReponse) {

        List<LigneFacture> lignes = factureReponse.getProduitCommandes()
                .stream()
                .map(p -> new LigneFacture(
                        null,
                        produitService.findById(p.getProduit().getIdProduit()),
                        p.getQuantiteCommandes()
                ))
                .toList();
        System.out.println("prduitsssssssssss"+lignes);
        System.out.println("creating.......");
        switch (factureReponse.getTypeFacture()) {
            case CLIENT ->
                    factureService.creerFactureClient(factureReponse.getCode(), lignes);
            case ADMIN ->
                    factureService.creerFactureComptable(factureReponse.getCode(), lignes);
            default ->
                    throw new RuntimeException("Invalid facture type");
        }
        System.out.println("created........");
    }
    @GetMapping("/all")
    public List<Facture> getAll(@RequestParam ROLES role) {
        return factureService.findByTypeFacture(role);
    }
    @GetMapping("/my-invoice")
    public List<Facture> getInvoices(@RequestParam ROLES role,@RequestParam String code) {
        return factureService.findByCodeAndTypeFacture(code,role);
    }
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadFacturePdf(
            @PathVariable("id") Integer idFacture

    ) throws Exception {
        Facture facture = factureService.findById(idFacture);

        byte[] pdfBytes = factureService.genererPDF(facture);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=facture_" + idFacture + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }




}




@Data
 class FactureReponse {
    private String code;
    private ROLES typeFacture;
    private double prixFacture;
    private List<ProduitCommandeResponse> produitCommandes;
}

@Data
 class ProduitCommandeResponse {
    private ProduitResponse produit;
    private int quantiteCommandes;
}

@Data
class ProduitResponse {
    private int idProduit;
}
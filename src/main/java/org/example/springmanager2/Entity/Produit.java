package org.example.springmanager2.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_produit")
    private Integer idProduit ;

    private String description ;
    @Column(name="image_url")
    private String imageUrl ;

    private String name ;
     @Column(name="quantite_stock")
    private int quantiteStock ;

    @Column(name="prix_unitaire")
    private double prixUnitaire ;




}

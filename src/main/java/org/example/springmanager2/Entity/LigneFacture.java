package org.example.springmanager2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data

public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_facture")
    @JsonIgnore
    private Facture facture;

    @ManyToOne
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;

    @Column(name="quantite_commandes")
    private Integer quantiteCommandes;

    public LigneFacture(Facture facture , Produit produit , Integer quantiteCommandes)
    {
        this.facture = facture ;
        this.produit = produit ;
        this.quantiteCommandes= quantiteCommandes ;
    }
    public LigneFacture(){}
}


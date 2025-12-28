package org.example.springmanager2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.springmanager2.Entity.Annotations.ComplexIntId;
import org.example.springmanager2.Entity.Enums.Etat;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data

@Entity
public class Facture {

    @Id
    @GeneratedValue(generator = "complex-int-id")
    @ComplexIntId(start = 3000)
    @GenericGenerator(
            name = "complex-int-id",
            strategy = "org.example.springmanager2.Entity.Annotations.ComplexIntIdGenerator"
    )
    @Column(name = "id_facture")
    private Integer idFacture;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LigneFacture> produitCommandes;

    private double prixFacture;

    @Column(name = "code_facturation", nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    private ROLES typeFacture;

    private Date createdAt = new Date(System.currentTimeMillis());

    @Enumerated(EnumType.STRING)
    private Etat etat = Etat.ATTENTE;

    public Facture(String code,ROLES typeFacture ,double prixFacture )
    {
        this.code=code ;
        this.typeFacture=typeFacture ;
        this.prixFacture=prixFacture ;
    }
    public Facture(){}
}





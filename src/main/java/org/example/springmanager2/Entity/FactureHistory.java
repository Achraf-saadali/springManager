package org.example.springmanager2.Entity;



import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
public class FactureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer factureId; // référence à la facture modifiée

    private String action; // "CREATED", "UPDATED", "STATE_CHANGED", etc.

    private String updatedBy; // user ou code qui a fait la modification

    @Column(columnDefinition = "TEXT")
    private String details; // JSON ou texte décrivant les changements

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
}


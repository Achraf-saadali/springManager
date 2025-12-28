package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FactureRepo extends JpaRepository<Facture, Integer> {

    List<Facture> findByTypeFacture(ROLES typeFacture);

    List<Facture> findByCodeAndTypeFacture(String code, ROLES typeFacture);
}


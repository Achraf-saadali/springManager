package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Facture;
import org.example.springmanager2.Entity.LigneFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneFactureRepo extends JpaRepository<LigneFacture ,Long> {

}

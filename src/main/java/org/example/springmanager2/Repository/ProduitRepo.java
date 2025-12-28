package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepo  extends JpaRepository<Produit,Integer> {
    Page<Produit> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

}

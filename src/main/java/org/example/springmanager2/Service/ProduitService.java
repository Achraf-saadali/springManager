package org.example.springmanager2.Service;

import org.example.springmanager2.Entity.Produit;
import org.example.springmanager2.Repository.ProduitRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {
    private ProduitRepo produitRepo ;
    public ProduitService(ProduitRepo produitRepo )
    {
        this.produitRepo=produitRepo;
    }

    public Produit create(Produit P)
    {
        return produitRepo.save(P);
    }

    public Produit findById(int id)
    {
        return produitRepo.findById(id).orElse(null);
    }

    public void delete(Integer idProduit)
    {
        produitRepo.deleteById(idProduit);
    }
    public void modify(Produit P)
    {
        produitRepo.save(P);

    }

    public List<Produit> findAll()
    {
        return produitRepo.findAll();
    }

    public Page<Produit> findAll(Pageable pageable)
    {
        return produitRepo.findAll(pageable);
    }
    public Page<Produit> search(String name, Pageable pageable) {
        return produitRepo.findByNameContainingIgnoreCase(name, pageable);
    }


    public void deleteProduct(int idProduct)
    {
        System.out.println("deleting product by id "+idProduct);
        produitRepo.deleteById(idProduct);
    }

    public void saveProduct(Produit p)
    {
        produitRepo.save(p);
    }





}

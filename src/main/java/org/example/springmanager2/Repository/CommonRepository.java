package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface CommonRepository<ENTITY,ID> extends JpaRepository<ENTITY,ID> {

    public ENTITY findByUserEmailAndUserPassword
            ( String userEmail,String userPassword);

    public ENTITY findByUserEmail(String userEmail);
    public ENTITY deleteByUserEmail(String userEmail);
     public Page<ENTITY> findByUserNameLike(String name , Pageable page);

    default ROLES supports()
    {
        return null;
    }

}

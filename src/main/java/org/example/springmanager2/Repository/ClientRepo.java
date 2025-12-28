package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClientRepo  extends CommonRepository<Client,Integer>
{
    public Client findByUserEmailAndUserPasswordAndClientCode
            ( String userEmail,String userPassword,String clientCode);

    public Client findByClientCode(String clientCode);


    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM client WHERE client_code = :clientCode",
            nativeQuery = true
    )
    int deleteByClientCode(@Param("clientCode") String clientCode);

    @Override
    default ROLES supports()
    {
        return ROLES.CLIENT ;
    }
}

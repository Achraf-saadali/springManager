package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComptableRepo extends CommonRepository<Comptable,Integer> {

    public  Comptable findByUserEmailAndUserPasswordAndComptableCode
            ( String userEmail,String userPassword,String ComptableCode);

    public Comptable findByComptableCode(String comptableCode);

    public long  deleteByComptableCode( String comptableCode);
    @Override
    default ROLES supports()
    {
        return ROLES.COMPTABLE ;
    }
}

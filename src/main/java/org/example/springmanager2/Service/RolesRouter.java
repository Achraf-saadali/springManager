package org.example.springmanager2.Service;

import lombok.Getter;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.example.springmanager2.Entity.Personne;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter

public class RolesRouter
{

   private Map<ROLES,CommonService> mappedServices  ;



   public RolesRouter(List<CommonService> commonServices){
       System.out.println("RolesRouter constructor," +
               " commonServices=" + commonServices);

       this.mappedServices = new HashMap<>();

       for(CommonService service  : commonServices )
           mappedServices.put(service.supports() , service) ;

   }




   public Authentication authenticate(ROLES role , Authentication auth)
           throws AuthenticationException
   {
       return mappedServices.get(role).authentication(auth);
   }



   public void create(ROLES role , Personne P)     {
       System.out.println("in create roles router");
       mappedServices.get(role).create( P);
   }



    public void delete(ROLES role , String code)     {
        mappedServices.get(role).delete( code);
    }



    public void modify(ROLES role , Personne P1 )
    {
        mappedServices.get(role).modify( P1);
    }



    public   UserDetails load(ROLES role , String userEmail)
    {
        return  mappedServices.get(role).loadUserByUsername(userEmail);
    }

    public  List<? extends Personne> loadAll(ROLES role )
    {
        return  mappedServices.get(role).getAll();
    }





    public List<? extends Personne> getAll(ROLES role)
    {
        return mappedServices.get(role).getAll();

    }

    public Page<? extends Personne> getSome(ROLES role , String name , Pageable page)
    {
        return mappedServices.get(role).getSome(name ,page);
    }








}

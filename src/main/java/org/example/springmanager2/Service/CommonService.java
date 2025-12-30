package org.example.springmanager2.Service;

import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CommonService  extends UserDetailsService
{






        public Authentication authentication(Authentication auth)
                throws AuthenticationException;

//        default  List<String> checkCredentialsExchange(Personne person1 , Personne person2)
//        {      if (person1 == null) throw new IllegalArgumentException("person1 cannot be null");
//
//            if (person2 == null) {
//                return List.of(person1.getUsername(), person1.getUserName(), person1.getUserPassword());
//            }
//
//            String userEmail = (person2.getUsername() == null || person2.getUsername().isBlank())
//                    ? person1.getUsername() : person2.getUsername();
//            String userName = (person2.getUserName() == null || person2.getUserName().isBlank())
//                    ? person1.getUserName() : person2.getUserName();
//            String userPassword = (person2.getUserPassword() == null || person2.getUserPassword().isBlank())
//                    ? person1.getUserPassword() : person2.getUserPassword();
//
//            return List.of(userEmail, userName, userPassword);
//        }




        public   ROLES supports();

        public  void create(Personne person) ;

        public  void delete(String code) ;

        public  void modify(Personne person) ;


        public  List<?extends Personne> getAll();

        public Page<? extends Personne> getSome(String name , Pageable page);















        }

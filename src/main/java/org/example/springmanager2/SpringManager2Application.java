package org.example.springmanager2;

import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Repository.AdminRepo;
import org.example.springmanager2.Repository.ClientRepo;
import org.example.springmanager2.Repository.ComptableRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringManager2Application {

    public static void main(String[] args) {



        ApplicationContext container  = SpringApplication.run(SpringManager2Application.class, args);

        String[] beans = container.getBeanDefinitionNames();

        int howManyBeans = beans.length ;

        System.out.println("***********************************************************************************************************************");
        for (int i = 0; i < howManyBeans; i++) {


            System.out.println("bean number  = "+i+" is : "+beans[i]);

        }
            System.out.println("***********************************************************************************************************************");
        //
//        AdminRepo adminRepo = container.getBean(AdminRepo.class);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        Admin admin  = new Admin();
//        admin.setUserName("Achraf_23") ;
//        admin.setUserEmail("achrafsaadalii@gmail.com");
//        admin.setUserPassword(encoder.encode("Achraf.2003"));
//        ClientRepo clientRepo = container.getBean(ClientRepo.class);
//
//        Client client  = new Client();
//        client.setUserName("Amrou_23") ;
//        client.setUserEmail("amrousaadalii@gmail.com");
//        client.setUserPassword(encoder.encode("Amrou.2006"));
//        client.setClientCode("Cl-2006");
//
//        ComptableRepo comptableRepo = container.getBean(ComptableRepo.class);
//
//        Comptable comptable  = new Comptable();
//        comptable.setUserName("ZineEddine_89") ;
//        comptable.setUserEmail("zinesaadalii@gmail.com");
//        comptable.setUserPassword(encoder.encode("Zine.1999"));
//        comptable.setComptableCode("Co-1999");
//        comptableRepo.save(comptable);
//        adminRepo.save(admin);
//        clientRepo.save(client);

    }

}

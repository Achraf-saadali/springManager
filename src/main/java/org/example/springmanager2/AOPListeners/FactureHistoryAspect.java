package org.example.springmanager2.AOPListeners;



import jakarta.transaction.Transactional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Facture;
import org.example.springmanager2.Entity.FactureHistory;
import org.example.springmanager2.Repository.FactureHistoryRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class FactureHistoryAspect {

    private final FactureHistoryRepo historyRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FactureHistoryAspect(FactureHistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    @AfterReturning(pointcut = "execution(* org.example.springmanager2.Service.FactureService.creerFacture*(..))",
            returning = "facture")
    @Transactional
    public void logFactureCreation(JoinPoint joinPoint, Facture facture) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             Object currentUser = auth.getPrincipal();
             String username = switch(currentUser)
             {case Admin A -> A.getUserName()+"/ADMIN";
                 case Comptable C -> C.getUserName()+"/COMPTABLE";
                 default-> throw new IllegalArgumentException("Can't do Invoice Updates !!");
             };
            FactureHistory history = new FactureHistory();
            history.setFactureId(facture.getIdFacture());
            history.setAction("CREATED");
            history.setUpdatedBy(username);
            history.setDetails(objectMapper.writeValueAsString(facture));
            historyRepo.save(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

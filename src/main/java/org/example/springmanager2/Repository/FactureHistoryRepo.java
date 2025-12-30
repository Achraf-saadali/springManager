package org.example.springmanager2.Repository;



import org.example.springmanager2.Entity.FactureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureHistoryRepo extends JpaRepository<FactureHistory, Long> { }

package org.example.springmanager2.Repository;

import org.example.springmanager2.Entity.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepository
        extends JpaRepository<ForgotPasswordToken, Integer> {

    Optional<ForgotPasswordToken> findByToken(String token);
    public void deleteByToken(String token);
}
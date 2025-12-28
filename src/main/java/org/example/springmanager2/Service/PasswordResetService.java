package org.example.springmanager2.Service;

import org.example.springmanager2.Entity.ForgotPasswordToken;
import org.example.springmanager2.Repository.ForgotPasswordTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordResetService {

    private final ForgotPasswordTokenRepository tokenRepository;
    private final GmailEmailService email;

    public PasswordResetService(ForgotPasswordTokenRepository tokenRepository,GmailEmailService email) {
        this.tokenRepository = tokenRepository;
        this.email = email ;
    }


    @Transactional
    public  String createToken(String email) {
        // Generate a random token
        String token = UUID.randomUUID().toString();

        // Create entity
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setEmail(email);
        forgotPasswordToken.setToken(token);

        // Save to DB
        tokenRepository.save(forgotPasswordToken);

        return token;
    }
    public boolean checkToken(String token)
    {
         return tokenRepository.findByToken(token).isPresent();
    }

    public void sendResetEmail(String email,String token) throws Exception{
        this.email.sendPasswordResetEmail(email ,token);
    }
}


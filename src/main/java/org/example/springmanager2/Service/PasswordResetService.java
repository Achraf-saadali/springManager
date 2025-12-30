package org.example.springmanager2.Service;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.ForgotPasswordToken;
import org.example.springmanager2.Repository.ForgotPasswordTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordResetService {

    private final ForgotPasswordTokenRepository tokenRepository;
    private final GmailEmailService email;
    private final JwtUtil jwtUtil ;

    public PasswordResetService(ForgotPasswordTokenRepository tokenRepository,GmailEmailService email ,JwtUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.email = email ;
        this.jwtUtil = jwtUtil ;
    }


    @Transactional
    public String createToken(String email) {

        // 1. Generate UUID token
        String token = UUID.randomUUID().toString();

        // 2. Create entity
        ForgotPasswordToken forgotpass = new ForgotPasswordToken();
        forgotpass.setEmail(email);
        forgotpass.setToken(token); // store UUID directly



        // 3. Save to DB
        tokenRepository.save(forgotpass);

        // 4. Return token (send via email)
        return token;
    }

    public boolean checkToken(String token)
    {
         return tokenRepository.findByToken(token).isPresent();
    }

    public void sendResetEmail(String email, String token , ROLES role) throws Exception{
        this.email.sendPasswordResetEmail(email ,token,role);
    }
}


package org.example.springmanager2.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

@Entity
@Table(
        name = "forgotpasswordtoken",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "token")
        }
)
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Integer idToken;

    @Column(name = "token", length = 100, nullable = false, unique = true)
    private String token;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt ;

    // Constructors
    public ForgotPasswordToken() {
        createdAt = LocalDateTime.now();
    }

    public ForgotPasswordToken(String token, String email) {
        this.token = token;
        this.email = email;
    }

    // Getters and Setters
    public Integer getIdToken() {
        return idToken;
    }

    public void setIdToken(Integer idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

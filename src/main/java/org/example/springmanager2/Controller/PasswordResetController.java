package org.example.springmanager2.Controller;

import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Service.PasswordResetService;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final BCryptPasswordEncoder encoder;
    private final RolesRouter rolesRouter;

    public PasswordResetController(
            PasswordResetService passwordResetService,
            BCryptPasswordEncoder encoder,
            RolesRouter rolesRouter
    ) {
        this.passwordResetService = passwordResetService;
        this.encoder = encoder;
        this.rolesRouter = rolesRouter;
    }

    // 1️⃣ SEND RESET LINK
    @PostMapping("/send-link")
    public Status sendPasswordLink(@RequestParam String email) throws Exception {

        Personne personne = rolesRouter.loadByEmail(email);
        if (personne == null) {
            throw new IllegalArgumentException("User not found");
        }

        String token = passwordResetService.createToken(email);
        passwordResetService.sendResetEmail(email, token);

        return new Status(200, "Reset email sent");
    }

    // 2️⃣ RESET PASSWORD
    @PostMapping("/reset-password")
    public Status resetPassword(
            @RequestParam String token,
            @RequestParam String password
    ) {
        // 🔐 Token identifies the user
        Personne personne = passwordResetService.getUserFromToken(token);

        if (personne == null || passwordResetService.isTokenExpired(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        personne.setUserPassword(encoder.encode(password));

        if (personne instanceof Client client) {
            rolesRouter.modify(ROLES.CLIENT, client);
        } else if (personne instanceof Admin admin) {
            rolesRouter.modify(ROLES.ADMIN, admin);
        } else if (personne instanceof Comptable comptable) {
            rolesRouter.modify(ROLES.COMPTABLE, comptable);
        }

        passwordResetService.invalidateToken(token);

        return new Status(200, "Password reset successfully");
    }
}

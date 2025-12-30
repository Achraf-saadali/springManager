package org.example.springmanager2.Controller;

import org.example.springmanager2.Config.JwtUtil;
import org.example.springmanager2.Entity.Admin;
import org.example.springmanager2.Entity.Client;
import org.example.springmanager2.Entity.Comptable;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Personne;
import org.example.springmanager2.Service.PasswordResetService;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/password")
public class PasswordResetController {
//
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
    public Status sendPasswordLink(@RequestParam("email") String email ,@RequestParam("pwdRole") String roleString) throws Exception {
        ROLES role = ROLES.valueOf(roleString);
            UserDetails personne = rolesRouter.load(role,email);
        if (personne == null) {
            throw new IllegalArgumentException("User not found");
        }
        System.out.println("personne est "+personne);
        String token = passwordResetService.createToken(email);
        System.out.println("token is =="+token);
        passwordResetService.sendResetEmail(email, token,role);


        // Forward vers le endpoint /reset-password

        return new Status(200, "Reset email sent");
    }
//
//    // 2️⃣ RESET PASSWORD
    @PostMapping("/reset-password")
    public Status resetPassword(
            @RequestParam String pwdToken,
            @RequestParam String password ,
            @RequestParam String pwdRole,
            @RequestParam String email
    ) {
        System.out.println("here "+pwdToken +" "+password+" "+" "+pwdRole+" "+email);

        UserDetails personne = rolesRouter.load(ROLES.valueOf(pwdRole),email);


        if (personne== null || ! passwordResetService.checkToken(pwdToken)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }



        if (personne instanceof Client client) {
            client.setUserPassword(encoder.encode(password));
            rolesRouter.modify(ROLES.CLIENT, client);
        } else if (personne instanceof Admin admin) {
            admin.setUserPassword(encoder.encode(password));
            rolesRouter.modify(ROLES.ADMIN, admin);
        } else if (personne instanceof Comptable comptable) {
            comptable.setUserPassword(encoder.encode(password));
            rolesRouter.modify(ROLES.COMPTABLE, comptable);
        }



        return new Status(200, "Password reset successfully");
    }
}

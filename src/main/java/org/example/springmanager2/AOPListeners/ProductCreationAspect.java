package org.example.springmanager2.AOPListeners;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.example.springmanager2.Entity.Produit;
import org.example.springmanager2.Service.GmailEmailService;
import org.example.springmanager2.Service.RolesRouter;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class ProductCreationAspect {

    private final GmailEmailService emailService;
    private final RolesRouter rolesRouter;

    public ProductCreationAspect(GmailEmailService emailService, RolesRouter rolesRouter) {
        this.emailService = emailService;
        this.rolesRouter = rolesRouter;
    }

    private List<String> returnAllClientEmails() {
        return rolesRouter.loadAll(ROLES.CLIENT).stream()
                .map(user -> user.getUsername())
                .toList();
    }

    // Trigger after the saveProduct method executes
    @AfterReturning("execution(* org.example.springmanager2.Service.ProduitService.saveProduct(..))")
    public void afterProductCreated(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof Produit p) {

            String htmlBody = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>New Product Available</title>
        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f4f4f9;
                color: #333;
                margin: 0;
                padding: 0;
            }
            .container {
                max-width: 600px;
                margin: 50px auto;
                background-color: #ffffff;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                overflow: hidden;
            }
            .header {
                background-color: #4CAF50;
                color: white;
                text-align: center;
                padding: 20px;
                font-size: 24px;
                font-weight: bold;
            }
            .content {
                padding: 30px 20px;
            }
            .content p {
                font-size: 16px;
                line-height: 1.6;
                margin: 10px 0;
            }
            .price {
                font-size: 20px;
                font-weight: bold;
                color: #e91e63;
            }
            .footer {
                background-color: #f1f1f1;
                text-align: center;
                padding: 15px;
                font-size: 14px;
                color: #777;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">New Product Added: %s</div>
            <div class="content">
                <p><strong>Description:</strong> %s</p>
                <p class="price">Price: %.2f MAD</p>
            </div>
            <div class="footer">Thank you for staying with us! Visit our store for more products.</div>
        </div>
    </body>
    </html>
""".formatted(p.getName(), p.getDescription(), p.getPrixUnitaire());


            List<String> emails = returnAllClientEmails();
            for (String email : emails) {
                try {
                    System.out.println("Sending email to " + email);
                    emailService.sendEmail(email, "New Product Available", htmlBody);
                } catch (Exception ex) {
                    System.out.println("Failed to send email to " + email);
                }
            }
        }
    }
}

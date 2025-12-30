package org.example.springmanager2.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.*;

import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.example.springmanager2.Entity.Enums.ROLES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GmailEmailService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    /**
     * Envoyer un email de vérification
     */
    public void sendVerificationEmail(String recipientEmail, String verificationCode) throws Exception {
        String subject = "Vérification de votre email";
        String htmlBody = String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                        <h2 style="color: #333; margin-bottom: 20px;">Vérification de votre email</h2>
                        <p style="color: #666; font-size: 16px;">Bonjour,</p>
                        <p style="color: #666; font-size: 16px;">Utilisez le code suivant pour vérifier votre email:</p>
                        <div style="background-color: #f0f0f0; padding: 20px; border-radius: 5px; text-align: center; margin: 30px 0;">
                            <h1 style="color: #007bff; letter-spacing: 5px; margin: 0; font-family: monospace;">%s</h1>
                        </div>
                        <p style="color: #999; font-size: 14px;">Ce code expire dans 15 minutes.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                        <p style="color: #999; font-size: 14px;">Cordialement,<br/><strong>L'équipe Facturation</strong></p>
                    </div>
                </body>
            </html>
            """, verificationCode);

        sendEmail(recipientEmail, subject, htmlBody);
    }

    /**
     * Envoyer un email de confirmation de facture
     */
    public void sendInvoiceEmail(String recipientEmail, String invoiceId, String amount) throws Exception {
        String subject = "Votre facture #" + invoiceId;
        String htmlBody = String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                        <h2 style="color: #333; margin-bottom: 20px;">Facture #%s</h2>
                        <p style="color: #666; font-size: 16px;">Bonjour,</p>
                        <p style="color: #666; font-size: 16px;">Veuillez trouver ci-joint votre facture.</p>
                        <div style="background-color: #f9f9f9; padding: 20px; border-left: 4px solid #007bff; margin: 30px 0;">
                            <p style="margin: 0; color: #666;"><strong>Montant:</strong> %s €</p>
                            <p style="margin: 10px 0 0 0; color: #666;"><strong>Date:</strong> %s</p>
                        </div>
                        <p style="color: #666; font-size: 16px;">Merci pour votre confiance.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                        <p style="color: #999; font-size: 14px;">Cordialement,<br/><strong>L'équipe Facturation</strong></p>
                    </div>
                </body>
            </html>
            """, invoiceId, amount, java.time.LocalDate.now());

        sendEmail(recipientEmail, subject, htmlBody);
    }

    /**
     * Envoyer un email de réinitialisation de mot de passe
     */
    public void sendPasswordResetEmail(String recipientEmail, String resetToken , ROLES role) throws Exception {
        String subject = "Réinitialisation de votre mot de passe";
        String resetLink = "http://localhost:3000/reset-password?pwdToken=" + resetToken+"&pwdRole="+role+"&email="+recipientEmail;
        String htmlBody = String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background-color: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                        <h2 style="color: #333; margin-bottom: 20px;">Réinitialisation de mot de passe</h2>
                        <p style="color: #666; font-size: 16px;">Bonjour,</p>
                        <p style="color: #666; font-size: 16px;">Vous avez demandé une réinitialisation de mot de passe. Cliquez sur le bouton ci-dessous:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background-color: #007bff; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;">Réinitialiser mon mot de passe</a>
                        </div>
                        <p style="color: #999; font-size: 14px;">Ce lien expire dans 10-12 minutes.</p>
                        <p style="color: #999; font-size: 14px;">Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                        <p style="color: #999; font-size: 14px;">Cordialement,<br/><strong>L'équipe Facturation</strong></p>
                    </div>
                </body>
            </html>
            """, resetLink);

        sendEmail(recipientEmail, subject, htmlBody);
    }

    /**
     * Envoyer un email personnalisé via SendGrid
     */
    public void sendEmail(String to, String subject, String htmlBody) throws Exception {
        try {
            System.out.println("📧 Envoi d'email via SendGrid");
            System.out.println("   De: " + fromEmail);
            System.out.println("   À: " + to);

            // Créer l'email
            Email from = new Email(fromEmail, "Invoice Manager");
            Email toEmail = new Email(to);
            Content content = new Content("text/html", htmlBody);
            Mail mail = new Mail(from, subject, toEmail, content);

            // Envoyer via SendGrid
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() == 202) {
                System.out.println("✅ Email envoyé avec succès à: " + to);
            } else {
                System.err.println("❌ Erreur SendGrid: " + response.getStatusCode() + " - " + response.getBody());
                throw new Exception("SendGrid error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
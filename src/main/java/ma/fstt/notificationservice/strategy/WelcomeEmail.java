package ma.fstt.notificationservice.strategy;


import jakarta.mail.MessagingException;
import ma.fstt.notificationservice.exception.WelcomeEmailFailureException;
import ma.fstt.notificationservice.model.Notification;
import ma.fstt.notificationservice.service.EmailService;
import ma.fstt.notificationservice.utils.JsonUtil;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class WelcomeEmail implements ChannelStrategy {

    private final EmailService emailService;
    private final JsonUtil jsonUtil;

    public WelcomeEmail(EmailService emailService, JsonUtil jsonUtil) {
        this.emailService = emailService;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void send(Notification request) throws WelcomeEmailFailureException {
        String subject = "Welcome to our The Rent Five!";

        Map<String, Object> data = jsonUtil.jsonToMap(request.getMessage());

        String userEmail = (String) data.get("userEmail");

        String htmlMessage = welcomeEmailTemplate();

        try {
            emailService.sendEmail(userEmail, subject, htmlMessage);
        } catch (MessagingException e) {
            throw new WelcomeEmailFailureException(e.getMessage());
        }
    }

    private String welcomeEmailTemplate()
    {
        return "<html>"
                + "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;\">"
                + "    <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
                + "        <tr>"
                + "            <td align=\"center\" style=\"padding: 20px 0;\">"
                + "                <table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\">"
                + "                    <tr>"
                + "                        <td style=\"padding: 40px 30px 20px 30px; text-align: center; border-bottom: 1px solid #eeeeee;\">"
                + "                            <h1 style=\"color: #333333; margin: 0; font-size: 28px;\">👋 Bienvenue chez The Rent Five!</h1>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td style=\"padding: 30px; color: #555555; font-size: 16px; line-height: 1.6;\">"
                + "                            <p style=\"margin-top: 0;\">Nous sommes ravis de vous compter parmi nous.</p>"
                + "                            <p>Votre inscription a été confirmée. Vous pouvez désormais explorer notre plateforme de location et trouver la propriété idéale.</p>"
                + "                            <p style=\"text-align: center; padding: 20px 0;\">"
                + "                                <a href=\"[LIEN_VERS_VOTRE_SITE]\" target=\"_blank\" style=\"display: inline-block; padding: 12px 25px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold;\">"
                + "                                    Commencer l'exploration"
                + "                                </a>"
                + "                            </p>"
                + "                            <p>Si vous avez des questions, n'hésitez pas à contacter notre support.</p>"
                + "                            <p style=\"margin-bottom: 0;\">À très bientôt,<br>L'équipe The Rent Five</p>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td style=\"padding: 20px 30px; text-align: center; font-size: 12px; color: #aaaaaa; border-top: 1px solid #eeeeee;\">"
                + "                            <p style=\"margin: 0;\">&copy; 2025 The Rent Five. Tous droits réservés.</p>"
                + "                        </td>"
                + "                    </tr>"
                + "                </table>"
                + "            </td>"
                + "        </tr>"
                + "    </table>"
                + "</body>"
                + "</html>";
    }
}

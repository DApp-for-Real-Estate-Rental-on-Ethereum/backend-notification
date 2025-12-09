package ma.fstt.notificationservice.strategy;

import jakarta.mail.MessagingException;
import ma.fstt.notificationservice.exception.WelcomeEmailFailureException;
import ma.fstt.notificationservice.model.Notification;
import ma.fstt.notificationservice.service.EmailService;
import ma.fstt.notificationservice.utils.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PasswordResetEmail implements ChannelStrategy {

    private final JsonUtil jsonUtil;

    private final EmailService emailService;

    public PasswordResetEmail(EmailService emailService, JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
        this.emailService = emailService;
    }

    @Override
    public void send(Notification request) throws WelcomeEmailFailureException {
        String subject = "Password Reset";

        Map<String, Object> data = jsonUtil.jsonToMap(request.getMessage());

        String code = (String) data.get("token"); // The token field now contains the reset code
        String expiresAt = (String) data.get("expiresAt");
        String fullName = (String) data.get("fullName");
        String userEmail = (String) data.get("userEmail");

        String htmlMessage = passwordResetTemplate(fullName, code, expiresAt);

        try {
            emailService.sendEmail(userEmail, subject, htmlMessage);
        } catch (MessagingException e) {
            throw new WelcomeEmailFailureException(e.getMessage());
        }
    }

    private String passwordResetTemplate(String fullName, String code, String expiresAt) {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\" />");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        sb.append("<title>Password Reset</title>");
        sb.append("<style>");
        sb.append("body { font-family: Arial, sans-serif; background-color: #f4f4f7; color: #333; margin: 0; padding: 0; }");
        sb.append(".container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden; }");
        sb.append(".header { background-color: #007bff; color: white; text-align: center; padding: 20px; }");
        sb.append(".content { padding: 30px; line-height: 1.6; }");
        sb.append(".content h2 { color: #007bff; }");
        sb.append(".button { display: inline-block; background-color: #007bff; color: white; padding: 12px 24px; border-radius: 5px; text-decoration: none; margin-top: 20px; font-weight: bold; }");
        sb.append(".footer { text-align: center; font-size: 12px; color: #999; padding: 20px; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div class=\"container\">");
        sb.append("<div class=\"header\"><h1>Password Reset Request</h1></div>");
        sb.append("<div class=\"content\">");

        sb.append("<p>Hi ").append(fullName).append(",</p>");
        sb.append("<p>We received a request to reset your password for your account. Use the code below to reset your password:</p>");

        sb.append("<div style=\"text-align: center; margin: 30px 0;\">");
        sb.append("<div style=\"display: inline-block; background-color: #f0f0f0; padding: 20px 40px; border-radius: 8px; font-size: 32px; font-weight: bold; letter-spacing: 8px; font-family: monospace; color: #007bff;\">");
        sb.append(code);
        sb.append("</div>");
        sb.append("</div>");

        sb.append("<p>Enter this code on the password reset page to set a new password.</p>");
        sb.append("<p>This code will expire on <strong>").append(expiresAt).append("</strong>.</p>");
        sb.append("<p>If you did not request a password reset, you can safely ignore this email.</p>");
        sb.append("<p>Thank you,<br>The Support Team</p>");
        sb.append("</div>");

        sb.append("<div class=\"footer\">&copy; ")
                .append(java.time.Year.now().getValue())
                .append(" Your Company. All rights reserved.</div>");

        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}

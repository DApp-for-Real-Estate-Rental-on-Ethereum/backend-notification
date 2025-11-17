package ma.fstt.notificationservice.strategy;

import jakarta.mail.MessagingException;
import ma.fstt.notificationservice.exception.VerificationEmailFailureException;
import ma.fstt.notificationservice.model.Notification;
import ma.fstt.notificationservice.service.EmailService;
import ma.fstt.notificationservice.utils.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccountVerificationEmail implements ChannelStrategy {

    private final EmailService emailService;
    private final JsonUtil jsonUtil;

    public AccountVerificationEmail(EmailService emailService, JsonUtil jsonUtil) {
        this.emailService = emailService;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void send(Notification request)  {
        String subject = "Account Verification";

        Map<String, Object> data = jsonUtil.jsonToMap(request.getMessage());

        String userEmail = (String) data.get("userEmail");

        Integer verificationCode = Integer.parseInt(data.get("verificationCode").toString());

        String htmlMessage = verificationEmailTemplate(verificationCode);

        try {
            emailService.sendEmail(userEmail, subject, htmlMessage);
        } catch (MessagingException e) {
            //throw new VerificationEmailFailureException(e.getMessage());
        }
    };

    private String verificationEmailTemplate(Integer code) {
        String verificationCode = "VERIFICATION CODE " + code;

        return "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}

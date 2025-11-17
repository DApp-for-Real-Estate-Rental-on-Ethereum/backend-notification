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

        String token = (String) data.get("token");
        String expiresAt = (String) data.get("expiresAt");
        String fullName = (String) data.get("fullName");
        String userEmail = (String) data.get("userEmail");

        String htmlMessage = passwordResetTemplate(fullName, token, expiresAt);

        try {
            emailService.sendEmail(userEmail, subject, htmlMessage);
        } catch (MessagingException e) {
            throw new WelcomeEmailFailureException(e.getMessage());
        }
    }

    private String passwordResetTemplate(String fullName, String token, String expiresAt) {
        String resetLink = "localhost:8080/reset-password?token=" + token;

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Password Reset</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f7;
                    color: #333;
                    margin: 0;
                    padding: 0;
                }
                .container {
                    max-width: 600px;
                    margin: 40px auto;
                    background-color: #ffffff;
                    border-radius: 10px;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    overflow: hidden;
                }
                .header {
                    background-color: #007bff;
                    color: white;
                    text-align: center;
                    padding: 20px;
                }
                .content {
                    padding: 30px;
                    line-height: 1.6;
                }
                .content h2 {
                    color: #007bff;
                }
                .button {
                    display: inline-block;
                    background-color: #007bff;
                    color: white;
                    padding: 12px 24px;
                    border-radius: 5px;
                    text-decoration: none;
                    margin-top: 20px;
                    font-weight: bold;
                }
                .footer {
                    text-align: center;
                    font-size: 12px;
                    color: #999;
                    padding: 20px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Password Reset Request</h1>
                </div>
                <div class="content">
                    <p>Hi %s,</p>
                    <p>We received a request to reset your password for your account. Click the button below to set a new password:</p>
                    <p style="text-align: center;">
                        <a href="%s" class="button">Reset Password</a>
                    </p>
                    <p>This link will expire on <strong>%s</strong>.</p>
                    <p>If you did not request a password reset, you can safely ignore this email.</p>
                    <p>Thank you,<br>The Support Team</p>
                </div>
                <div class="footer">
                    &copy; %d Your Company. All rights reserved.
                </div>
            </div>
        </body>
        </html>
        """.formatted(fullName, resetLink, expiresAt, java.time.Year.now().getValue());
    }
}

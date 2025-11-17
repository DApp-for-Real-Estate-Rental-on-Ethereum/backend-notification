package ma.fstt.notificationservice.factory;

import ma.fstt.notificationservice.enums.ChannelTypeEnum;
import ma.fstt.notificationservice.service.EmailService;
import ma.fstt.notificationservice.strategy.*;
import ma.fstt.notificationservice.utils.JsonUtil;
import org.springframework.stereotype.Component;

@Component
public class ChannelFactory {

    private final EmailService emailService;
    private final JsonUtil jsonUtil;

    private final AccountVerificationEmail accountVerificationEmail;
    private final WelcomeEmail welcomeEmail;
    private final PasswordResetEmail passwordResetEmail;
    private final InApp inApp;

    public ChannelFactory(
            EmailService emailService,
            JsonUtil jsonUtil,
            AccountVerificationEmail accountVerificationEmail,
            WelcomeEmail welcomeEmail,
            PasswordResetEmail passwordResetEmail,
            InApp inApp
    ) {
        this.emailService = emailService;
        this.jsonUtil = jsonUtil;
        this.accountVerificationEmail = accountVerificationEmail;
        this.welcomeEmail = welcomeEmail;
        this.passwordResetEmail = passwordResetEmail;
        this.inApp = inApp;
    }

    public ChannelStrategy getChannel(ChannelTypeEnum channelType) {
        switch (channelType) {
            case ACCOUNT_VERIFICATION_EMAIL:
                return accountVerificationEmail;
            case WELCOME_EMAIL:
                return welcomeEmail;
            case PASSWORD_RESET_EMAIL:
                return passwordResetEmail;
            case IN_APP:
                return inApp;
            default:
                throw new IllegalArgumentException("Unknown channel type: " + channelType);
        }
    }
}

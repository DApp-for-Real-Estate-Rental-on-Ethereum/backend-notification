package ma.fstt.notificationservice.exception;

public class WelcomeEmailFailureException extends RuntimeException {
    public WelcomeEmailFailureException(String message) {
        super(message);
    }

    public WelcomeEmailFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}

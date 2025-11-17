package ma.fstt.notificationservice.strategy;

import ma.fstt.notificationservice.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class InApp implements ChannelStrategy {
    @Override
    public void send(Notification request) {}
}

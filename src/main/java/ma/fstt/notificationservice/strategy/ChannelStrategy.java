package ma.fstt.notificationservice.strategy;

import ma.fstt.notificationservice.model.Notification;

public interface ChannelStrategy {
    void send(Notification request);
}

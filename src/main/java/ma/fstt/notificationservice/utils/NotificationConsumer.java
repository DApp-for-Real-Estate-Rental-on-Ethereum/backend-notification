package ma.fstt.notificationservice.utils;

import ma.fstt.notificationservice.configuration.RabbitMQConfiguration;
import ma.fstt.notificationservice.dto.NotificationRequestDTO;
import ma.fstt.notificationservice.model.Notification;
import ma.fstt.notificationservice.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void consume(NotificationRequestDTO request) throws Exception {

        Notification notification =notificationService.createNotification(request);

        if (notification == null) {
            throw new RuntimeException("Failed to create notification record. See logs for details.");
        }

        notificationService.sendNotification(notification);
    }
}

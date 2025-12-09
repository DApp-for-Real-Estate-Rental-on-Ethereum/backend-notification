package ma.fstt.notificationservice.service;

import jakarta.transaction.Transactional;
import ma.fstt.notificationservice.dto.NotificationRequestDTO;
import ma.fstt.notificationservice.enums.LogLevelEnum;
import ma.fstt.notificationservice.enums.StatusTypeEnum;
import ma.fstt.notificationservice.factory.ChannelFactory;
import ma.fstt.notificationservice.model.Notification;
import ma.fstt.notificationservice.model.NotificationLog;
import ma.fstt.notificationservice.repository.NotificationRepository;
import ma.fstt.notificationservice.strategy.ChannelStrategy;
import ma.fstt.notificationservice.utils.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationLogService notificationLogService;
    private final JsonUtil jsonUtil;
    private final ChannelFactory channelFactory;

    public NotificationService(
            NotificationRepository notificationRepository,
            NotificationLogService notificationLogService,
            JsonUtil jsonUtil,
            ChannelFactory channelFactory
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationLogService = notificationLogService;
        this.jsonUtil = jsonUtil;
        this.channelFactory = channelFactory;
    }

    @Transactional
    public Notification createNotification(NotificationRequestDTO request) {
        try{
            Notification notification = new Notification();
            notification.setChannel(request.getChannel());
            notification.setCreatedAt(request.getCreatedAt());
            notification.setUserId(request.getUserId());
            notification.setStatus(StatusTypeEnum.PENDING);
            notification.setMessage(jsonUtil.messageConverter(request.getMessage()));

            Notification savedNotification = notificationRepository.save(notification);

            notificationLogService.addLog(
                    "Notification record created for user: " + request.getUserId(),
                    LogLevelEnum.INFO,
                    savedNotification
            );

            return savedNotification;
        } catch (Exception e) {
            notificationLogService.addLog(
                    e.getMessage(),
                    LogLevelEnum.ERROR
            );
            return null;
        }
    }

    public void sendNotification(Notification notification) throws Exception {
        ChannelStrategy channelStrategy = channelFactory.getChannel(notification.getChannel());
        try {
            channelStrategy.send(notification);

            notification.setCreatedAt(notification.getCreatedAt());
            notification.markAsSent();

            notificationRepository.save(notification);

            notificationLogService.addLog(
                    "Notification was sent to : " + notification.getUserId(),
                    LogLevelEnum.INFO,
                    notification
            );
        } catch (Exception e) {
            notification.markAsFailed();
            notificationRepository.save(notification);
            notificationLogService.addLog(
                    e.getMessage(),
                    LogLevelEnum.ERROR,
                    notification
            );

            throw new RuntimeException("Failed to send notification. Moving to DLQ.", e);
        }

    }

    public Notification getNofication(){return null;}

}

package ma.fstt.notificationservice.service;

import ma.fstt.notificationservice.enums.LogLevelEnum;
import ma.fstt.notificationservice.model.Notification;
import ma.fstt.notificationservice.model.NotificationLog;
import ma.fstt.notificationservice.repository.NotificationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationLogService {
    private final NotificationLogRepository notificationLogRepository;

    public NotificationLogService(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    @Transactional
    public void addLog(String message, LogLevelEnum level, Notification notification) {
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setMessage(message);
        notificationLog.setLevel(level);
        notificationLog.setNotification(notification);

        notificationLogRepository.save(notificationLog);
    }

    @Transactional
    public void addLog(String message, LogLevelEnum level) {
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setMessage(message);
        notificationLog.setLevel(level);

        notificationLogRepository.save(notificationLog);
    }

    public List<NotificationLog> getLogs(){return null;}

    public NotificationLog getLog(){return null;}
}

package ma.fstt.notificationservice.repository;

import ma.fstt.notificationservice.enums.StatusTypeEnum;
import ma.fstt.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}

package ma.fstt.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.fstt.notificationservice.enums.LogLevelEnum;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notification_logs")
public class NotificationLog {

    public NotificationLog(Notification notification, LogLevelEnum level, String message) {
        this.notification = notification;
        this.level = level;
        this.message = message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column
    private LocalDateTime timestamp;

    @Column
    private String message;

    @Column
    private LogLevelEnum level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private Notification notification;
}

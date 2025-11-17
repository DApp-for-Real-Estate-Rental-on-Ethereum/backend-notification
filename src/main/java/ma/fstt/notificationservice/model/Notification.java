package ma.fstt.notificationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.fstt.notificationservice.enums.ChannelTypeEnum;
import ma.fstt.notificationservice.enums.StatusTypeEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column
    private String message;

    @Column
    private ChannelTypeEnum channel;

    @Column
    private StatusTypeEnum status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "seen_at")
    private LocalDateTime seenAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationLog> logs;

    public void markAsSeen() {
        this.seenAt = LocalDateTime.now();
        this.status = StatusTypeEnum.SEEN;
    }

    public void markAsSent() {
        this.sentAt = LocalDateTime.now();
        this.status = StatusTypeEnum.SENT;
    }

    public void markAsFailed() {
        this.status = StatusTypeEnum.FAILED;
    }
}

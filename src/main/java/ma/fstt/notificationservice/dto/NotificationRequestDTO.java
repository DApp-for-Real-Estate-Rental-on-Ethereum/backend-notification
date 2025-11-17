package ma.fstt.notificationservice.dto;

import lombok.Data;
import ma.fstt.notificationservice.enums.ChannelTypeEnum;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class NotificationRequestDTO {
    String userId;
    Map<String, Object> message;
    ChannelTypeEnum channel;
    LocalDateTime createdAt;
}

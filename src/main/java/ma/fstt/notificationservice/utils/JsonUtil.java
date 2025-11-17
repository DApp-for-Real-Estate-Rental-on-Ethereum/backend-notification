package ma.fstt.notificationservice.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String messageConverter(Map<String, Object> message){
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message data", e);
        }
    }

    public Map<String, Object> jsonToMap(String jsonMessage) {
        if (jsonMessage == null || jsonMessage.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(
                    jsonMessage,
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON message data to Map.", e);
        }
    }
}

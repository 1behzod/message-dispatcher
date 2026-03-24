package uz.behzod.message_dispatcher.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@Component
public class AppProperties {

    private Message message = new Message();

    @Getter
    @Setter
    public static class Message {
        private String appId;
        private String targetUrl;
        private int batchSize;
        private int maxRetries;
        private int lockTimeoutMinutes;
    }

}

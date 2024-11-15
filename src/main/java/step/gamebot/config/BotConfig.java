package step.gamebot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegrambot")
@Getter
@Setter
public class BotConfig {
    private String botToken;
    private String botUsername;
}

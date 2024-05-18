package telegrambot.TemplateForTelegramBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.properties")
@Data
public class TelegramBotConfig {
    @Value("${bot.name}")
    String telegramBotName;
    @Value("${bot.token}")
    String telegramBotToken;
}

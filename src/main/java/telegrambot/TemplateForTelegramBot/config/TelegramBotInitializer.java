package telegrambot.TemplateForTelegramBot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.TemplateForTelegramBot.service.TelegramBotService;

@Slf4j
@Component
public class TelegramBotInitializer {

    private final TelegramBotService telegramBotService;

    @Autowired
    public TelegramBotInitializer(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotService);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}

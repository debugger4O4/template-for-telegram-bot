package rems.telegrambot.ChlorophyllNetBot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import rems.telegrambot.ChlorophyllNetBot.config.TelegramBotConfig;
import rems.telegrambot.ChlorophyllNetBot.entity.User;
import rems.telegrambot.ChlorophyllNetBot.repository.UserRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramBotConfig telegramBotConfig;
    private static final String HELP = "У нас было два пакетика травы, семьдесят пять ампул мескалина, 5 пакетиков диэтиламида лизергиновой кислоты или ЛСД, солонка, наполовину наполненная кокаином, и целое море разноцветных амфетаминов, барбитуратов и транквилизаторов, а так же литр текилы, литр рома, ящик «Бадвайзера», пинта чистого эфира, и 12 пузырьков амилнитрита. Не то, чтобы всё это было категорически необходимо в поездке, но если уж начал собирать коллекцию, то к делу надо подходить серьёзно.";

    private final UserRepository userRepository;

    @Autowired
    public TelegramBotService(TelegramBotConfig telegramBotConfig, UserRepository userRepository) {
        this.telegramBotConfig = telegramBotConfig;
        this.userRepository = userRepository;
        List<BotCommand> menu = new ArrayList<>();
        menu.add(new BotCommand("/start", "поздороваться с братишкой"));
        menu.add(new BotCommand("/makeorder", "сделать заказ"));
        menu.add(new BotCommand("/help", "распедалить, чё по чём"));
        try {
            this.execute(new SetMyCommands(menu, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String s = update.getMessage().getFrom().getUserName();
        String user = userRepository.getUserByUserName(s);
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start" -> {
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    registerUser(chatId, update.getMessage(), user);
                }
                case "/makeorder" -> getUserInfo(chatId, update.getMessage().getFrom().getId(), update.getMessage().getChat().getFirstName());
                case "/help" -> getHelp(chatId, update.getMessage().getChat().getFirstName());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getTelegramBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getTelegramBotToken();
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode( "Здравствуйте. Ввведите ваш номер телефона и адрес для регистрации" + ":blush:");
        sendMessage(chatId, answer);
        log.info(name + " использует бота.");
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void getHelp(long chatId, String name) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(name);
        message.setText(TelegramBotService.HELP);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void getUserInfo(long chatId, Long userId, String firstName) {
            log.info("{ chatId:" + chatId + ", userId:" + userId + ", firstName: " + firstName + " }");
    }

    private void registerUser(long chatId, Message message, String user) {
        Chat chat = message.getChat();
        if (!userRepository.getUserByUserName(user).isEmpty()) {
            String answer = "С возвращением " + user;
            sendMessage(chatId, answer);
        } else {
            User u = new User();

            u.setChatId(chatId);
            u.setUserName(chat.getUserName());
            u.setFirstName(chat.getFirstName());
            u.setLastName(chat.getLastName());
            u.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(u);
            log.info(user + " зрегестрирован");
        }
    }
}


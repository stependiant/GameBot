package step.gamebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import step.gamebot.config.BotProperties;
import step.gamebot.model.User;
import step.gamebot.model.UserState;
import step.gamebot.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebhookReceive extends TelegramWebhookBot {

    private final BotProperties botProperties;
    private final UserRepository userRepository;

    @Autowired
    public WebhookReceive(BotProperties botProperties, UserRepository userRepository) {
        super(botProperties.getBotToken());
        this.botProperties = botProperties;
        this.userRepository = userRepository;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("got update: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedMessage = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            if (receivedMessage.equals("/start")) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());
                message.setText("Добро пожаловать! Нажмите кнопку ниже, чтобы добавить пользователя.");

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton addUserButton = new InlineKeyboardButton();
                addUserButton.setText("Добавить пользователя");
                addUserButton.setCallbackData("addUser");
                row.add(addUserButton);
                keyboard.add(row);
                keyboardMarkup.setKeyboard(keyboard);

                message.setReplyMarkup(keyboardMarkup);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                User user = userRepository.findByUsername(username);

                if (user != null) {
                    if (user.getState() == UserState.ENTERING_USERNAME) {
                        user.setUsername(receivedMessage);
                        user.setState(UserState.ENTERING_LASTNAME);
                        userRepository.save(user);

                        SendMessage message = new SendMessage();
                        message.setChatId(chatId.toString());
                        message.setText("Введите фамилию:");

                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (user.getState() == UserState.ENTERING_LASTNAME) {
                        user.setLastname(receivedMessage);
                        user.setState(UserState.ENTERING_EMAIL);
                        userRepository.save(user);

                        SendMessage message = new SendMessage();
                        message.setChatId(chatId.toString());
                        message.setText("Введите email:");

                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (user.getState() == UserState.ENTERING_EMAIL) {
                        user.setEmail(receivedMessage);
                        user.setState(UserState.INITIAL);
                        userRepository.save(user);

                        SendMessage message = new SendMessage();
                        message.setChatId(chatId.toString());
                        message.setText("Данные пользователя сохранены.");

                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String username = update.getCallbackQuery().getFrom().getUserName();

            if (callbackData.equals("addUser")) {
                User user = new User();
                user.setUsername(username);
                user.setState(UserState.ENTERING_USERNAME);
                userRepository.save(user);

                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());
                message.setText("Введите имя пользователя:");

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public String getBotPath() {
        return botProperties.getBotWebhookPath();
    }

    @Override
    public String getBotUsername() {
        return botProperties.getBotUsername();
    }
}

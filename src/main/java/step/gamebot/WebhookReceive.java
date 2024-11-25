package step.gamebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import step.gamebot.config.BotProperties;

@Component
public class WebhookReceive extends TelegramWebhookBot {

    private final BotProperties botProperties;

    @Autowired
    public WebhookReceive(BotProperties botProperties) {
        super(botProperties.getBotToken());
        this.botProperties = botProperties;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("Получено обновление: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedMessage = update.getMessage().getText();
            String responseText = "Вы отправили: " + receivedMessage;
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(responseText);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
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

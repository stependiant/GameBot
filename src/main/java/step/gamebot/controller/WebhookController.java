package step.gamebot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import step.gamebot.WebhookReceive;

@RestController
public class WebhookController {
    private final WebhookReceive webhookReceive;

    @Autowired
    public WebhookController(WebhookReceive webhookReceive) {
        this.webhookReceive = webhookReceive;
    }

    @PostMapping("/webhook")
    public void onUpdateReceived(@RequestBody Update update) {
        webhookReceive.onWebhookUpdateReceived(update);
    }

}

package com.shusainov.tgbot;


import com.google.gson.JsonObject;
import com.shusainov.tgbot.api.methods.GetMe;
import com.shusainov.tgbot.api.methods.GetUpdates;
import com.shusainov.tgbot.api.methods.SendMessage;
import com.shusainov.tgbot.db.StoreItemRepository;
import com.shusainov.tgbot.db.models.StoreItem;
import com.shusainov.tgbot.api.models.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
public class TgBot {
    private Logger log = LoggerFactory.getLogger(TgBot.class);
    private static volatile boolean status = false;
    private static volatile int lastUpdateID = 1;
    private static LinkedBlockingQueue<Update> updates = new LinkedBlockingQueue<>();

    @Autowired
    StoreItemRepository repository;

    @Autowired
    Config config;

    @Scheduled(fixedRate = 1000)
    public void checkStatus() {
        //TODO надо подумать если получу 409ю ошибку бот будет спать 1 минуту.
        GetMe getMe = new GetMe(config.token, config.connectionTimeout);
        status = getMe.execute().get("ok").getAsBoolean();
    }

    @Scheduled(fixedDelay = 2000)
    public void getUpdates() {
        if (!status) return;
        GetUpdates method = new GetUpdates(config.token, config.connectionTimeout);
        method.setOffset(lastUpdateID + 1);
        JsonObject JsonRequest = method.execute();

        if (!JsonRequest.get("ok").getAsBoolean()) {
            log.error("Get error: " + JsonRequest);
            return;
        }

        updates.addAll(List.of(method.getResult()));
        lastUpdateID = updates.stream().max(Comparator.comparing(Update::getUpdate_id)).orElse(new Update(lastUpdateID)).getUpdate_id();
        log.info("LastUpdateID: " + lastUpdateID);
    }

    @Scheduled(fixedDelay = 2000)
    public void sendMessages() {
        if (!status || updates.size() < 1) return;
        Update update = updates.poll();
        if (update == null) return;
        long chatID = update.getMessage().getChat().getId();
        String incomingText = update.getMessage().getText();
        String resultText = "Не понял тебя";
        //TODO здесь происходит обработка данных, надо бы как то улучшить. Может получится слишком сложный метод
        switch (incomingText) {
            case "/help":
            case "/start": {
                resultText = "Bot для отслеживания цен в маркетах OZON, и тд.. \n" + "напиши например 3060: покажет видяхи RTX 3060, \n" + "в данный момент парсит Ozon: 3060,3070,3080, 6TB";
                //TODO как то надо показать доступные DataSets
                break;
            }
            default: {
                //TODO нужно как то ограничить выгрузку возвращает слишком много данных
                StoreItem storeItem = repository.searchLastByLike(incomingText).stream().findFirst().orElse(new StoreItem());
                if (storeItem != null) {
                    String sendingTextFormat = "Магазин: %s Цена: %s id: %s\n %s";
                    resultText = String.format(sendingTextFormat, storeItem.getStoreName(), storeItem.getPrice(), storeItem.getId(), storeItem.getLink());
                }
            }
        }
        SendMessage sendMessage = new SendMessage(config.token, config.connectionTimeout, chatID, resultText);
        sendMessage.execute();
    }
}


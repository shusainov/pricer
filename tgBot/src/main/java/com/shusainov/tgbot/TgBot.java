package com.shusainov.tgbot;


import com.shusainov.tgbot.db.StoreItemRepository;
import com.shusainov.tgbot.db.models.StoreItem;
import com.shusainov.tgbot.models.Update;
import com.shusainov.tgbot.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@EnableAutoConfiguration
@EnableScheduling
public class TgBot {
    private Logger log = LoggerFactory.getLogger(TgBot.class);
    private final TgAPI tgAPI = new TgAPI(Config.get("TOKEN"));
    private static volatile boolean status = false;
    private static volatile int lastUpdateID = 1;
    private static LinkedBlockingQueue<Update> updates = new LinkedBlockingQueue<>();

    @Autowired
    StoreItemRepository repository;

    @Scheduled(fixedRate = 60000)
    public void checkStatus() {
        //TODO надо подумать если получу 409ю ошибку бот будет спать 1 минуту.
        status = tgAPI.checkMyStatus();
    }

    @Scheduled(fixedDelay = 2000)
    public void getUpdates() {
        if (!status) return;
        Update[] request = tgAPI.getUpdates(lastUpdateID + 1);
        updates.addAll(List.of(request));
        lastUpdateID = updates.stream().max(Comparator.comparing(Update::getUpdate_id)).orElse(new Update(lastUpdateID)).getUpdate_id();
        log.info("Last Update ID: " + lastUpdateID);
    }

    @Scheduled(fixedDelay = 2000)
    public void sendMessages() {
        if (!status) return;
        Update update = updates.poll();
        if (update == null) return;

        long chatID = update.getMessage().getChat().getId();
        System.out.println(chatID);
        String incomingText = update.getMessage().getText();
        System.out.println(incomingText);
        String resultText = "Не понял тебя";
        //TODO здесь происходит обработка данных, надо бы как то улучшить. Может получится слишком сложный метод
        switch (incomingText) {
            case "/help":
            case "/start": {
                resultText = "Bot для отслеживания цен в маркетах OZON, и тд.. \n" +
                        "напиши например 3060: покажет видяхи RTX 3060, \n" +
                        "в данный момент парсит Ozon: 3060,3070,3080, 6TB";
                //TODO как то надо показать доступные DataSets
                break;
            }
            default: {
                //TODO нужно както ограничить выгрузку возвращает слишком много данных
                StoreItem storeItem = repository.searchLastByLike(incomingText)
                        .stream().findFirst().orElse(new StoreItem());
                if (storeItem != null) {
                    String sendingTextFormat = "Магазин: %s Цена: %s id: %s\n %s";
                    resultText = String.format(sendingTextFormat,
                            storeItem.getStoreName(), storeItem.getPrice(), storeItem.getId(),
                            storeItem.getLink());
                }
            }
        }
        tgAPI.sendMessage(chatID, resultText);

    }
}


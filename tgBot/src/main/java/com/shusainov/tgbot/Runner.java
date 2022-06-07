package com.shusainov.tgbot;

import com.shusainov.tgbot.db.StoreItemRepository;
import com.shusainov.tgbot.db.models.StoreItem;
import com.shusainov.tgbot.models.Update;
import com.shusainov.tgbot.utils.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Comparator;

@SpringBootApplication
public class Runner {
    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    @Bean
    public CommandLineRunner botRunner(StoreItemRepository repository) {
        return (args -> {
            TgAPI tgAPI = new TgAPI(Config.get("TOKEN"));
            int lastID = 1;
            while (true) {
                Update[] updates = tgAPI.getUpdates(lastID);
                Arrays.stream(updates).forEach(x -> {
                    long chatID = x.getMessage().getChat().getId();
                    System.out.println(chatID);
                    String incomingText = x.getMessage().getText();
                    System.out.println(incomingText);
                    String resultText="Не понял тебя";
                    //TODO здесь происходит обработка данных, надо бы как то улучшить. Может получится слищком сложный метод
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
                            if (storeItem!=null) {
                                String sendingTextFormat = "Магазин: %s Цена: %s id: %s\n %s";
                                resultText = String.format(sendingTextFormat,
                                        storeItem.getStoreName(),storeItem.getPrice(),storeItem.getId(),
                                        storeItem.getLink());
                            }
                        }
                    }
                    tgAPI.sendMessage(chatID, resultText);
                });

                lastID = Arrays.stream(updates).max(Comparator.comparing(Update::getUpdate_id)).orElse(new Update(lastID - 1)).getUpdate_id() + 1;
                Thread.sleep(2000);
            }
        });
    }
}

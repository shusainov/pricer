package com.shusainov.tgbot;

import com.shusainov.tgbot.models.Update;
import com.shusainov.tgbot.utils.Config;

import java.util.Arrays;
import java.util.Comparator;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        TgAPI tgAPI = new TgAPI(Config.get("TOKEN"));
        int lastID = 1;
        while (true) {
            Update[] updates = tgAPI.getUpdates(lastID);
            Arrays.stream(updates).forEach(x -> {
                int chatID = x.getMessage().getChat().getId();
                String text = x.getMessage().getText();
                tgAPI.sendMessage(chatID, text);
            });
            lastID = Arrays.stream(updates).max(Comparator.comparing(Update::getUpdate_id)).orElse(new Update(lastID-1)).getUpdate_id() + 1;
            System.out.println(lastID);
            Thread.sleep(2000);
        }
    }
}

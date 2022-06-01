package com.shusainov.tgbot;

import com.shusainov.tgbot.utils.Config;

public class Runner {
    public static void main(String[] args) {
        Updater updater = new Updater(Config.get("TOKEN"));
        System.out.println(updater.checkMyStatus());
    }
}

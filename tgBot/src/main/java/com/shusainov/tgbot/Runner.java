package com.shusainov.tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Runner {
    public static void main(String[] args) {
        SpringApplication.run(TgBot.class, args);
    }
}

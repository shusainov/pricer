package com.shusainov.tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(Config.class)
@SpringBootApplication
public class Runner {
    public static void main(String[] args) {
        SpringApplication.run(TgBot.class, args);
    }
}

package com.shusainov.tgbot;

public class Runner {
    public static void main(String[] args) {
        Updater updater = new Updater("15443541071:AAHUemb7fDkPhzE3r33Vrq2o3DJGLV7Eqac");
        System.out.println(updater.checkMyStatus());
    }
}

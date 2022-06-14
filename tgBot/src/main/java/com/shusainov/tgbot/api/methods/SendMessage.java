package com.shusainov.tgbot.api.methods;

public class SendMessage extends Method {
    public SendMessage(String token, int connectionTimeout) {
        super(token, connectionTimeout);
        setPath("SendMessage");
    }
}

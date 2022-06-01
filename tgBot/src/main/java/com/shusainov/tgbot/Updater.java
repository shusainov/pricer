package com.shusainov.tgbot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;


public class Updater {
    private String token;
    private int lastUpdateID;
    private String urlFormat = "https://api.telegram.org/bot%s/%s";

    public Updater(String token) {
        this.token = token;
    }

    public boolean checkMyStatus() {
        try {
            String request = Request.get(String.format(urlFormat, token, "getMe"))
                    .connectTimeout(Timeout.ofSeconds(10))
                    .responseTimeout(Timeout.ofSeconds(10))
                    .execute()
                    .returnContent().toString();

            JsonObject jsonObject =
                    new JsonParser().parse(request).getAsJsonObject();
            return jsonObject.get("ok").getAsBoolean();

        } catch (IOException e) {
           return false;
        }
    }
}

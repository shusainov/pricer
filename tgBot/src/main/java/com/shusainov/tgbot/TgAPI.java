package com.shusainov.tgbot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shusainov.tgbot.models.Message;
import com.shusainov.tgbot.models.Update;
import com.shusainov.tgbot.utils.Config;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class TgAPI {
    private Logger log = LoggerFactory.getLogger(TgAPI.class);
    private String token;

    public TgAPI(String token) {
        this.token = token;
    }

    public boolean checkMyStatus() {
        try {
            log.info("Status checking started...");
            String request = Request.post(String.format(Config.get("URL"), token, "getMe"))
                    .connectTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .responseTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .execute()
                    .returnContent().toString();

            JsonObject requestJson =
                    new JsonParser().parse(request).getAsJsonObject();

            log.info("Status checking end. Result:" + requestJson);

            return requestJson.get("ok").getAsBoolean();
        } catch (IOException e) {
            log.error(e.toString());
            return false;
        }
    }

    public Update[] getUpdates() {
        try {
            String request = Request.post(String.format(Config.get("URL"), token, "getUpdates"))
                    .connectTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .responseTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .execute()
                    .returnContent().toString();

            JsonObject requestJson =
                    new JsonParser().parse(request).getAsJsonObject();
            Gson gson = new Gson();

            return gson.fromJson(requestJson.get("result"), Update[].class);

        } catch (IOException e) {
            log.error("getUpdates():" + e.toString());
            return null;
        }
    }

    public Update[] getUpdates(int offset) {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("offset", String.valueOf(offset)));

            String request = Request.post(String.format(Config.get("URL"), token, "getUpdates"))
                    .bodyForm(params)
                    .connectTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .responseTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .execute()
                    .returnContent().toString();

            JsonObject requestJson =
                    new JsonParser().parse(request).getAsJsonObject();
            Gson gson = new Gson();
            log.info("Get updates: " + request);
            return gson.fromJson(requestJson.get("result"), Update[].class);

        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
    }

    public Message sendMessage(long chat_id, String text) {
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("chat_id", Long.toString(chat_id)));
            params.add(new BasicNameValuePair("text", text));
            String request = Request.post(String.format(Config.get("URL"), token, "sendMessage"))
                    .bodyForm(params, Charset.forName("UTF-8"))
                    .connectTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .responseTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .execute()
                    .returnContent().toString();
            JsonObject requestJson =
                    new JsonParser().parse(request).getAsJsonObject();
            Gson gson = new Gson();
            return gson.fromJson(requestJson.get("result"), Message.class);
        } catch (IOException e) {
            log.error("sendMessage():" + e.toString());
            return null;
        }
    }
}

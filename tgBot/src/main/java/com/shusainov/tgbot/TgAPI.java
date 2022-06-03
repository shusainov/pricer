package com.shusainov.tgbot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shusainov.tgbot.models.Message;
import com.shusainov.tgbot.models.Update;
import com.shusainov.tgbot.utils.Config;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.Timeout;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class TgAPI {
    private String token;

    public TgAPI(String token) {
        this.token = token;
    }

    public boolean checkMyStatus() {
        try {
            String request = Request.post(String.format(Config.get("URL"), token, "getMe"))
                    .connectTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .responseTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .execute()
                    .returnContent().toString();

            JsonObject jsonObject =
                    new JsonParser().parse(request).getAsJsonObject();
            return jsonObject.get("ok").getAsBoolean();

        } catch (IOException e) {
            System.out.println(e);
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

            JsonObject jsonObject =
                    new JsonParser().parse(request).getAsJsonObject();
            Gson gson = new Gson();

            return gson.fromJson(jsonObject.get("result"), Update[].class);

        } catch (IOException e) {
            System.out.println(e);
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

            JsonObject jsonObject =
                    new JsonParser().parse(request).getAsJsonObject();
            Gson gson = new Gson();

            return gson.fromJson(jsonObject.get("result"), Update[].class);

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public Message sendMessage(int chat_id, String text) {
        String request = null;
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("chat_id", Integer.toString(chat_id)));
            params.add(new BasicNameValuePair("text", text));
            request = Request.post(String.format(Config.get("URL"), token, "sendMessage"))
                    .bodyForm(params,Charset.forName("UTF-8"))
                    .connectTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .responseTimeout(Timeout.ofSeconds(Integer.parseInt(Config.get("TIMEOUT"))))
                    .execute()
                    .returnContent().toString();
            JsonObject jsonObject =
                    new JsonParser().parse(request).getAsJsonObject();
            Gson gson = new Gson();
            return gson.fromJson(jsonObject.get("result"), Message.class);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
}

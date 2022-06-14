package com.shusainov.tgbot.api.methods;

import com.google.gson.Gson;
import com.shusainov.tgbot.api.models.Update;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class GetUpdates extends Method {

    public GetUpdates(String token, int connectionTimeout) {
        super(token, connectionTimeout);
        setPath("getUpdates");
    }

    public void setOffset(long offset) {
        if (params == null) params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("offset", Long.toString(offset)));
    }

    public void setLimit(long limit) {
        if (params == null) params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("limit", Long.toString(limit)));
    }

    public void setTimeout(long timeout) {
        if (params == null) params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("timeout", Long.toString(timeout)));
    }

    public Update[] getResult() {
        Gson gson = new Gson();
        return gson.fromJson(requestJson.get("result"), Update[].class);
    }
}

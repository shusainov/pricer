package com.shusainov.tgbot.api.methods;

import org.apache.hc.core5.http.message.BasicNameValuePair;

public class SendMessage extends Method {
    private long chat_id;//Integer or String	Yes	Unique identifier for the target chat or username of the target channel (in the format @channelusername)
    private String text; //String	Yes	Text of the message to be sent

    public SendMessage(String token, int connectionTimeout, long chat_id, String text) {
        super(token, connectionTimeout);
        params.add(new BasicNameValuePair("chat_id", Long.toString(chat_id)));
        params.add(new BasicNameValuePair("text", text));
        setPath("SendMessage");
    }


}

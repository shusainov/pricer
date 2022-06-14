package com.shusainov.tgbot.api.methods;

import java.util.ArrayList;

public class GetMe extends Method {

    public GetMe(String token, int connectionTimeout) {
        super(token, connectionTimeout);
        params = new ArrayList<>();
        setPath("GetMe");
    }
}

package com.shusainov.tgbot.api.methods;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shusainov.tgbot.Config;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public abstract class Method {
    protected final Logger log = LoggerFactory.getLogger(Method.class);
    private final String telegramURL = "https://api.telegram.org/bot%s/%s";
    private String token;
    private int connectionTimeout;
    private String path;
    protected List<NameValuePair> params = new ArrayList<>();
    protected JsonObject requestJson = null;
    public Method(String token, int connectionTimeout) {
        this.token = token;
        this.connectionTimeout = connectionTimeout;
    }

    public String getPath() {
        return path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    public JsonObject execute() {

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(connectionTimeout))
                        .setResponseTimeout(Timeout.ofSeconds(connectionTimeout))
                        .setCookieSpec(StandardCookieSpec.STRICT)
                        .build())
                .build();

        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(new URIBuilder(String.format(telegramURL, token, path), StandardCharsets.UTF_8)
                    .addParameters(params.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                    .build());
        } catch (URISyntaxException e) {
            log.error("Cannot parse URI or parameters: " + e);
            throw new RuntimeException(e);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            requestJson = new JsonParser().parse(EntityUtils.toString(response.getEntity(), "UTF-8")).getAsJsonObject();
            return requestJson;
        } catch (IOException e) {
            log.error("Cannot send request: " + e);
            throw new RuntimeException(e);
        } catch (ParseException e) {
            log.error("Cannot parse request to JSON: " + e);
            throw new RuntimeException(e);
        }
    }
}

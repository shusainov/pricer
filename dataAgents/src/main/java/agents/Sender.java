package agents;

import agents.page.BasePage;
import agents.page.DNS;
import agents.page.Ozon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import utils.Config;

import java.io.FileReader;
import java.io.Reader;

public class Sender implements Runnable {
    private static volatile int threadsCount = 0;
    private final JsonObject dataSet;

    public Sender(JsonObject dataSet) {
        this.dataSet = dataSet;
    }

    private static final String dataSetPath = "dataSet.json";

    private static JsonArray getDataSet() {
        JsonArray dataSet = new JsonArray();

        try (Reader testDataFile = new FileReader(dataSetPath)) {
            dataSet = JsonParser.parseReader(testDataFile).getAsJsonArray();
        } catch (Exception e) {
            System.out.println("Cannot load dataSet file " + e);
        }
        return dataSet;
    }

    public static void main(String[] args) throws InterruptedException {

        JsonArray dataSets = getDataSet();
        for (JsonElement dataSet : dataSets) {
            Sender sender = new Sender(dataSet.getAsJsonObject());
            Thread thread = new Thread(sender);
            thread.start();
            Thread.sleep(1000);
            while (threadsCount >= Integer.valueOf(Config.get("THREAD_COUNT"))) {
                Thread.sleep(1000);
            }
        }
    }

    @Override
    public void run() {
        threadsCount++;
        System.out.println("Start thread, total thread count:" + threadsCount);
        try (Playwright playwright = Playwright.create()) {
            Browser browser = null;
            switch (Config.get("BROWSER")) {
                case "webkit": {
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(Config.get("HEADLESS").equals("true")).setSlowMo(1000));
                    break;
                }
                case "chromium": {
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(Config.get("HEADLESS").equals("true")).setSlowMo(1000));
                    break;
                }
                case "firefox": {
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(Config.get("HEADLESS").equals("true")).setSlowMo(1000));
                    break;
                }
            }
            BrowserContext context = browser.newContext();
            context.setDefaultTimeout(Integer.parseInt(Config.get("DEFAULT_TIMEOUT")));
            Page page = context.newPage();
            BasePage basePage = null;
            switch (dataSet.get("storeName").getAsString()) {
                case "Ozon": {
                    basePage = new Ozon(page);
                    break;
                }
                case "DNS": {
                    basePage = new DNS(page);
                    break;
                }
            }
            JsonObject firstElement = basePage.getFirstElement((JsonObject) dataSet);
            basePage.closePage();
            System.out.println("first element:" + firstElement);
            RequestOptions rs = RequestOptions.create()
                    .setData(firstElement)
                    .setQueryParam("token", Config.get("SERVER_TOKEN"));
            APIResponse response = page.request().post(Config.get("SERVER_URL"), rs);
            System.out.println(response.text());
        } finally {
            threadsCount--;
        }

    }
}
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
import java.nio.charset.Charset;

public class Sender extends Thread {
    private static volatile int threadsCount = 0;
    private final JsonObject dataSet;

    public Sender(JsonObject dataSet) {
        this.dataSet = dataSet;
    }

    private static final String dataSetPath = "src/main/resources/dataSet.json";

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
            Thread thread = new Sender(dataSet.getAsJsonObject());
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
        } catch (Throwable e) {
            System.out.println("Something wrong:" + e);
        } finally {
            threadsCount--;
        }

    }
}
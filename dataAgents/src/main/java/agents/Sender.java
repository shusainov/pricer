package agents;

import agents.page.BasePage;
import agents.page.DNS;
import agents.page.Ozon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import utils.Config;

import static utils.Config.getDataSet;

public class Sender extends Thread {
    private static volatile int threadsCount = 0;
    private final JsonObject dataSet;

    public Sender(JsonObject dataSet) {
        this.dataSet = dataSet;
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
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(Config.get("HEADLES").equals("true")).setSlowMo(1000));
                    break;
                }
                case "chromium": {
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(Config.get("HEADLES").equals("true")).setSlowMo(1000));
                    break;
                }
                case "firefox": {
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(Config.get("HEADLES").equals("true")).setSlowMo(1000));
                    break;
                }
            }
            BrowserContext context = browser.newContext();
            context.setDefaultTimeout(Integer.parseInt(Config.get("DEFAULT_TIMEOUT")));
            Page page = context.newPage();
            BasePage basePage =null;
            switch (dataSet.get("storeName").getAsString()){
                case "Ozon":  basePage = new Ozon(page);
                case "DNS" :  basePage = new DNS(page);
            }

            RequestOptions rs = RequestOptions.create()
                    .setData(basePage.getFirstElement((JsonObject) dataSet))
                    .setQueryParam("token", Config.get("SERVER_TOKEN"));
            APIResponse response = page.request().post(Config.get("SERVER_URL"), rs);
            System.out.println(response.text());
        } catch (Throwable e) {
            System.out.println("Something wrong:" + e);
        } finally {
            threadsCount--;
            //guthubtest
        }

    }
}
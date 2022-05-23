import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import utils.Config;

import java.io.FileReader;
import java.io.Reader;

public abstract class BaseTest {
    protected Playwright playwright;
    protected BrowserContext context;
    private static final Logger log = Logger.getLogger(BaseTest.class);

    protected abstract String getConfigPath();
    @DataProvider(parallel = false)
    public Object[][] getJsonElements() {
        JsonArray testSets = new JsonArray();

        try (Reader testDataFile = new FileReader(getConfigPath())) {
            testSets = JsonParser.parseReader(testDataFile).getAsJsonArray();
        } catch (Exception e) {
            log.debug("Cannot load testData file " + e);
        }
        Object[][] returnValue = new Object[testSets.size()][1];
        int index = 0;
        for (Object[] each : returnValue) {
            each[0] = testSets.get(index++);
        }
        return returnValue;
    }

    @BeforeSuite
    public void setupBrowser() {
        playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(1000));
        context = browser.newContext();
        context.setDefaultTimeout(Integer.parseInt(Config.get("DEFAULT_TIMEOUT")));
    }

    @AfterSuite
    public void closeBrowser() {
        playwright.close();
    }
}

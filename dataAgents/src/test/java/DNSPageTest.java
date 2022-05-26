import agents.page.DNS;
import agents.page.Ozon;
import com.google.gson.JsonObject;
import com.microsoft.playwright.Page;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class DNSPageTest extends BaseTest{

    @Override
    protected String getTestSetPath() {
        return "src/test/resources/DNSTestDataArray.json";
    }

    @Test(dataProvider = "getJsonElements")
    public void checkFirstElement(JsonObject testSet) {
        Page page = context.newPage();
        DNS dns = new DNS(page);
        JsonObject item = dns.getFirstElement(testSet);
        assertTrue(item.isJsonObject(), "Does not get JSON Object");
        assertTrue(item.get("price").getAsInt() > 0, "Price is not positive");
    }
}

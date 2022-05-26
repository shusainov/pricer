import agents.page.Ozon;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.*;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Config;

import java.io.FileReader;
import java.io.Reader;

import static org.testng.Assert.assertTrue;

public class OzonPageTest extends BaseTest{

    @Override
    protected String getTestSetPath() {
        return "src/test/resources/ozonTestDataArray.json";
    }

    @Test(dataProvider = "getJsonElements")
    public void checkFirstElement(JsonObject testSet) {
        Page page = context.newPage();
        Ozon ozon = new Ozon(page);
        JsonObject item = ozon.getFirstElement(testSet);
        assertTrue(item.isJsonObject(), "Does not get JSON Object");
        assertTrue(item.get("price").getAsInt() > 0, "Price is not positive");
    }
}

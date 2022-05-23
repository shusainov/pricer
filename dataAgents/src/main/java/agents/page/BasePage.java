package agents.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.Page;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void openStartPage(String URL) {
        page.navigate(URL);
    }

    abstract protected void navigate(JsonObject dataSet);

    abstract public JsonObject getFirstElement(JsonObject dataSet);

    abstract public JsonArray getElements(JsonObject dataSet);

    public void closePage() {
        page.close();
    }
}

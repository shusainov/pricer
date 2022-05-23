package agents.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.Arrays;

public class DNS extends BasePage {
    private static final String pathLocator = "text='%s'";
    private static final String filterNameLocator = "";
    //ui-list-controls ui-collapse ui-collapse_list
    public DNS(Page page) {
        super(page);
    }

    private boolean checkFilterIsOpen(String filterName){
        Locator element = page.locator("i:left-of(.ui-link  :has-text('Графический процессор'))");
        return element.getAttribute("class").contains("upper");
    }

    @Override
    protected void navigate(JsonObject dataSet) {
        openStartPage(dataSet.get("URL").getAsString());
        String[] path = dataSet.get("path").getAsString().split("/");
        Arrays.stream(path).forEach(s->page.click(String.format(pathLocator, s)));
    }

    @Override
    public JsonObject getFirstElement(JsonObject dataSet) {
        navigate(dataSet);
        page.pause();
        return null;
    }

    @Override
    public JsonArray getElements(JsonObject dataSet) {
        return null;
    }
}

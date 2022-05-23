package agents.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.Set;

public class Ozon extends BasePage {
    private static final String pathLocator = "text='%s'";
    private static final String showMoreFilterNameLocator = "span.show:below(:text('%s'))";
    private static final String filterValueLocator = "css=.filter-block >> text=%s";
    private static final String orderLocator = "div[role='option']:has-text(%s)";
    private static final String productRowLocator = ".widget-search-result-container>div>div:nth-child(%d)";
    private static final String priceDivLocator = productRowLocator + ">div:nth-child(3)";

    public Ozon(Page page) {
        super(page);
    }

    private static int removeSymbolsFromPrice(String price) {
        String temp = price.replaceAll("−", "-"); //Ozon использует не минус
        return Integer.parseInt(temp.replaceAll("[^0-9-]", ""));
    }

    private static int getPricesFromRow(ElementHandle element) {
        List<ElementHandle> prices = element.querySelectorAll("text = ₽");
        if (removeSymbolsFromPrice(prices.get(0).innerText()) > 0) {
            return removeSymbolsFromPrice(prices.get(0).innerText());
        } else {
            return removeSymbolsFromPrice(prices.get(1).innerText());
        }
    }

    @Override
    protected void navigate(JsonObject dataSet) {
        openStartPage(dataSet.get("URL").getAsString());

        page.click("button:has-text('Каталог')");
        String[] path = dataSet.get("path").getAsString().split("/");
        for (String s : path) {
            page.click(String.format(pathLocator, s));
        }

        Set<String> filters = dataSet.getAsJsonObject("filter").keySet();
        for (String key : filters) {
            if (page.isVisible(String.format(showMoreFilterNameLocator, key))) {
                page.click(String.format(showMoreFilterNameLocator, key));
            }
            page.click(String.format(filterValueLocator, dataSet.getAsJsonObject("filter").get(key)));
        }

        page.click("input[role='combobox']");
        page.click(String.format(orderLocator, dataSet.get("order")));
    }

    @Override
    public JsonObject getFirstElement(JsonObject dataSet) {
        openStartPage(dataSet.get("URL").getAsString());
        navigate(dataSet);

        ElementHandle productRow = page.querySelector(String.format(productRowLocator, 1));
        ElementHandle pricesDiv = page.querySelector(String.format(priceDivLocator, 1));
        JsonObject element = new JsonObject();
        element.addProperty("name", productRow.querySelector("div>div>a.tile-hover-target").innerText());
        element.addProperty("link", dataSet.get("URL").getAsString() + productRow.querySelector("div>div>a.tile-hover-target").getAttribute("href"));
        element.addProperty("price", getPricesFromRow(pricesDiv));
        element.addProperty("dataSetName", dataSet.get("dataSetName").getAsString());
        element.addProperty("storeName", dataSet.get("storeName").getAsString());

        closePage();
        return element;
    }

    @Override
    public JsonArray getElements(JsonObject dataSet) {
        openStartPage(dataSet.get("URL").getAsString());
        navigate(dataSet);

        List<ElementHandle> names = page.querySelectorAll(".widget-search-result-container>div>div>div>div>a.tile-hover-target");
        JsonArray result = new JsonArray();
        for (int i = 0; i < names.size(); i++) {
            ElementHandle productRow = page.querySelector(String.format(productRowLocator, i + 1));
            ElementHandle pricesDiv = page.querySelector(String.format(priceDivLocator, i + 1));
            JsonObject element = new JsonObject();
            element.addProperty("name", productRow.querySelector("div>div>a.tile-hover-target").innerText());

            element.addProperty("link", dataSet.get("URL").getAsString() + productRow.querySelector("div>div>a.tile-hover-target").getAttribute("href"));
            element.addProperty("price", getPricesFromRow(pricesDiv));
            element.addProperty("dataSetName", dataSet.get("dataSetName").getAsString());
            result.add(element);
        }

        closePage();
        return result;
    }
}

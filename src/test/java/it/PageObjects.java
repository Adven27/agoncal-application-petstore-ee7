package it;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.linkText;

public class PageObjects {
    public static SelenideElement menu(String text) {
        return $(linkText(text));
    }

    public static ElementsCollection table(String css) {
        return $$("." + css + " tr");
    }
}
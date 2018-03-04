package e2e;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.linkText;

@RunWith(JUnit4.class)
public class OrderE2E {

    @BeforeClass
    public static void setUp() throws Exception {
        ChromeDriverManager.getInstance().version("2.35").setup();
        Configuration.browser = "chrome";
    }

    @Test
    public void buyDog() throws Exception {
        open("/applicationPetstore/shopping/main.xhtml");

        $(linkText("Log in")).click();
        $(".at-login").sendKeys("user");
        $(".at-password").sendKeys("user");
        $(Selectors.byValue("Sign In")).pressEnter();

        $(linkText("Dogs")).click();
        $(linkText("Golden Retriever")).click();
        $(linkText("Tailed")).click();
        $(linkText("Add to Cart")).click();
        $(linkText("Check Out")).click();
        $(".at-creditCardNumber").sendKeys("1234");
        $(".at-creditCardExpDate").sendKeys("12");
        $(Selectors.byValue("Submit")).pressEnter();
        final String orderId = $(".at-orderId").text();

        $(linkText("Admin")).click();
        $(linkText("Order")).click();
        $$(".at-orderId").filter(text(orderId)).shouldHaveSize(1);
    }
}

package it;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.agoncal.application.pfm.services.CategoryRepo;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static com.codeborne.selenide.Selenide.open;
import static it.PageObjects.menu;
import static it.PageObjects.table;
import static it.TableConditions.dataEqualTo;
import static org.jboss.arquillian.persistence.TestExecutionPhase.NONE;

@RunWith(Arquillian.class)
@Cleanup(phase = NONE)
public class PfmViewIT extends BaseIT {

    @Deployment
    public static WebArchive createDeployment() {
        return deployment().addClass(CategoryRepo.class);
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ChromeDriverManager.getInstance().version("2.35").setup();
        Configuration.browser = "chrome";
    }

    @Test
    @InSequence(1)
    public void whenNoTransactionsOnBack() {
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void thenFrontShouldDisplayEmptyTable(@ArquillianResource URL url) throws Exception {
        open(url);
        menu("PFM").click();
        table("it-transactions").shouldHaveSize(0);
    }

    @Test
    @InSequence(3)
    @UsingDataSet("datasets/transactions.xml")
    public void whenBackHasTransactions() {
    }

    @Test
    @RunAsClient
    @InSequence(4)
    public void thenFrontShouldDisplayThemInTable() throws Exception {
        menu("PFM").click();
        table("it-transactions").shouldHave(dataEqualTo(
                "FOOD", "1000",
                "DRINK", "200",
                "DRINK", "220"
        ));
    }
}
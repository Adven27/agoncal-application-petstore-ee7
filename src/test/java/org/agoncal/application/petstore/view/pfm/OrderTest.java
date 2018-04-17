package org.agoncal.application.petstore.view.pfm;

import com.codeborne.selenide.Configuration;
import com.google.common.collect.Table;
import e2e.DataSets;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.agoncal.application.petstore.service.TransactionDAO;
import org.agoncal.application.petstore.service.TransactionDAOImpl;
import org.agoncal.application.petstore.view.shopping.Transaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Specializes;
import java.math.BigDecimal;
import java.util.*;

import static com.codeborne.selenide.Selenide.*;
import static e2e.DataSets.data;
import static e2e.MyConditions.dataEqualTo;
import static java.lang.Double.parseDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.By.linkText;

@RunWith(Arquillian.class)
public class OrderTest {
    private static TransactionDAO mockedDAO = mock(TransactionDAO.class);
    private static String[] dataSet = {"CAT1", "10.0", "CAT2", "20.0"};

    @Deployment
    public static WebArchive createDeployment() {
        return ((WebArchive) EmbeddedMaven.forProject("pom.xml")
                .useMaven3Version("3.3.9")
                .setGoals("package")
                .setQuiet()
                .setProfiles("E2E")
                .ignoreFailure()
                .build()
                .getDefaultBuiltArchive()).addClass(DataSets.class);
        // .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml");
        //.addAsResource("persistence.xml", "META-INF/persistence.xml");//replace with test persistence
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ChromeDriverManager.getInstance().version("2.35").setup();
        Configuration.browser = "chrome";
    }

    @Test
    @InSequence(1)
    public void whenNoTransactionsOnBack() {
        when(mockedDAO.getTransactions()).thenReturn(Collections.<Transaction>emptyList());
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void thenFrontShouldDisplayEmptyTable() throws Exception {
        open("/applicationPetstore/index.html");
        $(linkText("PFM")).click();
        $$(".it-transactions tr").shouldHaveSize(0);
    }

    @Test
    @InSequence(3)
    public void whenBackHasTransactions() {
        when(mockedDAO.getTransactions()).thenReturn(transactionsFrom(data(dataSet)));
    }

    @Test
    @RunAsClient
    @InSequence(4)
    public void thenFrontShouldDisplayThemInTable() throws Exception {
        $(linkText("PFM")).click();
        $$(".it-transactions tr").shouldHave(dataEqualTo(dataSet));
    }

    private List<Transaction> transactionsFrom(Table<Integer, String, String> table) {
        List<Transaction> t = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> row : table.rowMap().entrySet()) {
            t.add(new Transaction(
                    BigDecimal.valueOf(parseDouble(row.getValue().get("sum"))),
                    new Date(),
                    row.getValue().get("cat")
            ));
        }
        return t;
    }

    @Specializes
    public static class MockedTransactionDAO extends TransactionDAOImpl {
        @Override
        public List<Transaction> getTransactions() {
            return mockedDAO.getTransactions();
        }
    }
}
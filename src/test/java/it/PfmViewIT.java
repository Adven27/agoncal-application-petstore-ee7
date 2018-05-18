package it;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.agoncal.application.petstore.service.DBTransactionDAO;
import org.agoncal.application.petstore.service.TransactionDAO;
import org.agoncal.application.petstore.view.shopping.Transaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Specializes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static it.DataSets.data;
import static it.PageObjects.menu;
import static it.PageObjects.table;
import static it.TableConditions.dataEqualTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Arquillian.class)
public class PfmViewIT extends BaseIT {
    private static TransactionDAO mockedDAO = mock(TransactionDAO.class);
    private static String[] expected = {"CAT1", "10.0", "CAT2", "20.0"};

    @Deployment
    public static WebArchive createDeployment() {
        return deployment();
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ChromeDriverManager.getInstance().version("2.35").setup();
        Configuration.browser = "chrome";
    }

    @Test
    @InSequence(1)
    public void whenNoTransactionsOnBack() {
        when(mockedDAO.findAll()).thenReturn(Collections.<Transaction>emptyList());
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void thenFrontShouldDisplayEmptyTable() throws Exception {
        open("/applicationPetstore/index.html");
        menu("PFM").click();
        table("it-transactions").shouldHaveSize(0);
    }

    @Test
    @InSequence(3)
    public void whenBackHasTransactions() {
        when(mockedDAO.findAll()).thenReturn(transactionsFrom(expected));
    }

    @Test
    @RunAsClient
    @InSequence(4)
    public void thenFrontShouldDisplayThemInTable() throws Exception {
        menu("PFM").click();
        table("it-transactions").shouldHave(dataEqualTo(expected));
    }

    private List<Transaction> transactionsFrom(String... dataSet) {
        List<Transaction> t = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, String>> row : data(dataSet).rowMap().entrySet()) {
            t.add(new Transaction());
        }
        return t;
    }

    @Specializes
    public static class MockedTransactionDAO extends DBTransactionDAO {
        public MockedTransactionDAO() {
            super(null);
        }

        @Override
        public List<Transaction> findAll() {
            return mockedDAO.findAll();
        }
    }
}
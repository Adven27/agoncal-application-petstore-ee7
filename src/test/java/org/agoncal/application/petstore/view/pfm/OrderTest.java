package org.agoncal.application.petstore.view.pfm;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.agoncal.application.petstore.view.shopping.Transaction;
import org.agoncal.application.petstore.view.shopping.TransactionDAO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Produces;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.By.linkText;

@RunWith(Arquillian.class)
public class OrderTest {

    private static TransactionDAO transactionDAO = mock(TransactionDAO.class);

    @Deployment
    public static WebArchive createDeployment() {
        return ((WebArchive) EmbeddedMaven.forProject("pom.xml")
                .useMaven3Version("3.3.9")
                .setGoals("package")
                .setQuiet()
                .setProfiles("E2E")
                .ignoreFailure()
                .build()
                .getDefaultBuiltArchive());
        // .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml");
        //.addAsResource("persistence.xml", "META-INF/persistence.xml");//replace with test persistence
    }

    @BeforeClass
    public static void setUp() throws Exception {
        ChromeDriverManager.getInstance().version("2.35").setup();
        Configuration.browser = "chrome";
    }

    @Produces
    TransactionDAO transactionDAO() {
        return transactionDAO;
    }

    @Test
    @InSequence(1)
    public void should_be_deployed() {
        when(transactionDAO.getTransactions()).thenReturn(
                asList(new Transaction(TEN, new Date(), "CAT"))
        );
    }

    @Test
    @RunAsClient
    @InSequence(2)
    public void start() throws Exception {
        open("/applicationPetstore/index.html");
        $(linkText("PFM")).click();
        shouldHaveListOfTransactions();
    }

    //TODO
    private void shouldHaveListOfTransactions() {
        //find table
        //check data
        //fail("Unimplemented");
    }

    @Test
    @InSequence(3)
    public void zero() {
        when(transactionDAO.getTransactions()).thenReturn(
                asList(
                        new Transaction(TEN, new Date(), "CAT"),
                        new Transaction(ZERO, new Date(), "!!!!!!!!!!!!!!!")
                )
        );
    }

    @Test
    @RunAsClient
    @InSequence(4)
    public void checkZero() throws Exception {
        $(linkText("PFM")).click();
        shouldHaveListOfTransactions();
    }
/*
    @Test
    @InSequence(2)
    public void should_be_deployed() {
        Assert.assertNotNull(purchaseorderbean);
    }

    @Test
    @RunAsClient
    @InSequence(3)
    public void buyDog() throws Exception {
        $(linkText("Dogs")).click();
    }

    @Test
    @InSequence(4)
    public void should_paginate() {
        // Creates an empty example
        PurchaseOrder example = new PurchaseOrder();

        // Paginates through the example
        purchaseorderbean.setExample(example);
        purchaseorderbean.paginate();
        assertTrue((purchaseorderbean.getPageItems().size() == purchaseorderbean.getPageSize()) || (purchaseorderbean.getPageItems().size() == purchaseorderbean.getCount()));
    }*/
}
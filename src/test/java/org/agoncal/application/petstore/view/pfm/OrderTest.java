package org.agoncal.application.petstore.view.pfm;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.agoncal.application.petstore.model.PurchaseOrder;
import org.agoncal.application.petstore.view.admin.PurchaseOrderBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.embedded.EmbeddedMaven;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.linkText;

@RunWith(Arquillian.class)
public class OrderTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    private static String context;

    @Inject
    private PurchaseOrderBean purchaseorderbean;

    @Deployment
    public static WebArchive createDeployment() {
        return ((WebArchive) EmbeddedMaven.forProject("pom.xml")
                .useMaven3Version("3.3.9")
                .setGoals("package")
                .setQuiet()
                .skipTests(true)
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

    @Test
    @RunAsClient
    @InSequence(1)
    public void start() throws Exception {
        open("/applicationPetstore/shopping/main.xhtml");
    }

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
    }
}
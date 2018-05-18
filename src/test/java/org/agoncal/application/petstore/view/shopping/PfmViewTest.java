package org.agoncal.application.petstore.view.shopping;

import com.google.common.collect.ImmutableMap;
import org.agoncal.application.petstore.service.TransactionDAO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.primefaces.model.chart.DonutChartModel;

import java.util.Date;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PfmViewTest {
    private static final String FOOD = "cat1";
    private static final String DRINK = "cat2";
    private static final Transaction FOOD_1 = new Transaction(1L, TEN, new Date(), FOOD);
    private static final Transaction FOOD_2 = new Transaction(2L, ONE, new Date(), FOOD);
    private static final Transaction DRINK_1 = new Transaction(3L, TEN, new Date(), DRINK);
    @Mock private TransactionDAO dao;
    private PfmView sut;

    @Test
    public void noTransactions() throws Exception {
        sut = new PfmView(dao);

        verify(dao).findAll();
        assertEquals(0, sut.getTransactions().size());
        assertDonutHas(sut.getDonut(), "Empty", 1);
    }

    @Test
    public void severalTransactionsInOneCategory() throws Exception {
        when(dao.findAll()).thenReturn(asList(FOOD_1, FOOD_2));
        sut = new PfmView(dao);

        verify(dao).findAll();
        assertEquals(2, sut.getTransactions().size());
        assertDonutHas(sut.getDonut(), FOOD, FOOD_1.getAmount().doubleValue() + FOOD_2.getAmount().doubleValue());
    }

    @Test
    public void severalTransactionsInSeveralCategories() throws Exception {
        when(dao.findAll()).thenReturn(asList(FOOD_1, FOOD_2, DRINK_1));
        sut = new PfmView(dao);

        verify(dao).findAll();
        assertEquals(3, sut.getTransactions().size());
        assertDonutHas(sut.getDonut(),
                FOOD, FOOD_1.getAmount().doubleValue() + FOOD_2.getAmount().doubleValue(),
                DRINK, DRINK_1.getAmount().doubleValue());
    }

    @Test
    @Ignore
    public void itemSelect() throws Exception {
    }

    private void assertDonutHas(DonutChartModel donut, String cat, Number sum) {
        Map<String, Number> expected = ImmutableMap.of(cat, sum);
        assertThat(donut.getData().get(0), is(expected));
    }

    private void assertDonutHas(DonutChartModel donut, String cat1, Number sum1, String cat2, Number sum2) {
        Map<String, Number> expected = ImmutableMap.of(cat1, sum1, cat2, sum2);
        assertThat(donut.getData().get(0), is(expected));
    }
}
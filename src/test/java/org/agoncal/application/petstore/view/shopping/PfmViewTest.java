package org.agoncal.application.petstore.view.shopping;

import com.google.common.collect.ImmutableMap;
import org.agoncal.application.pfm.model.Transaction;
import org.agoncal.application.pfm.services.PFMService;
import org.agoncal.application.pfm.view.PfmView;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PfmViewTest {
    private static final String FOOD = "cat1";
    private static final String DRINK = "cat2";
    private static final Transaction FOOD_1 = new Transaction(1L, TEN, new Date(), FOOD);
    private static final Transaction FOOD_2 = new Transaction(2L, ONE, new Date(), FOOD);
    private static final Transaction DRINK_1 = new Transaction(3L, TEN, new Date(), DRINK);
    @Mock private PFMService pfm;
    private PfmView sut;

    private static void assertDonutHas(DonutChartModel actual, String cat, Number sum) {
        Map<String, Number> expected = ImmutableMap.of(cat, sum);
        assertThat(actual.getData().get(0), is(expected));
    }

    private static void assertDonutHas(DonutChartModel actual, String cat1, Number sum1, String cat2, Number sum2) {
        Map<String, Number> expected = ImmutableMap.of(cat1, sum1, cat2, sum2);
        assertThat(actual.getData().get(0), is(expected));
    }

    @Test
    public void shouldRetrieveTransactionsAtInit() {
        sut = new PfmView(pfm);
        verify(pfm).transactions();
        verify(pfm).categories();
        verifyNoMoreInteractions(pfm);
    }

    @Test
    public void noTransactions() {
        sut = new PfmView(pfm);

        assertEquals(0, sut.getTransactions().size());
        assertDonutHas(sut.getDonut(), "Empty", 1);
    }

    @Test
    public void severalTransactionsInOneCategory() {
        when(pfm.transactions()).thenReturn(asList(FOOD_1, FOOD_2));
        sut = new PfmView(pfm);

        assertEquals(2, sut.getTransactions().size());
        assertDonutHas(
                sut.getDonut(),
                FOOD, FOOD_1.getAmount().add(FOOD_2.getAmount()).doubleValue()
        );
    }

    @Test
    public void severalTransactionsInSeveralCategories() {
        when(pfm.transactions()).thenReturn(asList(FOOD_1, FOOD_2, DRINK_1));
        sut = new PfmView(pfm);

        assertEquals(3, sut.getTransactions().size());
        assertDonutHas(
                sut.getDonut(),
                FOOD, FOOD_1.getAmount().add(FOOD_2.getAmount()).doubleValue(),
                DRINK, DRINK_1.getAmount().doubleValue()
        );
    }
}
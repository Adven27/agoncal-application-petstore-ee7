package org.agoncal.application.petstore.view.shopping;

import com.google.common.collect.ImmutableMap;
import org.agoncal.application.pfm.ClientCardInfo;
import org.agoncal.application.pfm.Currency;
import org.agoncal.application.pfm.Money;
import org.agoncal.application.pfm.Operation;
import org.agoncal.application.pfm.services.PFMService;
import org.agoncal.application.pfm.view.PfmView;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.primefaces.model.chart.DonutChartModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PfmViewTest {
    private static final String FOOD = "cat1";
    private static final String DRINK = "cat2";
    private static final Operation FOOD_1 = mock(Operation.class);
    private static final Operation FOOD_2 = mock(Operation.class);
    private static final Operation DRINK_1 = mock(Operation.class);
    private static final ClientCardInfo CARD_1 = mock(ClientCardInfo.class);
    private static final ClientCardInfo CARD_2 = mock(ClientCardInfo.class);
    private static final Currency ANY_CURRENCY = mock(Currency.class);
    private static final PFMService PFM = mock(PFMService.class);
    private PfmView sut;

    @BeforeClass
    public static void setUpMocks() {
        when(FOOD_1.getAmount()).thenReturn(new Money.Simple(TEN, ANY_CURRENCY));
        when(FOOD_1.getDatetime()).thenReturn(new Date());
        when(FOOD_1.getMcc()).thenReturn(100);

        when(FOOD_2.getAmount()).thenReturn(new Money.Simple(ONE, ANY_CURRENCY));
        when(FOOD_2.getDatetime()).thenReturn(new Date());
        when(FOOD_2.getMcc()).thenReturn(100);

        when(DRINK_1.getAmount()).thenReturn(new Money.Simple(TEN, ANY_CURRENCY));
        when(DRINK_1.getDatetime()).thenReturn(new Date());
        when(DRINK_1.getMcc()).thenReturn(200);

        when(CARD_1.getIdentifier()).thenReturn("card_1_id");
        when(CARD_2.getIdentifier()).thenReturn("card_2_id");
    }

    @Before
    public void setUpSUT() {
        when(PFM.categoryOf(FOOD_1.getMcc())).thenReturn(FOOD);
        when(PFM.categoryOf(FOOD_2.getMcc())).thenReturn(FOOD);
        when(PFM.categoryOf(DRINK_1.getMcc())).thenReturn(DRINK);
        sut = new PfmView(PFM);
    }

    @After
    public void tearDown() {
        reset(PFM);
    }

    @Test
    public void emptyDonutIfNoProducts() {
        sut.postConstruct();

        assertEquals(0, sut.getProductOperations().size());
        assertDonutHas(sut.getDonut(), "Empty", 1);
    }

    @Test
    public void emptyDonutIfNoProductOperations() {
        when(PFM.cards()).thenReturn(asList(CARD_1));
        sut.postConstruct();

        assertEquals(0, sut.getProductOperations().size());
        assertDonutHas(sut.getDonut(), "Empty", 1);
    }

    @Test
    public void shouldLoadProductsAndOperationsForFirstProductOnInit() {
        when(PFM.cards()).thenReturn(asList(CARD_1, CARD_2));
        sut.postConstruct();

        verify(PFM).cards();
        verify(PFM).operations(eq(CARD_1.getIdentifier()), any(Date.class), any(Date.class));
        verifyNoMoreInteractions(PFM);
    }

    @Test
    public void severalOperationsInOneCategory() {
        when(PFM.cards()).thenReturn(asList(CARD_1));
        when(PFM.operations(eq(CARD_1.getIdentifier()), any(Date.class), any(Date.class))).then(new Answer<List<? extends Operation>>() {
            @Override
            public List<? extends Operation> answer(InvocationOnMock invocation) {
                return asList(FOOD_1, FOOD_2);
            }
        });
        sut.postConstruct();

        assertEquals(2, sut.getProductOperations().size());
        assertDonutHas(
                sut.getDonut(),
                FOOD, sum(FOOD_1, FOOD_2)
        );
    }

    @Test
    public void severalOperationsInSeveralCategories() {
        when(PFM.cards()).thenReturn(asList(CARD_1));
        when(PFM.operations(eq(CARD_1.getIdentifier()), any(Date.class), any(Date.class))).then(new Answer<List<? extends Operation>>() {
            @Override
            public List<? extends Operation> answer(InvocationOnMock invocation) {
                return asList(FOOD_1, FOOD_2, DRINK_1);
            }
        });
        sut.postConstruct();

        assertEquals(3, sut.getProductOperations().size());
        assertDonutHas(
                sut.getDonut(),
                FOOD, sum(FOOD_1, FOOD_2),
                DRINK, sum(DRINK_1)
        );
    }

    private double sum(Operation... ops) {
        double result = 0;
        for (Operation op : ops) {
            result += op.getAmount().getAmount().doubleValue();
        }
        return result;
    }

    private static void assertDonutHas(DonutChartModel actual, String cat, Number sum) {
        Map<String, Number> expected = ImmutableMap.of(cat, sum);
        assertThat(actual.getData().get(0), is(expected));
    }

    private static void assertDonutHas(DonutChartModel actual, String cat1, Number sum1, String cat2, Number sum2) {
        Map<String, Number> expected = ImmutableMap.of(cat1, sum1, cat2, sum2);
        assertThat(actual.getData().get(0), is(expected));
    }
}
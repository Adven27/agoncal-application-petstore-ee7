package org.agoncal.application.pfm.view;

import com.google.common.base.Joiner;
import lombok.*;
import org.agoncal.application.pfm.ClientCardInfo;
import org.agoncal.application.pfm.services.PFMService;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.DonutChartModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Arrays.asList;
import static org.joda.time.LocalDate.now;

@Named
@ViewScoped
public class PfmView implements Serializable {
    private final Map<String, List<Operation>> productOperations = new LinkedHashMap<>();
    private final Map<String, String> productCurrency = new HashMap<>();
    private final PFMService pfmService;
    @Getter
    private WithTotal donut;
    private String selectedCategory;

    @Getter
    @Setter
    private Filter filter;

    @Inject
    public PfmView(PFMService pfmService) {
        this.pfmService = pfmService;
    }

    @PostConstruct
    public void postConstruct() {
        for (ClientCardInfo c : pfmService.cards()) {
            productOperations.put(c.getIdentifier(), null);
            productCurrency.put(c.getIdentifier(), "rub");
        }
        filter = initFilter(productOperations);

        initDonut(filter);
    }

    private void initDonut(Filter filter) {
        LinkedHashMap<String, Number> cat2Sum = new LinkedHashMap<>();
        if (!isNullOrEmpty(filter.getProduct())) {
            String p = filter.getProduct();
            List<Operation> operations = map(pfmService.operations(p, filter.getFrom(), filter.getTo()));
            productOperations.put(p, operations);
            cat2Sum.putAll(getExpenseMap(operations));
        }
        donut = new WithTotal(cat2Sum, productCurrency.get(filter.getProduct()));
    }

    private Filter initFilter(Map<String, List<Operation>> productOperations) {
        return new Filter(
                productOperations.isEmpty()
                        ? null
                        : productOperations.keySet().iterator().next(),
                now().withDayOfMonth(1).toDate(),
                now().plusDays(1).toDate()
        );
    }

    private List<Operation> map(List<? extends org.agoncal.application.pfm.Operation> operations) {
        List<Operation> result = new ArrayList<>();
        for (org.agoncal.application.pfm.Operation op : operations) {
            result.add(
                    new Operation(
                            op.getAmount().getAmount(),
                            op.getAmount().getCurrency().getName(),
                            op.getDatetime(),
                            pfmService.categoryOf(op.getMcc())
                    )
            );
        }
        return result;
    }

    public List<Operation> getProductOperations() {
        return filter.getProduct() == null
                ? Collections.<Operation>emptyList()
                : selectedCategory == null
                        ? productOperations.get(filter.getProduct())
                        : filter(productOperations.get(filter.getProduct()), selectedCategory);
    }

    public void itemSelect(ItemSelectEvent e) {
        selectedCategory = selectCategoryBy(e.getItemIndex(), donut.getData().get(0));
    }

    public void changeProduct() {
        initDonut(filter);
    }

    private String selectCategoryBy(int index, Map<String, Number> circle) {
        int i = 0;
        for (Map.Entry<String, Number> entry : circle.entrySet()) {
            if (i < index) {
                i++;
                continue;
            }
            return entry.getKey();
        }
        return null;
    }

    private List<Operation> filter(List<Operation> transactions, String selectedCategory) {
        List<Operation> result = new ArrayList<>();
        for (Operation ov : transactions) {
            if (ov.getCategory().equals(selectedCategory)) {
                result.add(ov);
            }
        }
        return result;
    }

    private LinkedHashMap<String, Number> getExpenseMap(List<Operation> operations) {
        LinkedHashMap<String, Number> result = new LinkedHashMap<>();
        for (Operation op : operations) {
            Number old = result.get(op.getCategory());
            BigDecimal amount = op.getAmount() == null ? BigDecimal.ZERO : op.getAmount();
            result.put(op.getCategory(), old == null
                    ? amount.doubleValue()
                    : amount.doubleValue() + old.doubleValue()
            );
        }
        return result;
    }

    public Set<String> getProducts() {
        return productOperations.keySet();
    }

    @Data
    @AllArgsConstructor
    public static class Operation {
        private BigDecimal amount;
        private String currency;
        private Date dt;
        private String category;
    }

    @Data
    @AllArgsConstructor
    public static class Filter {
        private String product;
        private Date from;
        private Date to;
    }

    @Data
    @EqualsAndHashCode(exclude = "currency")
    @AllArgsConstructor
    public static class Product {
        private String id;
        private String currency;

        public String toString() {
            return id;
        }
    }

    public class WithTotal extends DonutChartModel {
        @Getter private final String currency;
        @Getter private final List<LegendRow> categoryBreakdown;
        private final Double total;
        private final List<String> colors = asList(
                "04ceff", "fa472f", "2d9200", "f7e926", "c747a3", "4b5de4", "ff8e02", "37a971",
                "bd70c7", "62ff05", "ff5800", "0085cc", "953579", "cddf54", "FBD178", "94bae3");

        public WithTotal(Map<String, Number> data, String currency) {
            super();
            this.currency = currency;
            total = total(data);
            categoryBreakdown = breakDown(data);
            setExtender("extLegend");
            setSeriesColors(Joiner.on(",").join(colors));
            addCircle(decorateEmpty(data));
        }

        public String getTotal() {
            List<String> strings = asList("1", "1000", "39 600", "300 300.10", "4 000 300.0", "50 000 333.12");
            return strings.get(new Random().nextInt(5));
        }

        private Double total(Map<String, Number> data) {
            double result = 0d;
            for (Number sum : data.values()) {
                result += sum.doubleValue();
            }
            return result;
        }

        private List<LegendRow> breakDown(Map<String, Number> data) {
            List<LegendRow> result = new ArrayList<>();

            int i = 0;
            for (Map.Entry<String, Number> e : data.entrySet()) {
                result.add(new LegendRow("#" + colors.get(i++), e.getKey(), e.getValue(), Math.round(e.getValue().doubleValue() / total * 100)));
            }
            Collections.sort(result, new Comparator<LegendRow>() {
                @Override public int compare(LegendRow o1, LegendRow o2) {
                    return Double.compare(o2.getSum().doubleValue(), o1.getSum().doubleValue());
                }
            });
            return result;
        }

        private Map<String, Number> decorateEmpty(Map<String, Number> expenseMap) {
            if (expenseMap.isEmpty()) {
                expenseMap.put("Empty", 1);
            }
            return expenseMap;
        }

        private <K, V extends Comparable<? super V>> Map<K, V> sort(final Map<K, V> toSort) {
            List<Map.Entry<K, V>> entries = new ArrayList<>(toSort.size());
            entries.addAll(toSort.entrySet());

            Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
                @Override
                public int compare(final Map.Entry<K, V> e1, final Map.Entry<K, V> e2) {
                    return e2.getValue().compareTo(e1.getValue());
                }
            });

            Map<K, V> sorted = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : entries) {
                sorted.put(entry.getKey(), entry.getValue());
            }
            return sorted;
        }

        @Data @AllArgsConstructor public class LegendRow {
            String color;
            String label;
            Number sum;
            long percent;
        }
    }
}
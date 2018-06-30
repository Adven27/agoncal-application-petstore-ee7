package org.agoncal.application.pfm.view;

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
        @Getter private final Double total;
        @Getter private final Map<String, Long> categoryBreakdown;

        public WithTotal(Map<String, Number> data, String currency) {
            super();
            this.currency = currency;
            total = total(data);
            categoryBreakdown = breakDown(data);
            setExtender("extLegend");
            setDataFormat("value");
            addCircle(decorateEmpty(data));
        }

        private Double total(Map<String, Number> data) {
            double result = 0d;
            for (Number sum : data.values()) {
                result += sum.doubleValue();
            }
            return result;
        }

        private Map<String, Long> breakDown(Map<String, Number> data) {
            Map<String, Long> result = new HashMap<>();
            for (Map.Entry<String, Number> e : data.entrySet()) {
                result.put(e.getKey(), Math.round(e.getValue().doubleValue() / getTotal() * 100));
            }
            return sort(result);
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

    }
}
package org.agoncal.application.pfm.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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

@Named
@ViewScoped
public class PfmView implements Serializable {
    private final Map<String, List<Operation>> productOperations = new LinkedHashMap<>();
    private final PFMService pfmService;
    @Getter
    private DonutChartModel donut = new DonutChartModel();
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
        donut = initChart(cat2Sum);
    }

    private Filter initFilter(Map<String, List<Operation>> productOperations) {
        return new Filter(
                productOperations.isEmpty()
                        ? null
                        : productOperations.keySet().iterator().next(),
                new Date(),
                new Date()
        );
    }

    private List<Operation> map(List<? extends org.agoncal.application.pfm.Operation> operations) {
        List<Operation> result = new ArrayList<>();
        for (org.agoncal.application.pfm.Operation op : operations) {
            result.add(
                    new Operation(
                            op.getAmount().getAmount(),
                            op.getDatetime(),
                            pfmService.categoryOf(op.getMcc())
                    )
            );
        }
        return result;
    }

    public List<Operation> getProductOperations() {
        return isNullOrEmpty(filter.getProduct())
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

    private DonutChartModel initChart(Map<String, Number> circle) {
        DonutChartModel d = new DonutChartModel();
        d.addCircle(decorateEmpty(circle));
        d.setLegendPosition("e");
        d.setSliceMargin(3);
        d.setShowDataLabels(true);
        d.setExtender("extLegend");
        d.setDataFormat("value");
        d.setShadow(true);
        return d;
    }

    private Map<String, Number> decorateEmpty(Map<String, Number> expenseMap) {
        if (expenseMap.isEmpty()) {
            expenseMap.put("Empty", 1);
        }
        return expenseMap;
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
}
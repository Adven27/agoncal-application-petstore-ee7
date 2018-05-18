package org.agoncal.application.petstore.view.shopping;

import lombok.Getter;
import org.agoncal.application.petstore.service.TransactionDAO;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.CatchException;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.DonutChartModel;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Loggable
@CatchException
@ViewScoped
@Named
public class PfmView implements Serializable {
    private final List<Transaction> transactions;
    @Getter private final DonutChartModel donut;
    private final LinkedHashMap<String, Number> cat2Sum;
    private String selectedCategory;

    @Inject
    public PfmView(TransactionDAO transactionDAO) {
        transactions = transactionDAO.getTransactions();
        cat2Sum = getExpenseMap(transactions);
        donut = initChart(cat2Sum);
    }

    public List<Transaction> getTransactions() {
        return selectedCategory == null ? transactions : filter(transactions, selectedCategory);
    }

    public void itemSelect(ItemSelectEvent e) {
        selectedCategory = selectCategoryBy(e.getItemIndex(), cat2Sum);
    }

    private String selectCategoryBy(int index, LinkedHashMap<String, Number> circle) {
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

    private List<Transaction> filter(List<Transaction> transactions, String selectedCategory) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getCategory().equals(selectedCategory)) {
                result.add(t);
            }
        }
        return result;
    }

    private DonutChartModel initChart(Map<String, Number> circle) {
        DonutChartModel pie = new DonutChartModel();
        pie.addCircle(customizeExpenseMap(circle));
        pie.setLegendPosition("e");
        pie.setSliceMargin(3);
        pie.setShowDataLabels(true);
        pie.setExtender("extLegend");
        pie.setDataFormat("value");
//        donut.setDataLabelFormatString("%d%%");
        pie.setShadow(true);
        return pie;
    }

    private LinkedHashMap<String, Number> getExpenseMap(List<Transaction> transactions) {
        LinkedHashMap<String, Number> result = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            Number old = result.get(t.getCategory());
            result.put(t.getCategory(), old == null
                    ? t.getSum().doubleValue()
                    : t.getSum().doubleValue() + old.doubleValue()
            );
        }
        return result;
    }

    private Map<String, Number> customizeExpenseMap(Map<String, Number> expenseMap) {
        if (expenseMap.isEmpty()) {
            expenseMap.put("Empty", 1);
        }
        return expenseMap;
    }
}
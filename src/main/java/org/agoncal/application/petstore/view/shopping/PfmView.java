package org.agoncal.application.petstore.view.shopping;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.agoncal.application.petstore.service.TransactionDAO;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.CatchException;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.DonutChartModel;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Loggable
@CatchException
public class PfmView implements Serializable {
    @Getter private final List<Transaction> transactions;
    @Inject
    Event<ItemSelectEvent> eventService;
    private DonutChartModel pie;

    @Inject
    public PfmView(TransactionDAO transactionDAO) {
        transactions = transactionDAO.getTransactions();
    }

    public DonutChartModel getPie() {
        calculateExpenseGraphModel();
        return pie;
    }

    public void itemSelect(ItemSelectEvent e) {
        //eventService.fire(e);
        showInfo(e);
    }

    public void showInfo(@Observes ItemSelectEvent e) {
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_WARN,
                "Item selected",
                e.getItemIndex() + " " + e.getSeriesIndex()
        );
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void calculateExpenseGraphModel() {
        try {
            pie = new DonutChartModel();
            pie.addCircle(customizeExpenseMap(getExpenseMap()));
            pie.setLegendPosition("e");
            pie.setSliceMargin(3);
            pie.setShowDataLabels(true);
            pie.setDataFormat("value");
            pie.setShadow(true);
        } catch (UnsupportedEncodingException e) {
        }
    }

    private Map<String, Number> getExpenseMap() {
        return ImmutableMap.<String, Number>of("cat 1", 1, "cat 2", 2);
    }

    private Map<String, Number> customizeExpenseMap(Map<String, Number> expenseMap) throws UnsupportedEncodingException {
        if (expenseMap.isEmpty()) {
            expenseMap.put("Empty", 1);
        }
        return expenseMap;
    }
}
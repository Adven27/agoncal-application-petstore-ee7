package org.agoncal.application.petstore.view;

import org.primefaces.event.ItemSelectEvent;

import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ShowMessageListener {
    public void showInfo(@Observes ItemSelectEvent e) {
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Item selected",
                e.getItemIndex() + " " + e.getSeriesIndex()
        );
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
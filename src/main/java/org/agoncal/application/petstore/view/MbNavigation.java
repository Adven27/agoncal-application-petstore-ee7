package org.agoncal.application.petstore.view;

import org.primefaces.model.menu.*;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class MbNavigation implements Serializable {
    private static final long serialVersionUID = 1L;

    public MenuModel getMainMenuModel() {
        MenuModel model = new DefaultMenuModel();
        DefaultMenuItem element = new DefaultMenuItem();
        element.setValue("PFM");
        element.setOutcome("/pages/pfm.xhtml");
        model.addElement(element);
        return model;
    }
}
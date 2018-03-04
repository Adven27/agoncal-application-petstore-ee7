package org.agoncal.application.petstore.view.shopping;

import lombok.Getter;
import lombok.Setter;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.service.CatalogService;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.CatchException;

import javax.enterprise.inject.Model;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Model
@Loggable
@CatchException
public class ItemView implements Serializable {
    @Inject CartView shoppingCart;
    @Inject AccountBean accountService;
    @Getter @Setter private Item item;

    public String addToCart() {
        return shoppingCart.addItemToCart();
    }

    public boolean isCanAdd() {
        return accountService.isLoggedIn();
    }

    @Named
    public static class ToItem implements Converter {
        @Inject private CatalogService catalogService;

        @Override
        public Item getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
            return catalogService.findItem(Long.valueOf(s));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
            return ((Item) o).getId().toString();
        }
    }
}
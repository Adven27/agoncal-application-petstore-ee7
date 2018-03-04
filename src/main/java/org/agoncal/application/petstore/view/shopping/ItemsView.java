package org.agoncal.application.petstore.view.shopping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.Product;
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
import java.util.List;

@Model
@Loggable
@CatchException
public class ItemsView implements Serializable {
    @Setter @Getter private Items items;

    @AllArgsConstructor
    public static class Items implements List<Item> {
        @Delegate private final List<Item> delegate;
        @Getter private final Product product;

        public Items(CatalogService catalogService, long productId) {
            product = catalogService.findProduct(productId);
            delegate = catalogService.findItems(productId);
        }
    }

    @Named
    public static class FromProduct implements Converter {
        @Inject private CatalogService catalogService;

        @Override
        public Items getAsObject(FacesContext facesContext, UIComponent uiComponent, String productId) {
            return new Items(catalogService, Long.valueOf(productId));
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
            return ((Items) o).getProduct().getId().toString();
        }
    }
}
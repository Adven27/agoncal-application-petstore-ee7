package org.agoncal.application.petstore.view.shopping;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
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
public class ProductsView implements Serializable {
    @Setter @Getter private Products products;

    public static class Products implements List<Product> {
        @Getter private final String category;
        @Delegate List<Product> delegate;

        public Products(CatalogService catalogService, String category) {
            this.category = category;
            delegate = catalogService.findProducts(category);
        }
    }

    @Named
    public static class ProductsConverter implements Converter {
        @Inject private CatalogService catalogService;

        @Override
        public ProductsView.Products getAsObject(FacesContext facesContext, UIComponent uiComponent, String category) {
            return new ProductsView.Products(catalogService, category);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
            return o == null ? null : ((ProductsView.Products) o).getCategory();
        }
    }
}
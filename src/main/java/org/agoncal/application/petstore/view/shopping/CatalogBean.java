package org.agoncal.application.petstore.view.shopping;

import lombok.Getter;
import lombok.Setter;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.AbstractBean;
import org.agoncal.application.petstore.view.CatchException;

import javax.inject.Named;
import java.io.Serializable;

@Named
@Loggable
@CatchException
public class CatalogBean extends AbstractBean implements Serializable {
    @Getter @Setter private String keyword;

    /**
     * Can also be invoked in a RESTful way :
     * http://localhost:8080/applicationPetstore/searchresult.xhtml?keyword=tail
     */
    public String doSearch() {
        //items = new Items(catalogService.searchItems(keyword));
//        return "searchresult";
        return "searchresult.faces&faces-redirect=true";
//        return "searchresult.faces?keyword=" + keyword + "&faces-redirect=true";
    }
}
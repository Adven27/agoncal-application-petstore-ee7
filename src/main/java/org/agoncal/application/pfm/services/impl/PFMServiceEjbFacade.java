package org.agoncal.application.pfm.services.impl;

import lombok.NoArgsConstructor;
import org.agoncal.application.pfm.Origin;
import org.agoncal.application.pfm.model.ClientCardInfo;
import org.agoncal.application.pfm.model.Operation;
import org.agoncal.application.pfm.services.PFMService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Stateless
public class PFMServiceEjbFacade implements PFMService {
    @Inject @Origin
    private PFMService original;

    @Override
    public List<? extends Operation> operations(String productId, Date from, Date to) {
        return original.operations(productId, from, to);
    }

    @Override
    public List<String> categories() {
        return original.categories();
    }

    @Override
    public List<ClientCardInfo> cards() {
        return original.cards();
    }

    @Override
    public String categoryOf(Integer mcc) {
        return original.categoryOf(mcc);
    }
}

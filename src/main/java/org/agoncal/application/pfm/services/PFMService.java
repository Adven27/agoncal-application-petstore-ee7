package org.agoncal.application.pfm.services;

import org.agoncal.application.pfm.model.ClientCardInfo;
import org.agoncal.application.pfm.model.Operation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface PFMService extends Serializable {
    List<? extends Operation> operations(String productId, Date from, Date to);

    List<String> categories();

    List<ClientCardInfo> cards();

    String categoryOf(Integer mcc);
}
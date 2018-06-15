package org.agoncal.application.pfm.model;

import com.sun.istack.internal.Nullable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@Data
public class ClientCardInfo {

    private static final long serialVersionUID = 1L;

    public ClientCardInfo(String identifier) {
        this.identifier = identifier;
        embossedName = null;
        number = null;
        expiryDate = null;
        availableBalance = null;
        currency = null;
    }

    private final String identifier;
    private final String embossedName;
    private final String number;
    private final @Nullable
    Date expiryDate;
    private @Nullable
    String name;
    private final @Nullable
    BigDecimal availableBalance;
    private final @Nullable
    Currency currency;
}
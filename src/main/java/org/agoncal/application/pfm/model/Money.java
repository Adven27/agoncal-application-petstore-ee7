package org.agoncal.application.pfm.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

public interface Money extends Serializable {

    BigDecimal getAmount();

    Currency getCurrency();

    @Data
    class Simple implements Money {
        private final BigDecimal amount;
        private final Currency currency;
    }
}

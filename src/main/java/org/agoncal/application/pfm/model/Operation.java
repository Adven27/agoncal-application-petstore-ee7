package org.agoncal.application.pfm.model;

import lombok.Data;

import java.util.Date;

public interface Operation {
    Date getDatetime();
    Money getAmount();
    Integer getMcc();

    @Data
    class Simple implements Operation{
        private final Money amount;
        private final Integer mcc;
        private final Date datetime;
    }
}
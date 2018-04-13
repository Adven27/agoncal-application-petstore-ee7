package org.agoncal.application.petstore.view.shopping;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {
    private final BigDecimal sum;
    private final Date date;
    private final String category;
}
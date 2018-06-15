package org.agoncal.application.pfm.services.impl;

import org.agoncal.application.pfm.Origin;
import org.agoncal.application.pfm.model.ClientCardInfo;
import org.agoncal.application.pfm.model.Currency;
import org.agoncal.application.pfm.model.Money;
import org.agoncal.application.pfm.model.Operation;
import org.agoncal.application.pfm.services.CategoryRepo;
import org.agoncal.application.pfm.services.PFMService;
import org.agoncal.application.pfm.services.TransactionRepo;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Arrays.asList;

@Origin
public class PFMServiceImpl implements PFMService {
    private final TransactionRepo transactionRepo;
    private final CategoryRepo categoryRepo;

    @Inject
    public PFMServiceImpl(TransactionRepo transactionRepo, CategoryRepo categoryRepo) {
        this.transactionRepo = transactionRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<? extends Operation> operations(final String productId, final Date from, final Date to) {
        return asList(
                new Operation.Simple(new Money.Simple(TEN, new Currency.Simple()), 100, new Date()),
                new Operation.Simple(new Money.Simple(ONE, new Currency.Simple()), 100, new Date())
        );
    }

    @Override
    public List<ClientCardInfo> cards() {
        return asList(
                new ClientCardInfo("card1"),
                new ClientCardInfo("card2")
        );
    }

    @Override
    public String categoryOf(Integer mcc) {
        return mcc == 100 ? "cat100" : "other";
    }

    @Override
    public List<String> categories() {
        return categoryRepo.findAll();
    }
}
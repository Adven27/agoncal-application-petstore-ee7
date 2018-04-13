package org.agoncal.application.petstore.view.shopping;

import lombok.Getter;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.CatchException;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Loggable
@CatchException
public class PfmView implements Serializable {
    @Getter private final List<Transaction> transactions;

    @Inject
    public PfmView(TransactionDAO transactionDAO) {
        transactions = transactionDAO.getTransactions();
    }
}
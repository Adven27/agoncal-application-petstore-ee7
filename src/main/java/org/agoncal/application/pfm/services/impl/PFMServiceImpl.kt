package org.agoncal.application.pfm.services.impl

import org.agoncal.application.pfm.Origin
import org.agoncal.application.pfm.model.ClientCardInfo
import org.agoncal.application.pfm.model.Currency
import org.agoncal.application.pfm.model.Money
import org.agoncal.application.pfm.model.Operation
import org.agoncal.application.pfm.services.CategoryRepo
import org.agoncal.application.pfm.services.PFMService
import org.agoncal.application.pfm.services.TransactionRepo
import org.joda.time.LocalDateTime.now
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.util.*
import javax.inject.Inject

@Origin
class PFMServiceImpl
@Inject constructor(private val transactionRepo: TransactionRepo, private val categoryRepo: CategoryRepo) : PFMService {

    override fun operations(productId: String, from: Date, to: Date): List<Operation> {
        return when (productId) {
            "card1" -> listOf(
                    Operation.Simple(Money.Simple(TEN, Currency.Simple()), 100, now().minusWeeks(1).toDate()),
                    Operation.Simple(Money.Simple(ONE, Currency.Simple()), 100, now().toDate()),
                    Operation.Simple(Money.Simple(ONE, Currency.Simple()), 200, now().plusWeeks(1).toDate()))
            "card2" -> listOf(Operation.Simple(Money.Simple(TEN, Currency.Simple()), 200, now().toDate()))
            else -> emptyList()
        }.filter { it.datetime in from..to }
    }

    override fun cards(): List<ClientCardInfo> {
        return listOf(ClientCardInfo("card1"), ClientCardInfo("card2"))
    }

    override fun categoryOf(mcc: Int?): String {
        return if (mcc == 100) "cat100" else "other"
    }

    override fun categories(): List<String> {
        return categoryRepo.findAll()
    }
}
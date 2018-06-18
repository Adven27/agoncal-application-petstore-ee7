package org.agoncal.application.pfm.services.impl

import org.agoncal.application.pfm.*
import org.agoncal.application.pfm.Currency
import org.agoncal.application.pfm.services.CategoryRepo
import org.agoncal.application.pfm.services.PFMService
import org.agoncal.application.pfm.services.TransactionRepo
import org.joda.time.LocalDateTime
import org.joda.time.LocalDateTime.now
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import java.util.*
import javax.inject.Inject

@Origin
class PFMServiceImpl
@Inject constructor(private val transactionRepo: TransactionRepo, private val categoryRepo: CategoryRepo) : PFMService {

    override fun operations(productId: String, from: Date, to: Date): MutableList<out Operation> = when (productId) {
        "card1" -> listOf(
                operation(TEN, 100, now().minusWeeks(1)),
                operation(ONE, 100, now()),
                operation(ONE, 200, now().plusWeeks(1)))
        "card2" -> listOf(operation(TEN, 200, now()))
        else -> emptyList()
    }.filter { it.datetime in from..to }.toMutableList()

    private fun operation(amount: BigDecimal, mcc: Int, date: LocalDateTime) =
            Operation.Simple(Money.Simple(amount, Currency.Simple()), mcc, date.toDate())

    override fun cards(): List<ClientCardInfo> =
            listOf(ClientCardInfo("card1", null), ClientCardInfo("card2", null))

    override fun categoryOf(mcc: Int?): String = when (mcc) {
        100 -> "cat_100"
        200 -> "cat_200"
        300 -> "cat_300"
        else -> "other"
    }

    override fun categories(): List<String> = categoryRepo.findAll()
}
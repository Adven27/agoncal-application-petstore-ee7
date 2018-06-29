package org.agoncal.application.pfm.services.impl

import org.agoncal.application.pfm.*
import org.agoncal.application.pfm.Currency
import org.agoncal.application.pfm.services.CategoryRepo
import org.agoncal.application.pfm.services.PFMService
import org.agoncal.application.pfm.services.TransactionRepo
import org.joda.time.LocalDateTime
import org.joda.time.LocalDateTime.now
import java.util.*
import javax.inject.Inject

@Origin
class PFMServiceImpl
@Inject constructor(private val transactionRepo: TransactionRepo, private val categoryRepo: CategoryRepo) : PFMService {

    override fun operations(productId: String, from: Date, to: Date): MutableList<out Operation> = when (productId) {
        "card1" -> listOf(
                operation(100.9, 100, now().minusWeeks(1)),
                operation(50.1, 100, now()),
                operation(230.0, 200, now()),
                operation(310.0, 300, now()),
                operation(425.5, 400, now()),
                operation(535.5, 500, now()),
                operation(625.5, 600, now()),
                operation(725.5, 700, now()),
                operation(825.5, 800, now()),
                operation(259.5, 900, now()),
                operation(925.5, 999, now()),
                operation(75.5, 200, now().plusWeeks(1)))
        "card2" -> listOf(operation(2.5, 200, now()))
        "card3" -> listOf(operation(25.5, 200, now()), operation(250.1, 300, now()))
        else -> emptyList()
    }.filter { it.datetime in from..to }.toMutableList()

    private fun operation(amount: Double, mcc: Int, date: LocalDateTime) =
            Operation.Simple(Money.Simple(amount.toBigDecimal(), Currency.Simple()), mcc, date.toDate())

    override fun cards(): List<ClientCardInfo> = listOf(
            ClientCardInfo("card1", null),
            ClientCardInfo("card2", null),
            ClientCardInfo("card3", null))

    override fun categoryOf(mcc: Int?): String = when (mcc) {
        100 -> "Airlines"
        200 -> "Bakeries"
        300 -> "Shoe Stores"
        400 -> "Book Stores"
        500 -> "Antique Shops"
        600 -> "Florists"
        700 -> "Laundries"
        800 -> "Detective Agencies"
        900 -> "Golf Courses"
        else -> "Hospitals"
    }
    override fun categories(): List<String> = categoryRepo.findAll()
}
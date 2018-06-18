package org.agoncal.application.pfm

import java.io.Serializable
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Transaction(
        @get:Id var id: Long? = null,
        var category: String? = null,
        var dt: Date? = null,
        var amount: BigDecimal? = null)

@Entity
data class Category(@get:Id var id: Long? = null, var category: String? = null)

data class ClientCardInfo(val identifier: String, val number: String?, val name: String? = null)

interface Money : Serializable {
    val amount: BigDecimal
    val currency: Currency

    data class Simple(override val amount: BigDecimal, override val currency: Currency) : Money
}

interface Currency : Serializable {
    val name: String

    data class Simple(override val name: String = "SIMPLE") : Currency
}

interface Operation {
    val datetime: Date
    val amount: Money
    val mcc: Int

    data class Simple(override val amount: Money, override val mcc: Int, override val datetime: Date) : Operation
}
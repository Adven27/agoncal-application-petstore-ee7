package org.agoncal.application.pfm.model

import java.io.Serializable
import java.math.BigDecimal

interface Money : Serializable {
    val amount: BigDecimal
    val currency: Currency

    data class Simple(override val amount: BigDecimal, override val currency: Currency) : Money
}
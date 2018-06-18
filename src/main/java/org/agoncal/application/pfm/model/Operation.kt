package org.agoncal.application.pfm.model

import java.util.*

interface Operation {
    val datetime: Date
    val amount: Money
    val mcc: Int

    data class Simple(override val amount: Money, override val mcc: Int, override val datetime: Date) : Operation
}
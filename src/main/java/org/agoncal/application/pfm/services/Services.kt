package org.agoncal.application.pfm.services

import org.agoncal.application.pfm.ClientCardInfo
import org.agoncal.application.pfm.Operation
import org.agoncal.application.pfm.Transaction

import java.io.Serializable
import java.util.*

interface TransactionRepo : Serializable {
    fun findAll(): List<Transaction>
}

interface PFMService : Serializable {
    fun operations(productId: String, from: Date, to: Date): MutableList<out Operation>

    fun categories(): List<String>

    fun cards(): List<ClientCardInfo>

    fun categoryOf(mcc: Int?): String
}

interface CategoryRepo : Serializable {
    fun findAll(): List<String>
}
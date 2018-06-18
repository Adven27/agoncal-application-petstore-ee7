package org.agoncal.application.pfm.model

import java.io.Serializable

interface Currency : Serializable {
    val name: String

    class Simple(override val name: String = "SIMPLE") : Currency
}

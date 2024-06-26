package com.example.tpo_da1.ui.domain
import java.io.Serializable

data class Deal(
    val title: String,
    val normalPrice: String = "",
    val salePrice: String = "",
    val thumb: String = "",
    val dealID: String = ""
): Serializable {
    constructor(): this("","","","","")
}

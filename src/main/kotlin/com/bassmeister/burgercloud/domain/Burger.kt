package com.bassmeister.burgercloud.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Document
data class Burger(
    @field:NotEmpty(message = "Your Burger must have a name")
    val name: String,
    @field:Size(min = 3, message = "Please select at least three ingredients")
    val ingredients: List<Ingredient>,
    val isStandardBurger: Boolean
) {

    @Id
    var id: String = ""

    var createdAt: LocalDateTime? = null

    fun setCreatedAt() {
        createdAt = LocalDateTime.now()
    }


}
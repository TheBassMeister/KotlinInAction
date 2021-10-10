package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import javax.validation.constraints.Size

@Document(collection = "Burger_Order")
data class Order(
    val customer: Customer,
    @field:Size(min = 1, message = "An Order must contain at least one burger")
    val burgers: List<BurgerOrder>,
    @JsonIgnore
    var ccNumber: String = "",
    @JsonIgnore
    val ccExpDate: String = "",
    @JsonIgnore
    val ccCVC: String = ""
) {

    @Id
    var id: String = ""

    var placedAt: LocalDateTime? = null

    fun setCreatedAt() {
        placedAt = LocalDateTime.now()
    }

}
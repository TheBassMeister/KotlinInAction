package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "Burger_Order")
data class Order(
    @ManyToOne @OnDelete(action = OnDeleteAction.CASCADE)
    val customer: Customer,
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var placedAt: LocalDateTime? = null

    @PrePersist
    fun setCreatedAt() {
        placedAt = LocalDateTime.now()
    }

}
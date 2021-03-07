package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.validator.constraints.CreditCardNumber
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Digits
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name="Burger_Order")
data class Order (@ManyToOne @OnDelete(action = OnDeleteAction.CASCADE)
                  val customer:Customer,
                  @OneToMany(fetch = FetchType.EAGER)
                  @JoinColumn(name="order_id") //Need to rethink, as now one burger can only be part of one order
                  @field:Size(min=1, message = "An Order must contain at least one burger")
                  val burger:List<Burger>,
                  @JsonIgnore
                  @field:CreditCardNumber(message = "Not a valid credit card number")
                  var creditCardNumber:String="",
                  @JsonIgnore
                  @field:Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
                  val expDate:String="",
                  @JsonIgnore
                  @field:Digits(integer=3, fraction=0, message="Invalid CVV")
                  val ccExp:String=""){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L

    var placedAt: LocalDateTime?=null

    @PrePersist
    fun setCreatedAt(){
        placedAt= LocalDateTime.now()
    }

}
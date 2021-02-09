package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="Burger_Order")
data class Order (@ManyToOne(cascade = [CascadeType.MERGE]) val customer:Customer,
                  @OneToMany(targetEntity = Burger::class, cascade = [CascadeType.MERGE]) val burger:List<Burger>,
                  @JsonIgnore val creditCardNumber:String="", @JsonIgnore val expDate:String="", @JsonIgnore val ccExp:String=""){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L

    var placedAt: LocalDateTime?=null

    @PrePersist
    fun setCreatedAt(){
        placedAt= LocalDateTime.now()
    }

}
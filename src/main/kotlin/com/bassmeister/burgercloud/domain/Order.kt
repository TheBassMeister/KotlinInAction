package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="Burger_Order")
data class Order (@ManyToOne @OnDelete(action = OnDeleteAction.CASCADE)
                  val customer:Customer,
                  @OneToMany //Need to think about cascading in the future after refreshing on Hibernate
                  val burger:List<Burger>,
                  @JsonIgnore
                  val creditCardNumber:String="",
                  @JsonIgnore
                  val expDate:String="",
                  @JsonIgnore
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
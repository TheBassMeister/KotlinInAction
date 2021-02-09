package com.bassmeister.burgercloud.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Burger(val name:String,
                  @ManyToMany(targetEntity = Ingredient::class) val ingredients:List<Ingredient>){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L

    var createdAt:LocalDateTime?=null

    @PrePersist
    fun setCreatedAt(){
       createdAt= LocalDateTime.now()
    }


}
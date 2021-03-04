package com.bassmeister.burgercloud.domain

import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
data class Burger(
    @field:NotEmpty(message="Your Burger must have a name")
    val name:String,
    @field:Size(min=3, message = "Please select at least three ingredients")
    @ManyToMany(targetEntity = Ingredient::class, fetch = FetchType.EAGER) val ingredients:List<Ingredient>){

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L

    var createdAt:LocalDateTime?=null

    @PrePersist
    fun setCreatedAt(){
       createdAt= LocalDateTime.now()
    }


}
package com.bassmeister.burgercloud.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "OrderedBurgers")
class BurgerOrder(

    val burger: Burger,
    val amount:Int){

    @Id
    val id:String=""
}
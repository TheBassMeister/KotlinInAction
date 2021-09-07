package com.bassmeister.burgercloud.api.converters


import com.bassmeister.burgercloud.api.BurgerModel
import com.bassmeister.burgercloud.controllers.BurgerController
import com.bassmeister.burgercloud.domain.Burger
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

object BurgerConverter {

    fun convertBurgerList(burgers: Iterable<Burger>): List<BurgerModel> {
        val burgerModels=burgers.map{ convertBurger(it,false)}
        burgerModels.forEach{
            it.add(
                linkTo(methodOn(BurgerController::class.java).getBurger(it.id)).withSelfRel())
        }
        return burgerModels
    }

    fun convertBurger(burger:Burger, withLink:Boolean):BurgerModel{
        var model=BurgerModel(burger.id, burger.name, burger.createdAt, burger.isStandardBurger,
            IngredientConverter.convertIngredientList(burger.ingredients))
        if(withLink)
            Converter.addLinkToRestHome(model)

        return model;
    }
}
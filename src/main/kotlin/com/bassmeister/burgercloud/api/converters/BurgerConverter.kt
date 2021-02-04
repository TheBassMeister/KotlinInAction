package com.bassmeister.burgercloud.api.converters


import com.bassmeister.burgercloud.api.BurgerModel
import com.bassmeister.burgercloud.controllers.BurgerController
import com.bassmeister.burgercloud.controllers.RootController
import com.bassmeister.burgercloud.domain.Burger
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*

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
        var model=BurgerModel(burger.id, burger.name, burger.createdAt,
            IngredientConverter.convertIngredientList(burger.ingredients))
        if(withLink)
            model.add(Link.of(linkTo(RootController::class.java).toString()).withRel("root"))

        return model;
    }
}
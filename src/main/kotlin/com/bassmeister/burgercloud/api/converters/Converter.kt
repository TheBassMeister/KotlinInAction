package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.controllers.RootController
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

object Converter{

    fun addLinkToRestHome(model:RepresentationModel<*>){
        model.add(Link.of(WebMvcLinkBuilder.linkTo(RootController::class.java).toString()).withRel("root"))
    }
}
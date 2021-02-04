package com.bassmeister.burgercloud.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel
import com.bassmeister.burgercloud.api.IngredientModel

class IngredientModel @JsonCreator constructor(val id: String, val name: String, val type: String)
    : RepresentationModel<IngredientModel?>()
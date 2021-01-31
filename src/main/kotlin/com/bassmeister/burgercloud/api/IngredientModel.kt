package com.bassmeister.burgercloud.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel
import com.bassmeister.burgercloud.api.IngredientModel

class IngredientModel @JsonCreator constructor(
    @param:JsonProperty("id") val id: String,
    @param:JsonProperty("name") val name: String,
    @param:JsonProperty("type") val type: String
) : RepresentationModel<IngredientModel?>()
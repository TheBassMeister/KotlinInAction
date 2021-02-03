package com.bassmeister.burgercloud.api

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.hateoas.RepresentationModel

class UserModel @JsonCreator constructor(
    val id: Long, val firstName: String, val lastName: String, val street: String, val city: String,
    val state: String, val zip: String, val phoneNumber: String
) : RepresentationModel<UserModel?>()
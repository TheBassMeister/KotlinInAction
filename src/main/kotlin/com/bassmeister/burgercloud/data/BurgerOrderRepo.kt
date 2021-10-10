package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.BurgerOrder
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource

interface BurgerOrderRepo : ReactiveMongoRepository<BurgerOrder, String>
package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.BurgerOrder
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource

@RestResource(exported = false)
interface BurgerOrderRepo : CrudRepository<BurgerOrder, Long>
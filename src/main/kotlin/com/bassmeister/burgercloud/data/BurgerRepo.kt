package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Burger
import org.springframework.data.repository.CrudRepository

interface BurgerRepo : CrudRepository<Burger, Long>
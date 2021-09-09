package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.BurgerModel
import com.bassmeister.burgercloud.api.converters.BurgerConverter
import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Burger
import org.hibernate.JDBCException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(path = ["/api/burgers"], produces = ["application/hal+json"])
class BurgerController(private val burgerRepo: BurgerRepo, private val orderRepo: OrderRepo) {

    @GetMapping
    fun getBurgers(): ResponseEntity<List<BurgerModel>> {
        var burgers = burgerRepo.findAll()
        var converted = BurgerConverter.convertBurgerList(burgers)
        return ResponseEntity(converted, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getBurger(@PathVariable id: Long): ResponseEntity<BurgerModel> {
        var burger = burgerRepo.findById(id)
        if (burger.isPresent) {
            return ResponseEntity(BurgerConverter.convertBurger(burger.get(), true), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    fun addNewBurger(@RequestBody @Validated burger: Burger): ResponseEntity<BurgerModel> {
        if(!checkIfNewBurgerIsStandard(burger)) {
            val newBurger = burgerRepo.save(burger)
            return ResponseEntity(BurgerConverter.convertBurger(newBurger, true), HttpStatus.CREATED)
        }
        return ResponseEntity(BurgerConverter.convertBurger(burger, true), HttpStatus.CREATED)
    }

    private fun checkIfNewBurgerIsStandard(burger: Burger): Boolean {
        val burgerFromRepo=burgerRepo.findByName(burger.name)
        return burgerFromRepo?.isStandardBurger ?:false
    }

    @DeleteMapping("/{id}")
    fun deleteBurger(@PathVariable id: Long): ResponseEntity<BurgerModel> {
        burgerRepo.deleteById(id)
        return ResponseEntity.noContent().build<BurgerModel>()
    }

    //No PutMapping as updating a burger is not allowed (my design decision).
    // Customers would have to delete and create a new one
}
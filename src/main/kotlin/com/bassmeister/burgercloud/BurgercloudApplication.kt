package com.bassmeister.burgercloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class BurgercloudApplication

fun main(args: Array<String>) {
    runApplication<BurgercloudApplication>(*args)
}


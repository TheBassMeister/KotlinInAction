package com.bassmeister.burgercloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.boot.web.client.RestTemplateBuilder

import org.springframework.web.client.RestTemplate




@SpringBootApplication
class BurgercloudApplication

fun main(args: Array<String>) {
	runApplication<BurgercloudApplication>(*args)
}


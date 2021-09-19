package com.bassmeister.burgercloud.controllers.config

import com.bassmeister.burgercloud.controllers.handlers.BurgerHandler
import com.bassmeister.burgercloud.controllers.handlers.IngredientHandler
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


@Configuration
class RouteConfiguration(private val burgerHandler: BurgerHandler, private val ingredientHandler: IngredientHandler) {

    @Bean
    fun route() = router {
        GET(""){_->getDefaultRoutes()}

        //Burgers
        GET("/burgers") { _ -> burgerHandler.getAllBurgers() }
        GET("/burgers/{id}").invoke(burgerHandler::getBurger)
        contentType(APPLICATION_JSON).nest {
            POST("/burgers").invoke(burgerHandler::handleRequest)
        }
        //Ingredients
        GET("/ingredients").invoke (ingredientHandler::getAllIngredients)
        GET("/ingredients/{id}").invoke(ingredientHandler::getIngredientById)
        //TEST
        GET("/**"){_->ServerResponse.notFound().build()}

    }

    fun getDefaultRoutes():Mono<ServerResponse>{
        //Not the most elegant solution, but HATEOAS and router functions do not work well together
        val burgerRoute="http://localhost:8080/api/burgers"
        val ingredientRoute="http://localhost:8080/api/ingredients"
        var routes= listOf(burgerRoute, ingredientRoute)
        return ServerResponse.ok().body(BodyInserters.fromValue(routes))
    }

    @Bean
    fun webFluxProperties(): WebFluxProperties {
        return WebFluxProperties()
    }
}
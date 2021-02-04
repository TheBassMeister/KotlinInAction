package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.data.UserRepository
import com.bassmeister.burgercloud.domain.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DevelopmentConfig {

    @Bean
    fun dataLoader(ingredientRepo:IngredientRepo, userRepo:UserRepository, burgerRepo:BurgerRepo,
    orderRepo: OrderRepo, pwEncoder: PasswordEncoder): CommandLineRunner {
        return CommandLineRunner {
            val regBun=Ingredient("REG_BUN", "Regular Bun", IngredientType.BUN)
            val sesBun=Ingredient("SES_BUN", "Sesame Bun", IngredientType.BUN)
            val noGlut=Ingredient("NOGLUT", "Gluten free Bun", IngredientType.BUN)
            val ketchup=Ingredient("KETCHUP", "Ketchup", IngredientType.SAUCE)
            val mayo=Ingredient("MAYO", "Mayonnaise", IngredientType.SAUCE)
            val letc=Ingredient("LETC", "Lettuce", IngredientType.VEG)
            val toma=Ingredient("TOMA", "Tomatoes", IngredientType.VEG)
            val bacon=Ingredient("BAC", "Bacon", IngredientType.OTHER)
            val pickles=Ingredient("PICK", "Pickles", IngredientType.OTHER)
            val cheese=Ingredient("CHES", "Cheese", IngredientType.OTHER)
            ingredientRepo.saveAll(listOf(regBun,sesBun,noGlut,ketchup,mayo,letc, toma, bacon,
            pickles, cheese))

            val burglar= Customer(pwEncoder.encode("BurglarHam"),"Ham", "Burglar",
                "123 Fries Avenue", "Big Mac", "TX",
                "76227", "123-123-1234")
            val ronald=Customer(pwEncoder.encode("RonaldMcDonald"),"Ronald", "Donald",
                "123 Milkshake Boulevard", "Royal TS", "TX",
                "76227", "123-123-1234")
            userRepo.saveAll(listOf(burglar, ronald))

            val burger1Ingredients= listOf(regBun, mayo,cheese, pickles)
            val standardBurger=Burger("Standard Burger",burger1Ingredients)
            burgerRepo.save(standardBurger)

            orderRepo.save(Order(burglar, listOf(standardBurger),"378618187748325", "03/22", "350"))

        }
    }

}
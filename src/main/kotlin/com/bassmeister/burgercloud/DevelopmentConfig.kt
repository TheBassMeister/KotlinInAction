package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Ingredient
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DevelopmentConfig {

    @Bean
    fun dataLoader(ingredientRepo:IngredientRepo): CommandLineRunner {
        return CommandLineRunner {
            ingredientRepo.save(Ingredient("REG_BUN", "Regular Bun", IngredientType.BUN))
            ingredientRepo.save(Ingredient("SES_BUN", "Sesame Bun", IngredientType.BUN))
            ingredientRepo.save(Ingredient("NOGLUT", "Gluten free Bun", IngredientType.BUN))
            ingredientRepo.save(Ingredient("KETCHUP", "Ketchup", IngredientType.SAUCE))
            ingredientRepo.save(Ingredient("MAYO", "Mayonnaise", IngredientType.SAUCE))
            ingredientRepo.save(Ingredient("LETC", "Lettuce", IngredientType.VEG))
            ingredientRepo.save(Ingredient("TOMA", "Tomatoes", IngredientType.VEG))
            ingredientRepo.save(Ingredient("BAC", "Bacon", IngredientType.OTHER))
            ingredientRepo.save(Ingredient("PICK", "Pickles", IngredientType.OTHER))
            ingredientRepo.save(Ingredient("CHES", "Cheese", IngredientType.OTHER))
        }
    }

}
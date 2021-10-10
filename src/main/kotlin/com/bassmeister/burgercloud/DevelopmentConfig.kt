package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.data.*
import com.bassmeister.burgercloud.domain.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean


@Configuration
class DevelopmentConfig {

    @Bean
    fun dataLoader(
        ingredientRepo: IngredientRepo, userRepo: CustomerRepo, burgerRepo: BurgerRepo,
        orderRepo: OrderRepo, burgerOrderRepo: BurgerOrderRepo, pwEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            //As this is a test application repos get cleared (In Real world scenario this would be disabled in prod with spring profile
            orderRepo.deleteAll().subscribe()
            userRepo.deleteAll().subscribe()
            burgerRepo.deleteAll().subscribe()
            burgerOrderRepo.deleteAll().subscribe()

            val regBun = Ingredient("REG_BUN", "Regular Bun", IngredientType.BUN)
            val sesBun = Ingredient("SES_BUN", "Sesame Bun", IngredientType.BUN)
            val noGlut = Ingredient("NOGLUT", "Gluten free Bun", IngredientType.BUN)
            val ketchup = Ingredient("KETCHUP", "Ketchup", IngredientType.SAUCE)
            val mayo = Ingredient("MAYO", "Mayonnaise", IngredientType.SAUCE)
            val letc = Ingredient("LETC", "Lettuce", IngredientType.VEG)
            val toma = Ingredient("TOMA", "Tomatoes", IngredientType.VEG)
            val bacon = Ingredient("BAC", "Bacon", IngredientType.OTHER)
            val pickles = Ingredient("PICK", "Pickles", IngredientType.OTHER)
            val cheese = Ingredient("CHES", "Cheese", IngredientType.OTHER)
            ingredientRepo.saveAll(
                listOf(
                    regBun, sesBun, noGlut, ketchup, mayo, letc, toma, bacon,
                    pickles, cheese
                )
            ).subscribe()

            val burglar = Customer(
                "Ham", "Burglar",
                "123 Fries Avenue", "Big Mac", "TX",
                "76227", "123-123-1234"
            )
            burglar.pass = "BurglarHam"
            burglar.id="1" //Would typically be auto generated, hardcoded at the moment while user part is not implemented atm
            val ronald = Customer(
                "Ronald", "Donald",
                "123 Milkshake Boulevard", "Royal TS", "TX",
                "76227", "123-123-1234"
            )
            ronald.pass = "RonaldMcDonald"
            ronald.id="2"
            userRepo.saveAll(listOf(burglar, ronald)).subscribe()

            val burger1Ingredients = listOf(regBun, mayo, cheese, pickles)
            val standardBurger = Burger("Standard Burger", burger1Ingredients, true);
            burgerRepo.save(standardBurger).subscribe()

            val burger2Ingredients = listOf(sesBun, ketchup, mayo, letc, bacon, pickles, cheese);

            val standardBurger2 = Burger("The One with everything", burger2Ingredients, true);
            burgerRepo.save(standardBurger2).subscribe()

            val burgerOrder = BurgerOrder(standardBurger, 2)
            burgerOrderRepo.save(burgerOrder).subscribe()
            val burgerOrder2 = BurgerOrder(standardBurger2, 3)
            burgerOrderRepo.save(burgerOrder2).subscribe()

            val burgerOrders = listOf(burgerOrder, burgerOrder2)

            val burglarWithId=userRepo.getUserByLastName("Burglar").blockFirst()
            burglarWithId?.let {
                orderRepo.save(Order(burglarWithId, burgerOrders, "378618187748325", "03/22", "350")).subscribe()
            }

        }
    }

    @Bean
    fun mailSender(): JavaMailSender {
        return JavaMailSenderImpl()
    }

    @Bean
    @Primary
    fun springValidator(): Validator {
        return LocalValidatorFactoryBean()
    }

}
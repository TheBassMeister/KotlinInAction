package com.bassmeister.burgercloud.messaging

import com.bassmeister.burgercloud.domain.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092" ])
class BurgerMessagingTest(@Autowired val consumer:OrderListener, @Autowired val producer: OrderService) {


    @Test
    fun `Send Test Order`() {
        val order=createTestOrder()
        producer.sendOrder(order)
    }

    private fun createTestOrder():Order{
        val regBun= Ingredient("REG_BUN", "Regular Bun", IngredientType.BUN)
        val ketchup=Ingredient("KETCHUP", "Ketchup", IngredientType.SAUCE)
        val bacon=Ingredient("BAC", "Bacon", IngredientType.OTHER)

        val burger1Ingredients= listOf(regBun, ketchup,bacon)
        val standardBurger= Burger("Standard Burger",burger1Ingredients)

        val burglar= Customer("BurglarHam","Ham", "Burglar",
            "123 Fries Avenue", "Big Mac", "TX",
            "76227", "123-123-1234")

        return Order(burglar, listOf(standardBurger),"323445234545","09/22","333")
    }

}
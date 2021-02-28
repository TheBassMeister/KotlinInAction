package com.bassmeister.burgercloud.mail

import com.bassmeister.burgercloud.domain.*
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer
import org.springframework.integration.support.AbstractIntegrationMessageBuilder
import org.springframework.integration.support.MessageBuilder
import org.springframework.stereotype.Component
import javax.mail.Message
import javax.mail.internet.InternetAddress

@Component
class EmailOrderTransformer(): AbstractMailMessageTransformer<Order>() {

    override fun doTransform(mailMessage: Message?): AbstractIntegrationMessageBuilder<Order>? {
        var burgerOrder=processOrder(mailMessage)
        if(burgerOrder!=null){
            return MessageBuilder.withPayload(burgerOrder)
        }
        return null
    }

    private fun processOrder(mailMessage: Message?): Order? {
        mailMessage?.let {
            var subject = mailMessage.subject
            if (subject.toUpperCase().contains("BURGER ORDER")) {
                var email = (mailMessage.from[0] as InternetAddress).address
                var content = mailMessage.content.toString()
                return parseEmailToOrder(email, content)
            }

        }
        return null
    }

    private fun parseEmailToOrder(email: String?, content: String): Order {
        //Here would be some fancy code to parse email content to an order
        //but that would distract too much from learning Spring Boot
        return createTestOrder()
    }

    private fun createTestOrder():Order{
        val regBun= Ingredient("REG_BUN", "Regular Bun", IngredientType.BUN)
        val ketchup= Ingredient("KETCHUP", "Ketchup", IngredientType.SAUCE)
        val bacon= Ingredient("BAC", "Bacon", IngredientType.OTHER)

        val burger1Ingredients= listOf(regBun, ketchup,bacon)
        val standardBurger= Burger("Standard Burger",burger1Ingredients)

        val burglar= Customer("BurglarHam","Ham", "Burglar",
            "123 Fries Avenue", "Big Mac", "TX",
            "76227", "123-123-1234")

        return Order(burglar, listOf(standardBurger),"323445234545","09/22","333")
    }


}
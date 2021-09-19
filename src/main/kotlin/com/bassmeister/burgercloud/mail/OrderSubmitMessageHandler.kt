package com.bassmeister.burgercloud.mail

import com.bassmeister.burgercloud.domain.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.handler.GenericHandler
import org.springframework.messaging.MessageHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI

//@Component
class OrderSubmitMessageHandler(val apiProps:ApiProperties, @Autowired val rest:RestTemplate):GenericHandler<Order> {

    override fun handle(payload: Order?, headers: MessageHeaders?): Any? {
        payload?.let {
            rest.postForObject(apiProps.url, payload, String::class.java)
        }
        return null
    }
}
package com.bassmeister.burgercloud.mail

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.mail.dsl.Mail
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec
import java.util.concurrent.TimeUnit


//@Configuration
class BurgerOrderEmailIntegration {


    @Bean
    fun burgerOrderFlow(emailProps:EmailProperties, emailOrderTransformer: EmailOrderTransformer,
        orderSubmitHandler:OrderSubmitMessageHandler):IntegrationFlow{
        return IntegrationFlows
            .from(
                Mail.imapInboundAdapter(emailProps.getImapUrl())
            ) { e: SourcePollingChannelAdapterSpec ->
                e.poller(
                    Pollers.fixedDelay(emailProps.pollRate, TimeUnit.HOURS)
                )
            }
            .transform(emailOrderTransformer)
            .handle(orderSubmitHandler)
            .get()
    }

}
package com.bassmeister.burgercloud.mail

import junit.framework.TestCase.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.transformer.MessageTransformationException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@SpringBootTest
class EmailOrderTransformerTest(@Autowired val mailTransformer:EmailOrderTransformer, @Autowired val mailSender: JavaMailSender) {

    @Test
    fun `Email Transformer Test`() {
        var mail=createMimeMessage()
        var message=MessageBuilder.withPayload(mail).build()
        var result=mailTransformer.transform(message)
        assertNotNull(result)
    }

    @Test
    fun `Email With Wrong Subject Test`() {
        var mail=createMimeMessage()
        mail.subject="WRONG SUBJECT"
        var message=MessageBuilder.withPayload(mail).build()
        var result= assertThrows<MessageTransformationException>{
            mailTransformer.transform(message)
        }
    }

    private fun createMimeMessage():MimeMessage{
        var mail=mailSender.createMimeMessage()
        var mailHelper=MimeMessageHelper(mail,true)
        mail.subject="BURGER ORDER"
        mailHelper.setFrom(InternetAddress("TEST"))
        mailHelper.setText("Here would be an order")
        return mail
    }

}
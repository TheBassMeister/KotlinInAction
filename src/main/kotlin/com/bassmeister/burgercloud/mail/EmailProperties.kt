package com.bassmeister.burgercloud.mail

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix="burgercloud.email")
@Component
data class EmailProperties(var userName:String="",var password:String="", var host: String="", var mailBox:String="", var pollRate:Long=1L) {

    fun getImapUrl():String{
        return String.format("imaps://%s:%s@%s/%s", userName,password,host, mailBox)
    }
}
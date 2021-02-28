package com.bassmeister.burgercloud.mail

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "burgercloud.api")
@Component
data class ApiProperties(var url:String="")
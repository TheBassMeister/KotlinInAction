package com.bassmeister.burgercloud.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class BurgerCloudSecurityConfig {

    @Bean
    fun passwordEncoder():PasswordEncoder{
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        //TODO: MAYBE NOT ALLOW ALL
        return http.authorizeExchange().anyExchange().permitAll().and().csrf().disable().build()
    }

//    override fun configure(http: HttpSecurity?) {
//        http {
//            httpBasic {}
//            authorizeRequests {
//                authorize("/api/**", permitAll)
//                authorize("/h2-console/**", permitAll)
//            }
//            csrf {
//                disable()
//            }
//            headers {
//                frameOptions {
//                    disable()
//                }
//            }
//
//        }
//    }
}

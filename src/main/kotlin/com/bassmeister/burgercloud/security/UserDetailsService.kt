package com.bassmeister.burgercloud.security

import com.bassmeister.burgercloud.data.CustomerRepo
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserDetailsService(private val customerRepo: CustomerRepo) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        val user = customerRepo.getUserByLastName(username).next()
        return user.map {
            it as UserDetails
        }.switchIfEmpty(
            return Mono.empty()
        )

    }
}
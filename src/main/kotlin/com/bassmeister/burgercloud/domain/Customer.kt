package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.Digits
import javax.validation.constraints.NotEmpty

@Document
data class Customer(
    @field:NotEmpty(message = "First Name must be set")
    var firstName: String = "",
    @field:NotEmpty(message = "Last Name must be set")
    var lastName: String = "",
    @field:NotEmpty(message = "Street must be set")
    var street: String = "",
    @field:NotEmpty(message = "City must be set")
    var city: String = "",
    @field:NotEmpty(message = "State must be set")
    var state: String = "",
    @field:Digits(integer = 5, fraction = 0, message = "Invalid Zip")
    var zip: String = "",
    @field:NotEmpty(message = "Phone Number must be set")
    var phoneNumber: String = ""
) : UserDetails {

    @Transient
    @JsonIgnore
    val pwEncoder: PasswordEncoder = BCryptPasswordEncoder()

    @Id
    var id: String = ""

    @JsonIgnore
    var pass: String = ""
        set(value) {
            field = pwEncoder.encode(value)
        }

    @JsonIgnore
    override fun getPassword(): String {
        return pass
    }

    @JsonIgnore
    override fun getUsername(): String {
        return lastName
    }

    @JsonIgnore
    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isEnabled(): Boolean {
        return true
    }


}

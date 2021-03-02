package com.bassmeister.burgercloud.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Digits
import javax.validation.constraints.NotEmpty

@Entity
data class Customer(
    @field:NotEmpty(message="First Name must be set")
    var firstName:String="",
    @field:NotEmpty(message="Last Name must be set")
    var lastName:String="",
    @field:NotEmpty(message="Street must be set")
    var street:String="",
    @field:NotEmpty(message="City must be set")
    var city:String="",
    @field:NotEmpty(message="State must be set")
    var state:String="",
    @field:Digits(integer = 5, fraction = 0, message = "Invalid Zip")
    var zip:String="",
    @field:NotEmpty(message="Phone Number must be set")
    var phoneNumber:String=""
) {

    @Transient
    @JsonIgnore
    val pwEncoder: PasswordEncoder=BCryptPasswordEncoder()

    var password: String = ""
        set(value) {
            field=pwEncoder.encode(value)
        }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long=0L

}
